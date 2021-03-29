//[hmkit-fleet](../../../index.md)/[[root]](../index.md)/[HMKitFleet](index.md)/[getVehicleAccess](get-vehicle-access.md)



# getVehicleAccess  
[jvm]  
Content  
fun [getVehicleAccess](get-vehicle-access.md)(vin: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [CompletableFuture](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/CompletableFuture.html)<[Response](../../network/-response/index.md)<[VehicleAccess](../../model/-vehicle-access/index.md)>>  
More info  


Get Vehicle Access object. This can be queried for vehicles with [getClearanceStatuses](get-clearance-statuses.md) Approved. The returned object can be used with [sendCommand](send-command.md) or [revokeClearance](revoke-clearance.md).



The user should securely store this object for later use.



#### Return  


The vehicle access object



## Parameters  
  
jvm  
  
| | |
|---|---|
| <a name="/HMKitFleet/getVehicleAccess/#kotlin.String/PointingToDeclaration/"></a>vin| <a name="/HMKitFleet/getVehicleAccess/#kotlin.String/PointingToDeclaration/"></a><br><br>The vehicle VIN number<br><br>|
  
  



