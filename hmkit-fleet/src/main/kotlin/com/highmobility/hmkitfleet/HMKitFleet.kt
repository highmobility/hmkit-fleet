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
package com.highmobility.hmkitfleet

import com.highmobility.hmkitfleet.model.Brand
import com.highmobility.hmkitfleet.model.ClearanceStatus
import com.highmobility.hmkitfleet.model.ControlMeasure
import com.highmobility.hmkitfleet.model.EligibilityStatus
import com.highmobility.hmkitfleet.model.RequestClearanceResponse
import com.highmobility.hmkitfleet.network.ClearanceRequests
import com.highmobility.hmkitfleet.network.Response
import com.highmobility.hmkitfleet.network.UtilityRequests
import com.highmobility.hmkitfleet.network.VehicleDataRequests
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.future.future
import org.slf4j.Logger
import java.util.concurrent.CompletableFuture

/**
 * HMKitFleet is the access point for the Fleet SDK functionality. It is accessed by
 * creating a new HMKitFleet object with a HMKitConfiguration object.
 *
 * ```
 * HMKitFleet fleet = new HMKitFleet(
 *     new HMKitConfiguration.Builder()
 *      .credentials(new HMKitOAuthCredentials("client_id", "client_secret"))
 *      .build()
 * );
 * ```
 *
 * [javadoc](https://highmobility.github.io/hmkit-fleet/v2/javadoc/com/highmobility/hmkitfleet/HMKitFleet.html)
 *
 */
class HMKitFleet constructor(
  /**
   * Configure the HMKit. Use the [HMKitConfiguration.Builder] to create the object.
   */
  hmKitConfiguration: HMKitConfiguration
) {
  private val koin = Koin(hmKitConfiguration).start()
  private val scope = koin.get<CoroutineScope>()
  private val logger by koin.inject<Logger>()

  /**
   * Get the eligibility status for a specific VIN. This can be used to find out if the vehicle has the necessary
   * connectivity to transmit data.
   *
   * @param vin The vehicle VIN number
   * @param brand The vehicle brand
   * @return The eligibility status
   */
  fun getEligibility(
    vin: String,
    brand: Brand
  ): CompletableFuture<Response<EligibilityStatus>> = scope.future {
    logger.debug("HMKitFleet: getEligibility: $vin")
    koin.get<UtilityRequests>().getEligibility(vin, brand)
  }

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
  ): CompletableFuture<Response<RequestClearanceResponse>> = scope.future {
    logger.debug("HMKitFleet: requestClearance: $vin")
    koin.get<ClearanceRequests>().requestClearance(vin, brand, controlMeasures)
  }

  /**
   * Get the status of VINs that have previously been registered for data access clearance with
   * [requestClearance]. After VIN is Approved, [getVehicleAccess] and subsequent [sendCommand]
   * can be sent.
   *
   * @return The clearance statuses
   */
  fun getClearanceStatuses(): CompletableFuture<Response<List<ClearanceStatus>>> = scope.future {
    logger.debug("HMKitFleet: getClearanceStatuses:")
    koin.get<ClearanceRequests>().getClearanceStatuses()
  }

  /**
   * Get the status of a [vin] that has previously been registered for data access clearance with
   * [requestClearance]. After the [vin] is Approved, [getVehicleAccess] and subsequent [sendCommand]
   * can be sent.
   *
   * @return The clearance status
   */
  fun getClearanceStatus(vin: String): CompletableFuture<Response<ClearanceStatus>> = scope.future {
    logger.debug("HMKitFleet: getClearanceStatus($vin):")
    koin.get<ClearanceRequests>().getClearanceStatus(vin)
  }

  /**
   * Delete the clearance for the given VIN.
   *
   * If the clearance is in a pending state, the activation process is canceled.
   * If the vehicle is in an approved state, a revoke is attempted. If the revoke is successful,
   * the [VehicleAccess] object for this VIN becomes invalid.
   *
   * @param vin The vehicle VIN number
   * @return The clearance status
   */
  fun deleteClearance(vin: String) = scope.future {
    logger.debug("HMKitFleet: delete clearance $vin:")
    koin.get<ClearanceRequests>().deleteClearance(vin)
  }

  fun getVehicleState(
    vin: String
  ): CompletableFuture<Response<String>> = scope.future {
    koin.get<VehicleDataRequests>().getVehicleStatus(vin)
  }

  /**
   * The Fleet SDK environment.
   *
   * [javadoc](https://highmobility.github.io/hmkit-fleet/v2/javadoc/com/highmobility/hmkitfleet/HMKitFleet.Environment.html)
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
    }
  }
}
