//[hmkit-fleet](../../../index.md)/[com.highmobility.hmkitfleet.model](../index.md)/[Odometer](index.md)

# Odometer

[jvm]\
@Serializable

@SerialName(value = &quot;odometer&quot;)

data class [Odometer](index.md)(val value: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html), val unit: [Odometer.Length](-length/index.md)) : [ControlMeasure](../-control-measure/index.md)

## Constructors

| | |
|---|---|
| [Odometer](-odometer.md) | [jvm]<br>constructor(value: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html), unit: [Odometer.Length](-length/index.md)) |

## Types

| Name | Summary |
|---|---|
| [Length](-length/index.md) | [jvm]<br>@Serializable<br>enum [Length](-length/index.md) : [Enum](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-enum/index.html)&lt;[Odometer.Length](-length/index.md)&gt; |

## Properties

| Name | Summary |
|---|---|
| [unit](unit.md) | [jvm]<br>val [unit](unit.md): [Odometer.Length](-length/index.md) |
| [value](value.md) | [jvm]<br>val [value](value.md): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-long/index.html) |
