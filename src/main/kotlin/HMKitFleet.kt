/*
 * The MIT License
 *
 * Copyright (c) 2014- High-Mobility GmbH (https://high-mobility.com)
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
import com.highmobility.value.Bytes
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.future.future
import model.*
import network.*
import org.koin.core.component.get
import org.koin.core.component.inject
import org.slf4j.Logger
import java.util.concurrent.CompletableFuture

/**
 * HMKitFleet is the access point for the Fleet SDK functionality. It is accessed by
 * HMKitFleet.INSTANCE and it's field [configuration] should be set before accessing other functions
 */
object HMKitFleet {
    init {
        Koin.start()
    }

    // use a separate class so public API doesn't need implement KoinComponent
    private val koin = KoinFactory()

    private val logger by koin.inject<Logger>()

    /**
     * The SDK environment. Default is Production.
     */
    var environment: Environment = Environment.PRODUCTION

    internal var authToken: AuthToken? = null

    /**
     * Set the Service Account Configuration before calling other methods.
     */
    lateinit var configuration: ServiceAccountApiConfiguration

    /**
     * Start the data access clearance process for a vehicle.
     *
     * @param vin The vehicle VIN number
     * @param brand The vehicle brand
     * @param controlMeasures Optional control measures for some vehicle brands.
     * @return The clearance status
     */
    @JvmOverloads
    fun requestClearance(
        vin: String,
        brand: Brand,
        controlMeasures: List<ControlMeasure>? = null
    ): CompletableFuture<Response<ClearanceStatus>> = GlobalScope.future {
        logger.debug("HMKitFleet: requestClearance: $vin")
        koin.get<ClearanceRequests>().requestClearance(vin, brand, controlMeasures)
    }

    /**
     * Get the status of VINs that have previously been registered for data access clearance with
     * [requestClearance]. After VIN is Approved, [getVehicleAccess] and subsequent [sendCommand]
     * can be sent.
     *
     * @return The clearance status
     */
    fun getClearanceStatuses(): CompletableFuture<Response<List<ClearanceStatus>>> =
        GlobalScope.future {
            logger.debug("HMKitFleet: getClearanceStatuses:")
            koin.get<ClearanceRequests>().getClearanceStatuses()
        }

    /**
     * Get Vehicle Access object. This can be queried for vehicles with [getClearanceStatuses]
     * Approved. The returned object can be used with [sendCommand] or [revokeClearance].
     *
     * The user should securely store this object for later use.
     *
     * @param vin The vehicle VIN number
     * @return The vehicle access object
     */
    fun getVehicleAccess(vin: String):
            CompletableFuture<Response<VehicleAccess>> = GlobalScope.future {
        val accessToken = koin.get<AccessTokenRequests>().getAccessToken(vin)

        if (accessToken.response != null) {
            val accessCertificate = koin.get<AccessCertificateRequests>().getAccessCertificate(
                accessToken.response,
            )

            if (accessCertificate.response != null) {
                val vehicleAccess =
                    VehicleAccess(
                        vin,
                        accessToken.response,
                        accessCertificate.response
                    )
                Response(vehicleAccess, null)
            } else {
                Response(null, accessCertificate.error)
            }
        } else {
            Response(null, accessToken.error)
        }
    }

    /**
     * Send a telematics command to the vehicle.
     *
     * @param vehicleAccess The vehicle access object returned in [getVehicleAccess]
     * @param command The command that is sent to the vehicle.
     * @return The response command from the vehicle.
     */
    fun sendCommand(
        command: Bytes,
        vehicleAccess: VehicleAccess
    ): CompletableFuture<Response<Bytes>> = GlobalScope.future {
        koin.get<TelematicsRequests>().sendCommand(command, vehicleAccess.accessCertificate)
    }

    /**
     * Revoke the vehicle clearance. After this, the [VehicleAccess] object is invalid.
     *
     * @param vehicleAccess The vehicle access object returned in [getVehicleAccess]
     * @return Whether clearance was successful
     */
    fun revokeClearance(vehicleAccess: VehicleAccess): CompletableFuture<Response<Boolean>> =
        GlobalScope.future {
            koin.get<AccessTokenRequests>().deleteAccessToken(vehicleAccess.accessToken)
        }

    /**
     * The Fleet SDK environment.
     */
    enum class Environment {
        PRODUCTION, SANDBOX;

        internal val url: String
            get() {
                return webUrl
                    ?: when (this) {
                        PRODUCTION -> prodUrl
                        SANDBOX -> "https://sandbox.api.high-mobility.com/v1"
                    }
            }

        companion object {
            private const val prodUrl = "https://api.high-mobility.com/v1"

            /**
             * Override the web url, which is normally derived from the [HMKitFleet.environment]
             * value
             */
            @JvmField
            var webUrl: String? = null
            internal const val jwtUrl = prodUrl
        }
    }

    private class KoinFactory : Koin.FleetSdkKoinComponent
}