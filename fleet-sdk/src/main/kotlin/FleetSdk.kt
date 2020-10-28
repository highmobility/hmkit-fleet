import com.highmobility.hmkit.HMKit
import network.WebService
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.slf4j.Logger

class FleetSdk private constructor(apiKey: String) : KoinComponent {
    private val logger by inject<Logger>()
    private val hmkit by inject<HMKit>()
    private val webService by inject<WebService>()

    init {
        DI.start(apiKey)
    }

    /**
     * Initialise the Fleet SDK with API key.
     *
     * @param apiKey The API key
     */
    fun init() {
        /*
        Open question: should the SDK be initiated with the production app client certificate as well?
        Or should the production app ID be sent in and the SDK downloads a device/client certificate
        automatically?
         */
        logger.info("fleet sdk initialised")
    }

    fun clearVehicles(vins: List<String>, result: FleetSdkResult) {
        // fleets/vehicles endpoint
        // post /v1/fleets/vehicles
        // auth header: token

        // flow:
        /*
        1) I have the api key, now have to use JWT for requests.
         */

        /*
        Once a vehicle VIN has been cleared, it’s possible to get an access token for that vehicle.
        With the access token it will be possible to use our REST API (or any SDK) to get data.
        */

        /*
        By using the POST /fleets/access_tokens endpoint, the data customer passes in the VIN and
         receives an access token (and a refresh token) The access token is used with the REST API
         to fetch data
         */

        /*
        VIN clearing can take a long time. For BMW we need to send email.
         */
    }

    // TODO: endpoints
    //  The SDK implements all endpoints of the Service Account API except for the device certificate
    //  endpoints, which are not needed

    fun deleteVehicleClearance(vin: String, result: FleetSdkResult) {
        /*
        Although the OAuth2 API is not used to get an access token, it is used to refresh the access
        token and to revoke access. Revoking access means that the VIN is removed from the clearance list.

        By using the POST /access_tokens endpoint, the data customer passes in the refresh token and
         receives a new access token

        By using the DELETE /access_tokens endpoint, the data customer passes in the access token
        and the vehicle is deleted from the clearance list
         */
    }

    interface FleetSdkResult {
        fun onVehiclesCleared()
        fun onFailure()
    }

    companion object {
        @Volatile
        private var INSTANCE: FleetSdk? = null

        @JvmStatic
        fun getInstance(apiKey: String): FleetSdk =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildFleetSdk(apiKey).also { INSTANCE = it }
            }

        private fun buildFleetSdk(apiKey: String) = FleetSdk(apiKey)
    }
}