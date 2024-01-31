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

import com.highmobility.crypto.AccessCertificate
import com.highmobility.crypto.Crypto
import com.highmobility.crypto.value.PrivateKey
import com.highmobility.hmkitfleet.ClientCertificate
import com.highmobility.value.Bytes
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import okhttp3.OkHttpClient
import okhttp3.Request
import org.slf4j.Logger
import utils.await
import java.net.HttpURLConnection

internal class TelematicsRequests(
    client: OkHttpClient,
    logger: Logger,
    baseUrl: String,
    private val privateKey: PrivateKey,
    private val certificate: ClientCertificate,
    private val crypto: Crypto
) : Requests(
    client, logger, baseUrl
) {
    suspend fun sendCommand(
        command: Bytes, accessCertificate: AccessCertificate
    ): TelematicsResponse {
        val nonce = getNonce()

        if (nonce.error != null) return TelematicsResponse(
            null, listOf(Error(nonce.error.title))
        )

        val encryptedCommand = crypto.createTelematicsContainer(
            command, privateKey, certificate.serial, accessCertificate, Bytes(nonce.response!!)
        )

        return postCommand(encryptedCommand, accessCertificate)
    }

    suspend fun sendCommandV05(
        command: Bytes,
        accessCertificate: AccessCertificate
    ): Response<Bytes> {
        val nonce = getNonce()

        if (nonce.error != null) return Response(null, nonce.error)

        val encryptedCommand =
            crypto.createTelematicsContainer(
                command,
                privateKey,
                certificate.serial,
                accessCertificate,
                Bytes(nonce.response!!)
            )

        val encryptedCommandResponse = postCommandV05(encryptedCommand, accessCertificate)

        if (encryptedCommandResponse.error != null) return encryptedCommandResponse

        val decryptedResponseCommand = crypto.getPayloadFromTelematicsContainer(
            encryptedCommandResponse.response!!,
            privateKey,
            accessCertificate,
        )

        return Response(decryptedResponseCommand)
    }


    private suspend fun getNonce(): Response<String> {
        val request = Request.Builder()
            .url("${baseUrl}/nonces")
            .headers(baseHeaders)
            .post(
                requestBody(
                    mapOf(
                        "serial_number" to certificate.serial.hex
                    )
                )
            ).build()

        printRequest(request)

        val call = client.newCall(request)
        val response = call.await()

        return tryParseResponse(response, HttpURLConnection.HTTP_CREATED) { body ->
            val jsonResponse = Json.parseToJsonElement(body) as JsonObject
            val nonce = jsonResponse.jsonObject["nonce"]?.jsonPrimitive?.content
            Response(nonce, null)
        }
    }

    private suspend fun postCommand(
        encryptedCommand: Bytes,
        accessCertificate: AccessCertificate,
    ): TelematicsResponse {
        val request = Request.Builder()
            .url("${baseUrl}/telematics_commands")
            .headers(baseHeaders)
            .post(
                requestBody(
                    mapOf(
                        "serial_number" to accessCertificate.gainerSerial.hex,
                        "issuer" to certificate.issuer.hex,
                        "data" to encryptedCommand.base64
                    )
                )
            ).build()

        printRequest(request)

        val call = client.newCall(request)
        val response = call.await()
        val responseBody = printResponse(response)

        val responseObject = try {
            if (response.code == 200 || response.code == 400 || response.code == 404 || response.code == 408) {
                val telematicsResponse = Json.decodeFromString<TelematicsCommandResponse>(responseBody)

                // Server only returns encrypted data if status is OK
                val decryptedData = if (telematicsResponse.status == TelematicsCommandResponse.Status.OK) {
                    crypto.getPayloadFromTelematicsContainer(
                        Bytes(telematicsResponse.responseData),
                        privateKey,
                        accessCertificate,
                    )
                } else {
                    telematicsResponse.responseData
                }

                TelematicsResponse(
                    response = TelematicsCommandResponse(
                        status = telematicsResponse.status,
                        message = telematicsResponse.message,
                        responseData = decryptedData
                    )
                )
            } else {
                // try to parse the normal server error format.
                // it will throw and will be caught if server returned unknown format
                TelematicsResponse(errors = Json.decodeFromString(responseBody))
            }
        } catch (e: Exception) {
            TelematicsResponse(errors = listOf(Error(title = "Unknown server response", detail = e.message)))
        }

        return responseObject
    }

    private suspend fun postCommandV05(
        encryptedCommand: Bytes,
        accessCertificate: AccessCertificate,
    ): Response<Bytes> {
        val request = Request.Builder()
            .url("${baseUrl}/telematics_commands")
            .headers(baseHeaders)
            .post(
                requestBody(
                    mapOf(
                        "serial_number" to accessCertificate.gainerSerial.hex,
                        "issuer" to certificate.issuer.hex,
                        "data" to encryptedCommand.base64
                    )
                )
            )
            .build()

        printRequest(request)

        val call = client.newCall(request)
        val response = call.await()

        return tryParseResponse(response, HttpURLConnection.HTTP_OK) { body ->
            val jsonResponse = Json.parseToJsonElement(body) as JsonObject
            val encryptedResponseCommand =
                jsonResponse.jsonObject["response_data"]?.jsonPrimitive?.content
            Response(Bytes(encryptedResponseCommand), null)
        }
    }
}