//[hmkit-fleet](../../../index.md)/[com.highmobility.hmkitfleet.model](../index.md)/[RequestClearanceResponse](index.md)

# RequestClearanceResponse

[jvm]\
@Serializable

data class [RequestClearanceResponse](index.md)(val vin: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), val status: [ClearanceStatus.Status](../-clearance-status/-status/index.md), val description: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)? = null)

## Constructors

| | |
|---|---|
| [RequestClearanceResponse](-request-clearance-response.md) | [jvm]<br>constructor(vin: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), status: [ClearanceStatus.Status](../-clearance-status/-status/index.md), description: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)? = null) |

## Properties

| Name | Summary |
|---|---|
| [description](description.md) | [jvm]<br>val [description](description.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)? = null<br>Filled when status is ERROR |
| [status](status.md) | [jvm]<br>val [status](status.md): [ClearanceStatus.Status](../-clearance-status/-status/index.md) |
| [vin](vin.md) | [jvm]<br>val [vin](vin.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html) |
