//[hmkit-fleet](../../../../index.md)/[com.highmobility.hmkitfleet](../../index.md)/[HMKitConfiguration](../index.md)/[Builder](index.md)

# Builder

[jvm]\
class [Builder](index.md)

## Constructors

| | |
|---|---|
| [Builder](-builder.md) | [jvm]<br>constructor() |

## Properties

| Name | Summary |
|---|---|
| [client](client.md) | [jvm]<br>var [client](client.md): OkHttpClient? |
| [credentials](credentials.md) | [jvm]<br>var [credentials](credentials.md): [HMKitCredentials](../../-h-m-kit-credentials/index.md)? |
| [environment](environment.md) | [jvm]<br>var [environment](environment.md): [HMKitFleet.Environment](../../-h-m-kit-fleet/-environment/index.md) |

## Functions

| Name | Summary |
|---|---|
| [build](build.md) | [jvm]<br>fun [build](build.md)(): [HMKitConfiguration](../index.md) |
| [client](client.md) | [jvm]<br>fun [client](client.md)(client: OkHttpClient): [HMKitConfiguration.Builder](index.md)<br>Optionally, set the OkHttpClient to be used for network requests. |
| [credentials](credentials.md) | [jvm]<br>fun [credentials](credentials.md)(credentials: [HMKitCredentials](../../-h-m-kit-credentials/index.md)): [HMKitConfiguration.Builder](index.md)<br>Set the credentials to be used for the SDK. Choose from either [HMKitOAuthCredentials](../../-h-m-kit-o-auth-credentials/index.md) or [HMKitPrivateKeyCredentials](../../-h-m-kit-private-key-credentials/index.md). This is a mandatory field. |
| [environment](environment.md) | [jvm]<br>fun [environment](environment.md)(environment: [HMKitFleet.Environment](../../-h-m-kit-fleet/-environment/index.md)): [HMKitConfiguration.Builder](index.md)<br>Set the SDK environment. Default is Production. |
