//[hmkit-fleet](../../../index.md)/[[root]](../index.md)/[HMKitFleet](index.md)/[getClearanceStatuses](get-clearance-statuses.md)



# getClearanceStatuses  
[jvm]  
Content  
fun [getClearanceStatuses](get-clearance-statuses.md)(): [CompletableFuture](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/CompletableFuture.html)<[Response](../../network/-response/index.md)<[List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)<[ClearanceStatus](../../model/-clearance-status/index.md)>>>  
More info  


Get the status of VINs that have previously been registered for data access clearance with [requestClearance](request-clearance.md). After VIN is Approved, [getVehicleAccess](get-vehicle-access.md) and subsequent [sendCommand](send-command.md) can be sent.



#### Return  


The clearance status

  



