/*
 * The MIT License
 *
 * Copyright (c) 2023- High-Mobility GmbH (https://high-mobility.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.highmobility.hmkitfleet.network

import kotlinx.serialization.json.*
import com.highmobility.hmkitfleet.model.Brand
import com.highmobility.hmkitfleet.model.ControlMeasure
import com.highmobility.hmkitfleet.model.ClearanceStatus
import com.highmobility.hmkitfleet.model.RequestClearanceResponse
import kotlinx.serialization.decodeFromString
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.slf4j.Logger
import utils.await
import java.net.HttpURLConnection

internal class ClearanceRequests(
    client: OkHttpClient,
    logger: Logger,
    baseUrl: String,
    private val authTokenRequests: AuthTokenRequests
) : Requests(
    client,
    logger, baseUrl
) {
    suspend fun requestClearance(
        vin: String,
        brand: Brand,
        controlMeasures: List<ControlMeasure>?
    ): Response<RequestClearanceResponse> {
        val body = requestBody(vin, brand, controlMeasures)
        val authToken = authTokenRequests.getAuthToken()

        if (authToken.error != null) return Response(null, authToken.error)

        val request = Request.Builder()
            .url("${baseUrl}/fleets/vehicles")
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer ${authToken.response?.authToken}")
            .post(body)
            .build()

        printRequest(request)

        val call = client.newCall(request)
        val response = call.await()

        return tryParseResponse(response, HttpURLConnection.HTTP_OK) { responseBody ->
            val jsonElement = jsonIg.parseToJsonElement(responseBody) as JsonObject
            val statuses = jsonElement["vehicles"] as JsonArray
            for (statusElement in statuses) {
                val status =
                    jsonIg.decodeFromJsonElement<RequestClearanceResponse>(statusElement)
                if (status.vin == vin) {
                    return Response(status, null)
                }
            }
            Response(null, genericError())
        }
    }

    suspend fun getClearanceStatuses(): Response<List<ClearanceStatus>> {
        // GET /v1/fleets/vehicles
        val authToken = authTokenRequests.getAuthToken()

        if (authToken.error != null) return Response(null, authToken.error)

        val request = Request.Builder()
            .url("${baseUrl}/fleets/vehicles")
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer ${authToken.response?.authToken}")
            .build()

        printRequest(request)

        val call = client.newCall(request)
        val response = call.await()

        return tryParseResponse(response, HttpURLConnection.HTTP_OK) { responseBody ->
            val statuses = jsonIg.parseToJsonElement(responseBody) as JsonArray

            val builder = Array(statuses.size) {
                val statusElement = statuses[it]
                val status = jsonIg.decodeFromJsonElement<ClearanceStatus>(statusElement)
                status
            }

            Response(builder.toList())
        }
    }

    suspend fun getClearanceStatus(vin: String): Response<ClearanceStatus> {
        // GET /v1/fleets/vehicles/{vin}
        val authToken = authTokenRequests.getAuthToken()

        if (authToken.error != null) return Response(null, authToken.error)

        val request = Request.Builder()
            .url("${baseUrl}/fleets/vehicles/${vin}")
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer ${authToken.response?.authToken}")
            .build()

        printRequest(request)

        val call = client.newCall(request)
        val response = call.await()

        return tryParseResponse(response, HttpURLConnection.HTTP_OK) { responseBody ->
            val status = jsonIg.decodeFromString<ClearanceStatus>(responseBody)
            Response(status)
        }
    }

    suspend fun deleteClearance(vin: String): Response<RequestClearanceResponse> {
        // DELETE /v1/fleets/vehicles/{vin}
        val authToken = authTokenRequests.getAuthToken()

        if (authToken.error != null) return Response(null, authToken.error)

        val request = Request.Builder()
            .url("${baseUrl}/fleets/vehicles/$vin")
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer ${authToken.response?.authToken}")
            .delete()
            .build()

        printRequest(request)

        val call = client.newCall(request)
        val response = call.await()

        return tryParseResponse(response, HttpURLConnection.HTTP_OK) { responseBody ->
            val status = jsonIg.decodeFromString<RequestClearanceResponse>(responseBody)
            Response(status)
        }
    }

    private fun requestBody(
        vin: String,
        brand: Brand,
        controlMeasures: List<ControlMeasure>?
    ): RequestBody {
        val vehicle = buildJsonObject {
            put("vin", vin)
            put("brand", jsonIg.encodeToJsonElement(brand))
            if (controlMeasures != null) {
                putJsonObject("control_measures") {
                    for (controlMeasure in controlMeasures) {
                        // polymorphism adds type key to child controlmeasure classes. remove with filter
                        val json = jsonIg.encodeToJsonElement(controlMeasure)
                        val valuesWithoutType = json.jsonObject.filterNot { it.key == "type" }
                        val jsonTrimmed = jsonIg.encodeToJsonElement(valuesWithoutType)
                        put("odometer", jsonTrimmed)
                    }
                }
            }
        }

        val arrayOfVehicles = JsonArray(listOf(vehicle))
        val completeBody = JsonObject(mapOf("vehicles" to arrayOfVehicles))

        val body = completeBody.toString().toRequestBody(mediaType)
        return body
    }
}