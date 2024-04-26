//[hmkit-fleet](../../../index.md)/[com.highmobility.hmkitfleet](../index.md)/[HMKitPrivateKeyCredentials](index.md)

# HMKitPrivateKeyCredentials

[jvm]\
@Serializable

data class [HMKitPrivateKeyCredentials](index.md)(val clientId: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), val privateKey: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), val privateKeyId: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)) : [HMKitCredentials](../-h-m-kit-credentials/index.md)

The private key credentials to be used for the SDK.

[Check out the Javadoc](https://highmobility.github.io/hmkit-fleet/v2/javadoc/com/highmobility/hmkitfleet/HMKitPrivateKeyCredentials.html)

## Constructors

| | |
|---|---|
| [HMKitPrivateKeyCredentials](-h-m-kit-private-key-credentials.md) | [jvm]<br>constructor(clientId: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), privateKey: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), privateKeyId: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)) |

## Properties

| Name | Summary |
|---|---|
| [clientId](client-id.md) | [jvm]<br>val [clientId](client-id.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>The OAuth client ID. |
| [privateKey](private-key.md) | [jvm]<br>val [privateKey](private-key.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>The PKCS8 formatted private key. It is included in the .json file downloaded from the developer console after creating a new private key in the App>OAuth section. |
| [privateKeyId](private-key-id.md) | [jvm]<br>val [privateKeyId](private-key-id.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>The private key ID. It is included in the .json file downloaded from the developer console after creating a new private key in the App>OAuth section. |
