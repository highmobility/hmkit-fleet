//[hmkit-fleet](../../../index.md)/[com.highmobility.hmkitfleet](../index.md)/[HMKitFleet](index.md)/[getEligibility](get-eligibility.md)

# getEligibility

[jvm]\
fun [getEligibility](get-eligibility.md)(vin: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), brand: [Brand](../../com.highmobility.hmkitfleet.model/-brand/index.md)): [CompletableFuture](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/CompletableFuture.html)&lt;[Response](../../com.highmobility.hmkitfleet.network/-response/index.md)&lt;[EligibilityStatus](../../com.highmobility.hmkitfleet.model/-eligibility-status/index.md)&gt;&gt;

Get the eligibility status for a specific VIN. This can be used to find out if the vehicle has the necessary connectivity to transmit data.

#### Return

The eligibility status

#### Parameters

jvm

| | |
|---|---|
| vin | The vehicle VIN number |
| brand | The vehicle brand |
