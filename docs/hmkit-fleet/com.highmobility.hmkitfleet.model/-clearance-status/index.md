//[hmkit-fleet](../../../index.md)/[com.highmobility.hmkitfleet.model](../index.md)/[ClearanceStatus](index.md)

# ClearanceStatus

[jvm]\
@Serializable

data class [ClearanceStatus](index.md)(val vin: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), val status: [ClearanceStatus.Status](-status/index.md), val brand: [Brand](../-brand/index.md)? = null, val changelog: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[ChangeLogItem](../-change-log-item/index.md)&gt; = emptyList())

## Constructors

| | |
|---|---|
| [ClearanceStatus](-clearance-status.md) | [jvm]<br>constructor(vin: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), status: [ClearanceStatus.Status](-status/index.md), brand: [Brand](../-brand/index.md)? = null, changelog: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[ChangeLogItem](../-change-log-item/index.md)&gt; = emptyList()) |

## Types

| Name | Summary |
|---|---|
| [Status](-status/index.md) | [jvm]<br>@Serializable<br>enum [Status](-status/index.md) : [Enum](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-enum/index.html)&lt;[ClearanceStatus.Status](-status/index.md)&gt; |

## Properties

| Name | Summary |
|---|---|
| [brand](brand.md) | [jvm]<br>val [brand](brand.md): [Brand](../-brand/index.md)? = null |
| [changelog](changelog.md) | [jvm]<br>val [changelog](changelog.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[ChangeLogItem](../-change-log-item/index.md)&gt; |
| [status](status.md) | [jvm]<br>val [status](status.md): [ClearanceStatus.Status](-status/index.md) |
| [vin](vin.md) | [jvm]<br>val [vin](vin.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
