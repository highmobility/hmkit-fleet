//[hmkit-fleet](../../../index.md)/[com.highmobility.hmkitfleet.model](../index.md)/[EligibilityStatus](index.md)

# EligibilityStatus

[jvm]\
@Serializable

data class [EligibilityStatus](index.md)(val vin: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), val eligible: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html), val dataDelivery: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[EligibilityStatus.DataDelivery](-data-delivery/index.md)&gt; = emptyList(), val connectivityStatus: [EligibilityStatus.ConnectivityStatus](-connectivity-status/index.md)? = null, val primaryUserAssigned: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)? = null)

## Constructors

| | |
|---|---|
| [EligibilityStatus](-eligibility-status.md) | [jvm]<br>constructor(vin: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), eligible: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html), dataDelivery: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[EligibilityStatus.DataDelivery](-data-delivery/index.md)&gt; = emptyList(), connectivityStatus: [EligibilityStatus.ConnectivityStatus](-connectivity-status/index.md)? = null, primaryUserAssigned: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)? = null) |

## Types

| Name | Summary |
|---|---|
| [ConnectivityStatus](-connectivity-status/index.md) | [jvm]<br>@Serializable<br>enum [ConnectivityStatus](-connectivity-status/index.md) : [Enum](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-enum/index.html)&lt;[EligibilityStatus.ConnectivityStatus](-connectivity-status/index.md)&gt; |
| [DataDelivery](-data-delivery/index.md) | [jvm]<br>@Serializable<br>enum [DataDelivery](-data-delivery/index.md) : [Enum](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-enum/index.html)&lt;[EligibilityStatus.DataDelivery](-data-delivery/index.md)&gt; |

## Properties

| Name | Summary |
|---|---|
| [connectivityStatus](connectivity-status.md) | [jvm]<br>@SerialName(value = &quot;connectivity_status&quot;)<br>val [connectivityStatus](connectivity-status.md): [EligibilityStatus.ConnectivityStatus](-connectivity-status/index.md)? = null |
| [dataDelivery](data-delivery.md) | [jvm]<br>@SerialName(value = &quot;data_delivery&quot;)<br>val [dataDelivery](data-delivery.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[EligibilityStatus.DataDelivery](-data-delivery/index.md)&gt; |
| [eligible](eligible.md) | [jvm]<br>val [eligible](eligible.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [primaryUserAssigned](primary-user-assigned.md) | [jvm]<br>@SerialName(value = &quot;primary_user_assigned&quot;)<br>val [primaryUserAssigned](primary-user-assigned.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)? = null |
| [vin](vin.md) | [jvm]<br>val [vin](vin.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
