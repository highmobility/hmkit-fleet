//[hmkit-fleet](../../../index.md)/[com.highmobility.hmkitfleet](../index.md)/[HMKitFleet](index.md)

# HMKitFleet

[jvm]\
class [HMKitFleet](index.md)(hmKitConfiguration: [HMKitConfiguration](../-h-m-kit-configuration/index.md))

HMKitFleet is the access point for the Fleet SDK functionality. It is accessed by creating a new HMKitFleet object with a HMKitConfiguration object.

```kotlin
HMKitFleet fleet = new HMKitFleet(
    new HMKitConfiguration.Builder()
     .credentials(new HMKitOAuthCredentials("client_id", "client_secret"))
     .build()
);
```

[Check out the Javadoc](https://highmobility.github.io/hmkit-fleet/v2/javadoc/com/highmobility/hmkitfleet/HMKitFleet.html)

## Constructors

| | |
|---|---|
| [HMKitFleet](-h-m-kit-fleet.md) | [jvm]<br>constructor(hmKitConfiguration: [HMKitConfiguration](../-h-m-kit-configuration/index.md)) |

## Types

| Name | Summary |
|---|---|
| [Environment](-environment/index.md) | [jvm]<br>enum [Environment](-environment/index.md) : [Enum](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-enum/index.html)&lt;[HMKitFleet.Environment](-environment/index.md)&gt; <br>The Fleet SDK environment. |

## Functions

| Name | Summary |
|---|---|
| [deleteClearance](delete-clearance.md) | [jvm]<br>fun [deleteClearance](delete-clearance.md)(vin: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)): [CompletableFuture](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/CompletableFuture.html)&lt;[Response](../../com.highmobility.hmkitfleet.network/-response/index.md)&lt;[RequestClearanceResponse](../../com.highmobility.hmkitfleet.model/-request-clearance-response/index.md)&gt;&gt;<br>Delete the clearance for the given VIN. |
| [getClearanceStatus](get-clearance-status.md) | [jvm]<br>fun [getClearanceStatus](get-clearance-status.md)(vin: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)): [CompletableFuture](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/CompletableFuture.html)&lt;[Response](../../com.highmobility.hmkitfleet.network/-response/index.md)&lt;[ClearanceStatus](../../com.highmobility.hmkitfleet.model/-clearance-status/index.md)&gt;&gt;<br>Get the status of a [vin](get-clearance-status.md) that has previously been registered for data access clearance with [requestClearance](request-clearance.md). After the [vin](get-clearance-status.md) is Approved, getVehicleAccess and subsequent sendCommand can be sent. |
| [getClearanceStatuses](get-clearance-statuses.md) | [jvm]<br>fun [getClearanceStatuses](get-clearance-statuses.md)(): [CompletableFuture](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/CompletableFuture.html)&lt;[Response](../../com.highmobility.hmkitfleet.network/-response/index.md)&lt;[List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[ClearanceStatus](../../com.highmobility.hmkitfleet.model/-clearance-status/index.md)&gt;&gt;&gt;<br>Get the status of VINs that have previously been registered for data access clearance with [requestClearance](request-clearance.md). After VIN is Approved, getVehicleAccess and subsequent sendCommand can be sent. |
| [getEligibility](get-eligibility.md) | [jvm]<br>fun [getEligibility](get-eligibility.md)(vin: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), brand: [Brand](../../com.highmobility.hmkitfleet.model/-brand/index.md)): [CompletableFuture](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/CompletableFuture.html)&lt;[Response](../../com.highmobility.hmkitfleet.network/-response/index.md)&lt;[EligibilityStatus](../../com.highmobility.hmkitfleet.model/-eligibility-status/index.md)&gt;&gt;<br>Get the eligibility status for a specific VIN. This can be used to find out if the vehicle has the necessary connectivity to transmit data. |
| [getVehicleState](get-vehicle-state.md) | [jvm]<br>fun [getVehicleState](get-vehicle-state.md)(vin: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)): [CompletableFuture](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/CompletableFuture.html)&lt;[Response](../../com.highmobility.hmkitfleet.network/-response/index.md)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)&gt;&gt;<br>Get vehicle status JSON string from the [Vehicle Data API](https://docs.high-mobility.com/api-references/code-references/vehicle-data/reference/v1/) |
| [getVehicleStaticData](get-vehicle-static-data.md) | [jvm]<br>fun [getVehicleStaticData](get-vehicle-static-data.md)(vin: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)): [CompletableFuture](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/CompletableFuture.html)&lt;[Response](../../com.highmobility.hmkitfleet.network/-response/index.md)&lt;[String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)&gt;&gt;<br>Get the static information about a vehicle. Static data can include the vehicle's brand, equipment and more. |
| [requestClearance](request-clearance.md) | [jvm]<br>@[JvmOverloads](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.jvm/-jvm-overloads/index.html)<br>fun [requestClearance](request-clearance.md)(vin: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), brand: [Brand](../../com.highmobility.hmkitfleet.model/-brand/index.md), controlMeasures: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.collections/-list/index.html)&lt;[ControlMeasure](../../com.highmobility.hmkitfleet.model/-control-measure/index.md)&gt;? = null): [CompletableFuture](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/CompletableFuture.html)&lt;[Response](../../com.highmobility.hmkitfleet.network/-response/index.md)&lt;[RequestClearanceResponse](../../com.highmobility.hmkitfleet.model/-request-clearance-response/index.md)&gt;&gt;<br>Start the data access clearance process for a vehicle. |
