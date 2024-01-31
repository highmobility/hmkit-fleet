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

import com.highmobility.crypto.Crypto
import com.highmobility.hmkitfleet.ServiceAccountApiConfiguration
import com.highmobility.hmkitfleet.HMKitFleet

import com.highmobility.utils.Base64
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import com.highmobility.hmkitfleet.model.AuthToken
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import org.slf4j.Logger
import utils.await
import java.net.HttpURLConnection

internal class AuthTokenRequests(
    client: OkHttpClient,
    private val crypto: Crypto,
    logger: Logger,
    baseUrl: String,
    private val configuration: ServiceAccountApiConfiguration,
    private val cache: Cache
) : Requests(
    client,
    logger,
    baseUrl
) {
    suspend fun getAuthToken(): Response<AuthToken> {
        val cachedToken = cache.authToken
        if (cachedToken != null) return Response(cachedToken)

        val body = FormBody.Builder()
            .add("assertion", getJwt(configuration))
            .build()

        val request = Request.Builder()
            .url("${baseUrl}/auth_tokens")
            .header("Content-Type", "application/json")
            .post(body)
            .build()
        printRequest(request)
        val call = client.newCall(request)

        val response = call.await()
        val responseBody = printResponse(response)

        return try {
            if (response.code == HttpURLConnection.HTTP_CREATED) {
                cache.authToken = jsonIg.decodeFromString(responseBody)
                Response(cache.authToken)
            } else {
                parseError(responseBody)
            }
        } catch (e: java.lang.Exception) {
            val detail = e.message.toString()
            Response(null, genericError(detail))
        }
    }

    private fun getJwt(configuration: ServiceAccountApiConfiguration): String {
        val header = buildJsonObject {
            put("alg", "ES256")
            put("typ", "JWT")
        }.toString()

        val jwtBody = buildJsonObject {
            put("ver", configuration.version)
            put("iss", configuration.serviceAccountApiKey)
            put("aud", baseUrl)
            put("jti", configuration.createJti())
            put("iat", configuration.createIat())
        }.toString()

        val headerBase64 = Base64.encodeUrlSafe(header.toByteArray())
        val bodyBase64 = Base64.encodeUrlSafe(jwtBody.toByteArray())
        val jwtContent = String.format("%s.%s", headerBase64, bodyBase64)
        val privateKey = configuration.getServiceAccountHmPrivateKey()
        val jwtSignature = crypto.signJWT(jwtContent.toByteArray(), privateKey)

        return String.format("%s.%s", jwtContent, jwtSignature.base64UrlSafe)
    }
}