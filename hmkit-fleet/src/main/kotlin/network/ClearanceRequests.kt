package network

import kotlinx.serialization.json.*
import network.response.ClearanceStatus
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.slf4j.Logger
import ru.gildor.coroutines.okhttp.await
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
        vin: String
    ): Response<ClearanceStatus> {
        val body = requestBody(vin)
        val authToken = authTokenRequests.getAuthToken()

        val request = Request.Builder()
            .url("${baseUrl}/fleets/vehicles")
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer $authToken")
            .post(body)
            .build()

        printRequest(request)

        val call = client.newCall(request)
        val response = call.await()

        return tryParseResponse(response, HttpURLConnection.HTTP_OK) { responseBody ->
            val jsonElement = Json.parseToJsonElement(responseBody) as JsonObject
            val statuses = jsonElement["vins"] as JsonArray
            for (statusElement in statuses) {
                val status =
                    Json.decodeFromJsonElement<ClearanceStatus>(statusElement)
                if (status.vin == vin) {
                    return Response(status, null)
                }
            }
            Response(null, genericError())
        }
    }

    suspend fun getClearanceStatuses(): Response<List<ClearanceStatus>> {
        // fleets/vehicles endpoint
        // GET /v1/fleets/vehicles
        // auth header: token
        val authToken = authTokenRequests.getAuthToken()

        val request = Request.Builder()
            .url("${baseUrl}/fleets/vehicles")
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer $authToken")
            .build()

        printRequest(request)

        val call = client.newCall(request)
        val response = call.await()

        return tryParseResponse(response, HttpURLConnection.HTTP_OK) { responseBody ->
            val statuses = Json.parseToJsonElement(responseBody) as JsonArray

            val builder = Array(statuses.size) {
                val statusElement = statuses[it]
                val status = Json.decodeFromJsonElement<ClearanceStatus>(statusElement)
                status
            }

            Response(builder.toList())
        }
    }

    private fun requestBody(vin: String): RequestBody {
        val vehicle = JsonObject(mapOf("vin" to JsonPrimitive(vin)))
        val arrayOfVehicles = JsonArray(listOf(vehicle))
        val completeBody = JsonObject(mapOf("vehicles" to arrayOfVehicles))

        val body = completeBody.toString().toRequestBody(mediaType)
        return body
    }
}