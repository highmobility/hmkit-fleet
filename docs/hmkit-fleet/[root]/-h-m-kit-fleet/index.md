//[hmkit-fleet](../../../index.md)/[[root]](../index.md)/[HMKitFleet](index.md)



# HMKitFleet  
 [jvm] object [HMKitFleet](index.md)

HMKitFleet is the access point for the Fleet SDK functionality. It is accessed by HMKitFleet.INSTANCE and it's field [configuration](configuration.md) should be set before accessing other functions

   


## Types  
  
|  Name |  Summary | 
|---|---|
| <a name="/HMKitFleet.Environment///PointingToDeclaration/"></a>[Environment](-environment/index.md)| <a name="/HMKitFleet.Environment///PointingToDeclaration/"></a>[jvm]  <br>Content  <br>enum [Environment](-environment/index.md) : [Enum](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-enum/index.html)<[HMKitFleet.Environment](-environment/index.md)>   <br><br><br>|


## Functions  
  
|  Name |  Summary | 
|---|---|
| <a name="/HMKitFleet/getClearanceStatuses/#/PointingToDeclaration/"></a>[getClearanceStatuses](get-clearance-statuses.md)| <a name="/HMKitFleet/getClearanceStatuses/#/PointingToDeclaration/"></a>[jvm]  <br>Content  <br>fun [getClearanceStatuses](get-clearance-statuses.md)(): [CompletableFuture](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/CompletableFuture.html)<[Response](../../network/-response/index.md)<[List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)<[ClearanceStatus](../../model/-clearance-status/index.md)>>>  <br>More info  <br>Get the status of VINs that have previously been registered for data access clearance with [requestClearance](request-clearance.md).  <br><br><br>|
| <a name="/HMKitFleet/getVehicleAccess/#kotlin.String/PointingToDeclaration/"></a>[getVehicleAccess](get-vehicle-access.md)| <a name="/HMKitFleet/getVehicleAccess/#kotlin.String/PointingToDeclaration/"></a>[jvm]  <br>Content  <br>fun [getVehicleAccess](get-vehicle-access.md)(vin: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [CompletableFuture](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/CompletableFuture.html)<[Response](../../network/-response/index.md)<[VehicleAccess](../../model/-vehicle-access/index.md)>>  <br>More info  <br>Get Vehicle Access object.  <br><br><br>|
| <a name="/HMKitFleet/requestClearance/#kotlin.String#model.Brand#kotlin.collections.List[model.ControlMeasure]?/PointingToDeclaration/"></a>[requestClearance](request-clearance.md)| <a name="/HMKitFleet/requestClearance/#kotlin.String#model.Brand#kotlin.collections.List[model.ControlMeasure]?/PointingToDeclaration/"></a>[jvm]  <br>Content  <br>@[JvmOverloads](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.jvm/-jvm-overloads/index.html)()  <br>  <br>fun [requestClearance](request-clearance.md)(vin: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), brand: [Brand](../../model/-brand/index.md), controlMeasures: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)<[ControlMeasure](../../model/-control-measure/index.md)>? = null): [CompletableFuture](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/CompletableFuture.html)<[Response](../../network/-response/index.md)<[ClearanceStatus](../../model/-clearance-status/index.md)>>  <br>More info  <br>Start the data access clearance process for a vehicle.  <br><br><br>|
| <a name="/HMKitFleet/revokeClearance/#model.VehicleAccess/PointingToDeclaration/"></a>[revokeClearance](revoke-clearance.md)| <a name="/HMKitFleet/revokeClearance/#model.VehicleAccess/PointingToDeclaration/"></a>[jvm]  <br>Content  <br>fun [revokeClearance](revoke-clearance.md)(vehicleAccess: [VehicleAccess](../../model/-vehicle-access/index.md)): [CompletableFuture](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/CompletableFuture.html)<[Response](../../network/-response/index.md)<[Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)>>  <br>More info  <br>Revoke the vehicle clearance.  <br><br><br>|
| <a name="/HMKitFleet/sendCommand/#com.highmobility.value.Bytes#model.VehicleAccess/PointingToDeclaration/"></a>[sendCommand](send-command.md)| <a name="/HMKitFleet/sendCommand/#com.highmobility.value.Bytes#model.VehicleAccess/PointingToDeclaration/"></a>[jvm]  <br>Content  <br>fun [sendCommand](send-command.md)(command: , vehicleAccess: [VehicleAccess](../../model/-vehicle-access/index.md)): [CompletableFuture](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/CompletableFuture.html)<[Response](../../network/-response/index.md)<>>  <br>More info  <br>Send a telematics command to the vehicle.  <br><br><br>|


## Properties  
  
|  Name |  Summary | 
|---|---|
| <a name="/HMKitFleet/configuration/#/PointingToDeclaration/"></a>[configuration](configuration.md)| <a name="/HMKitFleet/configuration/#/PointingToDeclaration/"></a> [jvm] lateinit var [configuration](configuration.md): [ServiceAccountApiConfiguration](../-service-account-api-configuration/index.md)Set the Service Account Configuration before calling other methods.   <br>|
| <a name="/HMKitFleet/environment/#/PointingToDeclaration/"></a>[environment](environment.md)| <a name="/HMKitFleet/environment/#/PointingToDeclaration/"></a> [jvm] var [environment](environment.md): [HMKitFleet.Environment](-environment/index.md)   <br>|

