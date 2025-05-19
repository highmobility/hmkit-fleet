//[hmkit-fleet](../../../index.md)/[com.highmobility.hmkitfleet](../index.md)/[HMKitFleet](index.md)/[deleteClearance](delete-clearance.md)

# deleteClearance

[jvm]\
fun [deleteClearance](delete-clearance.md)(vin: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)): [CompletableFuture](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/CompletableFuture.html)&lt;[Response](../../com.highmobility.hmkitfleet.network/-response/index.md)&lt;[RequestClearanceResponse](../../com.highmobility.hmkitfleet.model/-request-clearance-response/index.md)&gt;&gt;

Delete the clearance for the given VIN.

If the clearance is in a pending state, the activation process is canceled. If the vehicle is in an approved state, a revoke is attempted. If the revoke is successful, the VehicleAccess object for this VIN becomes invalid.

#### Return

The clearance status

#### Parameters

jvm

| | |
|---|---|
| vin | The vehicle VIN number |
