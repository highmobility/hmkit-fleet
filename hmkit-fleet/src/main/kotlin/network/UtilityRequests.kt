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
import com.highmobility.hmkitfleet.model.EligibilityStatus
import com.highmobility.hmkitfleet.model.RequestClearanceResponse
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.slf4j.Logger
import utils.await
import java.net.HttpURLConnection

internal class UtilityRequests(
    client: OkHttpClient,
    logger: Logger,
    baseUrl: String,
    private val authTokenRequests: AuthTokenRequests
) : Requests(
    client,
    logger, baseUrl
) {
    suspend fun getEligibility(
        vin: String,
        brand: Brand
    ): Response<EligibilityStatus> {
        val body = requestBody(vin, brand)
        val authToken = authTokenRequests.getAuthToken()

        if (authToken.error != null) return Response(null, authToken.error)

        val request = Request.Builder()
            .url("${baseUrl}/eligibility")
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer ${authToken.response?.authToken}")
            .post(body)
            .build()

        printRequest(request)

        val call = client.newCall(request)
        val response = call.await()

        return tryParseResponse(response, HttpURLConnection.HTTP_OK) { responseBody ->
            val eligibilityStatus = jsonIg.decodeFromString<EligibilityStatus>(responseBody)
            if (eligibilityStatus.vin != vin) logger.warn("VIN in response does not match VIN in request")
            Response(eligibilityStatus, null)
        }
    }

    private fun requestBody(
        vin: String,
        brand: Brand,
    ): RequestBody {
        val vehicle = buildJsonObject {
            put("vin", vin)
            put("brand", jsonIg.encodeToJsonElement(brand))
        }

        val body = jsonIg.encodeToString(vehicle).toRequestBody(mediaType)
        return body
    }
}