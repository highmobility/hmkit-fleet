//[hmkit-fleet](../../index.md)/[com.highmobility.hmkitfleet](index.md)

# Package-level declarations

## Types

| Name | Summary |
|---|---|
| [HMKitConfiguration](-h-m-kit-configuration/index.md) | [jvm]<br>class [HMKitConfiguration](-h-m-kit-configuration/index.md)<br>The configuration for the SDK. This is a mandatory class to be passed to the SDK. |
| [HMKitCredentials](-h-m-kit-credentials/index.md) | [jvm]<br>abstract class [HMKitCredentials](-h-m-kit-credentials/index.md)<br>The credentials to be used for the SDK. Choose from either [HMKitOAuthCredentials](-h-m-kit-o-auth-credentials/index.md) or [HMKitPrivateKeyCredentials](-h-m-kit-private-key-credentials/index.md). |
| [HMKitFleet](-h-m-kit-fleet/index.md) | [jvm]<br>class [HMKitFleet](-h-m-kit-fleet/index.md)(hmKitConfiguration: [HMKitConfiguration](-h-m-kit-configuration/index.md))<br>HMKitFleet is the access point for the Fleet SDK functionality. It is accessed by creating a new HMKitFleet object with a HMKitConfiguration object. |
| [HMKitOAuthCredentials](-h-m-kit-o-auth-credentials/index.md) | [jvm]<br>@Serializable<br>data class [HMKitOAuthCredentials](-h-m-kit-o-auth-credentials/index.md)(val clientId: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), val clientSecret: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)) : [HMKitCredentials](-h-m-kit-credentials/index.md)<br>The OAuth credentials to be used for the SDK. |
| [HMKitPrivateKeyCredentials](-h-m-kit-private-key-credentials/index.md) | [jvm]<br>@Serializable<br>data class [HMKitPrivateKeyCredentials](-h-m-kit-private-key-credentials/index.md)(val clientId: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), val privateKey: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html), val privateKeyId: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin-stdlib/kotlin/-string/index.html)) : [HMKitCredentials](-h-m-kit-credentials/index.md)<br>The private key credentials to be used for the SDK. |
