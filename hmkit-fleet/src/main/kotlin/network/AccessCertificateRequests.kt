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
import kotlinx.serialization.json.*
import com.highmobility.hmkitfleet.model.AccessToken
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.slf4j.Logger
import utils.await
import java.net.HttpURLConnection

internal class AccessCertificateRequests(
    client: OkHttpClient,
    logger: Logger,
    baseUrl: String,
    private val privateKey: PrivateKey,
    private val certificate: ClientCertificate,
    private val crypto: Crypto
) : Requests(
    client,
    logger, baseUrl
) {
    val apiDeviceCertKey = "device_access_certificate"

    suspend fun getAccessCertificate(
        accessToken: AccessToken,
    ): Response<AccessCertificate> {
        val request = Request.Builder()
            .url("${baseUrl}/access_certificates")
            .header("Content-Type", "application/json")
            .post(requestBody(accessToken, certificate, privateKey, crypto))
            .build()

        printRequest(request)

        val call = client.newCall(request)
        val response = call.await()

        return tryParseResponse(response, HttpURLConnection.HTTP_CREATED) { body ->
            val jsonResponse = jsonIg.parseToJsonElement(body) as JsonObject
            val certBytes = jsonResponse.jsonObject[apiDeviceCertKey]?.jsonPrimitive?.content
            val cert = AccessCertificate(certBytes)
            Response(cert, null)
        }
    }

    private fun requestBody(
        accessToken: AccessToken,
        certificate: ClientCertificate,
        privateKey: PrivateKey,
        crypto: Crypto
    ): RequestBody {
        val accessTokenBytes = Bytes(accessToken.accessToken.encodeToByteArray())
        val signature = crypto.sign(accessTokenBytes, privateKey).base64

        val completeBody = buildJsonObject {
            put("serial_number", certificate.serial.hex)
            put("access_token", accessToken.accessToken)
            put("signature", signature)
        }

        return completeBody.toString().toRequestBody(mediaType)
    }
}