//[hmkit-fleet](../../../index.md)/[[root]](../index.md)/[HMKitFleet](index.md)/[revokeClearance](revoke-clearance.md)



# revokeClearance  
[jvm]  
Content  
fun [revokeClearance](revoke-clearance.md)(vehicleAccess: [VehicleAccess](../../model/-vehicle-access/index.md)): [CompletableFuture](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/CompletableFuture.html)<[Response](../../network/-response/index.md)<[Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)>>  
More info  


Revoke the vehicle clearance. After this, the [VehicleAccess](../../model/-vehicle-access/index.md) object is invalid.



#### Return  


Whether clearance was successful



## Parameters  
  
jvm  
  
| | |
|---|---|
| <a name="/HMKitFleet/revokeClearance/#model.VehicleAccess/PointingToDeclaration/"></a>vehicleAccess| <a name="/HMKitFleet/revokeClearance/#model.VehicleAccess/PointingToDeclaration/"></a><br><br>The vehicle access object<br><br>|
  
  



