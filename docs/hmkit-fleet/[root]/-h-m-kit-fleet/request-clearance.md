//[hmkit-fleet](../../../index.md)/[[root]](../index.md)/[HMKitFleet](index.md)/[requestClearance](request-clearance.md)



# requestClearance  
[jvm]  
Content  
@[JvmOverloads](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.jvm/-jvm-overloads/index.html)()  
  
fun [requestClearance](request-clearance.md)(vin: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), brand: [Brand](../../model/-brand/index.md), controlMeasures: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)<[ControlMeasure](../../model/-control-measure/index.md)>? = null): [CompletableFuture](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/CompletableFuture.html)<[Response](../../network/-response/index.md)<[ClearanceStatus](../../model/-clearance-status/index.md)>>  
More info  


Start the data access clearance process for a vehicle.



#### Return  


The clearance status



## Parameters  
  
jvm  
  
| | |
|---|---|
| <a name="/HMKitFleet/requestClearance/#kotlin.String#model.Brand#kotlin.collections.List[model.ControlMeasure]?/PointingToDeclaration/"></a>vin| <a name="/HMKitFleet/requestClearance/#kotlin.String#model.Brand#kotlin.collections.List[model.ControlMeasure]?/PointingToDeclaration/"></a><br><br>The vehicle VIN number<br><br>|
| <a name="/HMKitFleet/requestClearance/#kotlin.String#model.Brand#kotlin.collections.List[model.ControlMeasure]?/PointingToDeclaration/"></a>brand| <a name="/HMKitFleet/requestClearance/#kotlin.String#model.Brand#kotlin.collections.List[model.ControlMeasure]?/PointingToDeclaration/"></a><br><br>The vehicle brand<br><br>|
| <a name="/HMKitFleet/requestClearance/#kotlin.String#model.Brand#kotlin.collections.List[model.ControlMeasure]?/PointingToDeclaration/"></a>controlMeasures| <a name="/HMKitFleet/requestClearance/#kotlin.String#model.Brand#kotlin.collections.List[model.ControlMeasure]?/PointingToDeclaration/"></a><br><br>Optional control measures for some vehicle brands.<br><br>|
  
  



