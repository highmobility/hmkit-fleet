package network

import ServiceAccountApiConfiguration
import com.highmobility.hmkit.HMKit
import com.highmobility.utils.Base64
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.*
import model.AuthToken
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import org.slf4j.Logger
import ru.gildor.coroutines.okhttp.await
import java.lang.Exception
import java.net.HttpURLConnection

internal class AuthTokenRequests(
    client: OkHttpClient,
    val hmkitOem: HMKit,
    logger: Logger,
    baseUrl: String
) : Requests(
    client,
    logger, baseUrl
) {
    suspend fun getAuthToken(configuration: ServiceAccountApiConfiguration): network.Response<AuthToken> {
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
                val token = Json.decodeFromString<AuthToken>(responseBody)
                Response(token)
            } else {
                parseError(responseBody)
            }
        } catch (e: Exception) {
            val detail = e.message.toString()
            Response(null, genericError(detail))
        }
    }

    fun getJwt(configuration: ServiceAccountApiConfiguration): String {
        val header = buildJsonObject {
            put("alg", "ES256")
            put("typ", "JWT")
        }.toString()

        val jwtBody = buildJsonObject {
            put("ver", configuration.version)
            put("iss", configuration.apiKey)
            put("aud", HMKitFleet.Environment.PRODUCTION.url)
            put("jti", configuration.createJti())
            put("iat", configuration.createIat())
        }.toString()

        val headerBase64 = Base64.encodeUrlSafe(header.toByteArray())
        val bodyBase64 = Base64.encodeUrlSafe(jwtBody.toByteArray())
        val jwtContent = String.format("%s.%s", headerBase64, bodyBase64)
        val privateKey = configuration.getHmPrivateKey()
        val jwtSignature = hmkitOem.crypto.signJWT(jwtContent.toByteArray(), privateKey)

        return String.format("%s.%s", jwtContent, jwtSignature.base64UrlSafe)
    }
}