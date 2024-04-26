//[hmkit-fleet](../../../index.md)/[com.highmobility.hmkitfleet](../index.md)/[HMKitFleet](index.md)/[getClearanceStatuses](get-clearance-statuses.md)

# getClearanceStatuses

[jvm]\
fun [getClearanceStatuses](get-clearance-statuses.md)(): [CompletableFuture](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/CompletableFuture.html)&lt;[Response](../../com.highmobility.hmkitfleet.network/-response/index.md)&lt;[List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[ClearanceStatus](../../com.highmobility.hmkitfleet.model/-clearance-status/index.md)&gt;&gt;&gt;

Get the status of VINs that have previously been registered for data access clearance with [requestClearance](request-clearance.md). After VIN is Approved, getVehicleAccess and subsequent sendCommand can be sent.

#### Return

The clearance statuses
