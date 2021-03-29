//[hmkit-fleet](../../index.md)/[model](index.md)



# Package model  


## Types  
  
|  Name |  Summary | 
|---|---|
| <a name="model/AccessToken///PointingToDeclaration/"></a>[AccessToken](-access-token/index.md)| <a name="model/AccessToken///PointingToDeclaration/"></a>[jvm]  <br>Content  <br>data class [AccessToken](-access-token/index.md)(**tokenType**: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), **scope**: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), **refreshToken**: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), **expiresIn**: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), **accessToken**: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html))  <br><br><br>|
| <a name="model/Brand///PointingToDeclaration/"></a>[Brand](-brand/index.md)| <a name="model/Brand///PointingToDeclaration/"></a>[jvm]  <br>Content  <br>enum [Brand](-brand/index.md) : [Enum](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-enum/index.html)<[Brand](-brand/index.md)>   <br><br><br>|
| <a name="model/ClearanceStatus///PointingToDeclaration/"></a>[ClearanceStatus](-clearance-status/index.md)| <a name="model/ClearanceStatus///PointingToDeclaration/"></a>[jvm]  <br>Content  <br>data class [ClearanceStatus](-clearance-status/index.md)(**vin**: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), **status**: [ClearanceStatus.Status](-clearance-status/-status/index.md))  <br><br><br>|
| <a name="model/ControlMeasure///PointingToDeclaration/"></a>[ControlMeasure](-control-measure/index.md)| <a name="model/ControlMeasure///PointingToDeclaration/"></a>[jvm]  <br>Content  <br>sealed class [ControlMeasure](-control-measure/index.md)  <br><br><br>|
| <a name="model/Odometer///PointingToDeclaration/"></a>[Odometer](-odometer/index.md)| <a name="model/Odometer///PointingToDeclaration/"></a>[jvm]  <br>Content  <br>data class [Odometer](-odometer/index.md)(**value**: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html), **unit**: [Odometer.Length](-odometer/-length/index.md)) : [ControlMeasure](-control-measure/index.md)  <br><br><br>|
| <a name="model/VehicleAccess///PointingToDeclaration/"></a>[VehicleAccess](-vehicle-access/index.md)| <a name="model/VehicleAccess///PointingToDeclaration/"></a>[jvm]  <br>Content  <br>data class [VehicleAccess](-vehicle-access/index.md)(**vin**: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), **accessToken**: [AccessToken](-access-token/index.md), **accessCertificate**: )  <br><br><br>|

