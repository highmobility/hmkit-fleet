//[hmkit-fleet](../../../index.md)/[com.highmobility.hmkitfleet](../index.md)/[HMKitConfiguration](index.md)

# HMKitConfiguration

class [HMKitConfiguration](index.md)

The configuration for the SDK. This is a mandatory class to be passed to the SDK.

#### Parameters

jvm

| | |
|---|---|
| credentials | The credentials to be used for the SDK. Choose from either [HMKitOAuthCredentials](../-h-m-kit-o-auth-credentials/index.md) or [HMKitPrivateKeyCredentials](../-h-m-kit-private-key-credentials/index.md). |
| environment | The SDK environment. Default is Production. |
| client | Optionally, set the OkHttpClient to be used for network requests.<br>[Check out the Javadoc](https://highmobility.github.io/hmkit-fleet/v2/javadoc/com/highmobility/hmkitfleet/HMKitConfiguration.html) |

## Types

| Name | Summary |
|---|---|
| [Builder](-builder/index.md) | [jvm]<br>class [Builder](-builder/index.md) |

## Properties

| Name | Summary |
|---|---|
| [client](client.md) | [jvm]<br>val [client](client.md): OkHttpClient |
| [credentials](credentials.md) | [jvm]<br>val [credentials](credentials.md): [HMKitCredentials](../-h-m-kit-credentials/index.md) |
| [environment](environment.md) | [jvm]<br>val [environment](environment.md): [HMKitFleet.Environment](../-h-m-kit-fleet/-environment/index.md) |
