package network

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.*
import model.AuthToken
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okio.Buffer
import org.slf4j.Logger

internal open class Requests(
    val client: OkHttpClient,
    val logger: Logger,
    val baseUrl: String
) {
    val mediaType = "application/json; charset=utf-8".toMediaType()
    var authToken: AuthToken? = null

    inline fun <T> tryParseResponse(
        response: Response,
        expectedResponseCode: Int,
        block: (body: String) -> (network.Response<T>)
    ): network.Response<T> {
        val responseBody = printResponse(response)

        return try {
            if (response.code == expectedResponseCode) {
                block(responseBody)
            } else {
                parseError(responseBody)
            }
        } catch (e: Exception) {
            val detail = e.message.toString()
            Response(null, genericError(detail))
        }
    }

    fun printRequest(request: Request) {
        val format = Json { prettyPrint = true }

        // parse into json, so can log it out with pretty print
        val body = request.bodyAsString()
        var bodyInPrettyPrint = ""
        if (body != null) {
            val jsonElement = format.decodeFromString<JsonElement>(body)
            bodyInPrettyPrint = format.encodeToString(jsonElement)
        }

        logger.debug(
            "sending ${request.url}:" +
                    "\nheaders: ${request.headers}" +
                    "body: $bodyInPrettyPrint"
        )
    }

    fun printResponse(response: Response): String {
        val body = response.body?.string()
        logger.debug("${response.request.url} response:\n${response.code}: ${body}")
        return body!!
    }

    fun <T> parseError(responseBody: String): network.Response<T> {
        val json = Json.parseToJsonElement(responseBody)
        if (json is JsonObject) {
            val errors = json["errors"] as JsonArray
            if (errors.size > 0) {
                val error =
                    Json.decodeFromJsonElement<network.response.Error>(errors.first())
                return Response(null, error)
            }
        }

        return Response(null, genericError("invalid error structure"))
    }
}

internal fun Request.bodyAsString(): String? {
    if (this.body == null) return null
    val buffer = Buffer()
    this.body?.writeTo(buffer)
    return buffer.readUtf8()
}

internal fun genericError(detail: String? = null): network.response.Error {
    val genericError = network.response.Error("Invalid server response", detail)
    return genericError
}