//[hmkit-fleet](../../../index.md)/[com.highmobility.hmkitfleet](../index.md)/[HMKitFleet](index.md)/[requestClearance](request-clearance.md)

# requestClearance

[jvm]\

@[JvmOverloads](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.jvm/-jvm-overloads/index.html)

fun [requestClearance](request-clearance.md)(vin: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), brand: [Brand](../../com.highmobility.hmkitfleet.model/-brand/index.md), controlMeasures: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[ControlMeasure](../../com.highmobility.hmkitfleet.model/-control-measure/index.md)&gt;? = null): [CompletableFuture](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/CompletableFuture.html)&lt;[Response](../../com.highmobility.hmkitfleet.network/-response/index.md)&lt;[RequestClearanceResponse](../../com.highmobility.hmkitfleet.model/-request-clearance-response/index.md)&gt;&gt;

Start the data access clearance process for a vehicle.

#### Return

The clearance status

#### Parameters

jvm

| | |
|---|---|
| vin | The vehicle VIN number |
| brand | The vehicle brand |
| controlMeasures | Optional control measures for some vehicle brands. |
