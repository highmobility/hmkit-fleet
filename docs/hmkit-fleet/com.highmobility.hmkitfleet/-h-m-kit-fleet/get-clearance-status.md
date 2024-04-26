//[hmkit-fleet](../../../index.md)/[com.highmobility.hmkitfleet](../index.md)/[HMKitFleet](index.md)/[getClearanceStatus](get-clearance-status.md)

# getClearanceStatus

[jvm]\
fun [getClearanceStatus](get-clearance-status.md)(vin: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [CompletableFuture](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/CompletableFuture.html)&lt;[Response](../../com.highmobility.hmkitfleet.network/-response/index.md)&lt;[ClearanceStatus](../../com.highmobility.hmkitfleet.model/-clearance-status/index.md)&gt;&gt;

Get the status of a [vin](get-clearance-status.md) that has previously been registered for data access clearance with [requestClearance](request-clearance.md). After the [vin](get-clearance-status.md) is Approved, getVehicleAccess and subsequent sendCommand can be sent.

#### Return

The clearance status
