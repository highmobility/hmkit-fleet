//[hmkit-fleet](../../../../index.md)/[com.highmobility.hmkitfleet.model](../../index.md)/[ClearanceStatus](../index.md)/[Status](index.md)

# Status

[jvm]\
@Serializable

enum [Status](index.md) : [Enum](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-enum/index.html)&lt;[ClearanceStatus.Status](index.md)&gt;

## Entries

| | |
|---|---|
| [APPROVED](-a-p-p-r-o-v-e-d/index.md) | [jvm]<br>@SerialName(value = &quot;approved&quot;)<br>[APPROVED](-a-p-p-r-o-v-e-d/index.md) |
| [PENDING](-p-e-n-d-i-n-g/index.md) | [jvm]<br>@SerialName(value = &quot;pending&quot;)<br>[PENDING](-p-e-n-d-i-n-g/index.md) |
| [ERROR](-e-r-r-o-r/index.md) | [jvm]<br>@SerialName(value = &quot;error&quot;)<br>[ERROR](-e-r-r-o-r/index.md) |
| [REVOKING](-r-e-v-o-k-i-n-g/index.md) | [jvm]<br>@SerialName(value = &quot;revoking&quot;)<br>[REVOKING](-r-e-v-o-k-i-n-g/index.md) |
| [REVOKED](-r-e-v-o-k-e-d/index.md) | [jvm]<br>@SerialName(value = &quot;revoked&quot;)<br>[REVOKED](-r-e-v-o-k-e-d/index.md) |
| [REJECTED](-r-e-j-e-c-t-e-d/index.md) | [jvm]<br>@SerialName(value = &quot;rejected&quot;)<br>[REJECTED](-r-e-j-e-c-t-e-d/index.md) |
| [CANCELING](-c-a-n-c-e-l-i-n-g/index.md) | [jvm]<br>@SerialName(value = &quot;canceling&quot;)<br>[CANCELING](-c-a-n-c-e-l-i-n-g/index.md) |
| [CANCELED](-c-a-n-c-e-l-e-d/index.md) | [jvm]<br>@SerialName(value = &quot;canceled&quot;)<br>[CANCELED](-c-a-n-c-e-l-e-d/index.md) |

## Properties

| Name | Summary |
|---|---|
| [entries](entries.md) | [jvm]<br>val [entries](entries.md): [EnumEntries](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin.enums/-enum-entries/index.html)&lt;[ClearanceStatus.Status](index.md)&gt;<br>Returns a representation of an immutable list of all enum entries, in the order they're declared. |
| [name](../../-eligibility-status/-connectivity-status/-u-n-k-n-o-w-n/index.md#-372974862%2FProperties%2F-1829386432) | [jvm]<br>val [name](../../-eligibility-status/-connectivity-status/-u-n-k-n-o-w-n/index.md#-372974862%2FProperties%2F-1829386432): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html) |
| [ordinal](../../-eligibility-status/-connectivity-status/-u-n-k-n-o-w-n/index.md#-739389684%2FProperties%2F-1829386432) | [jvm]<br>val [ordinal](../../-eligibility-status/-connectivity-status/-u-n-k-n-o-w-n/index.md#-739389684%2FProperties%2F-1829386432): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-int/index.html) |

## Functions

| Name | Summary |
|---|---|
| [valueOf](value-of.md) | [jvm]<br>fun [valueOf](value-of.md)(value: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)): [ClearanceStatus.Status](index.md)<br>Returns the enum constant of this type with the specified name. The string must match exactly an identifier used to declare an enum constant in this type. (Extraneous whitespace characters are not permitted.) |
| [values](values.md) | [jvm]<br>fun [values](values.md)(): [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-array/index.html)&lt;[ClearanceStatus.Status](index.md)&gt;<br>Returns an array containing the constants of this enum type, in the order they're declared. |
