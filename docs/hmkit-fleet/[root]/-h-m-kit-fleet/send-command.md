//[hmkit-fleet](../../../index.md)/[[root]](../index.md)/[HMKitFleet](index.md)/[sendCommand](send-command.md)



# sendCommand  
[jvm]  
Content  
fun [sendCommand](send-command.md)(command: , vehicleAccess: [VehicleAccess](../../model/-vehicle-access/index.md)): [CompletableFuture](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/CompletableFuture.html)<[Response](../../network/-response/index.md)<>>  
More info  


Send a telematics command to the vehicle.



#### Return  


The response command from the vehicle.



## Parameters  
  
jvm  
  
| | |
|---|---|
| <a name="/HMKitFleet/sendCommand/#com.highmobility.value.Bytes#model.VehicleAccess/PointingToDeclaration/"></a>vehicleAccess| <a name="/HMKitFleet/sendCommand/#com.highmobility.value.Bytes#model.VehicleAccess/PointingToDeclaration/"></a><br><br>The vehicle access object returned in [getVehicleAccess](get-vehicle-access.md)<br><br>|
| <a name="/HMKitFleet/sendCommand/#com.highmobility.value.Bytes#model.VehicleAccess/PointingToDeclaration/"></a>command| <a name="/HMKitFleet/sendCommand/#com.highmobility.value.Bytes#model.VehicleAccess/PointingToDeclaration/"></a><br><br>The command that is sent to the vehicle.<br><br>|
  
  



