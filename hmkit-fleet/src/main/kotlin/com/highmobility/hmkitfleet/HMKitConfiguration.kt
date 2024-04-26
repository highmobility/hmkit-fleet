package com.highmobility.hmkitfleet

import okhttp3.OkHttpClient

/**
 * The configuration for the SDK. This is a mandatory class to be passed to the SDK.
 *
 * @param credentials The credentials to be used for the SDK. Choose from either [HMKitOAuthCredentials] or
 * [HMKitPrivateKeyCredentials].
 * @param environment The SDK environment. Default is Production.
 * @param client Optionally, set the OkHttpClient to be used for network requests.
 *
 * [Check out the Javadoc](https://highmobility.github.io/hmkit-fleet/v2/javadoc/com/highmobility/hmkitfleet/HMKitConfiguration.html)
 */
class HMKitConfiguration private constructor(builder: Builder) {
  val credentials = builder.credentials ?: throw IllegalArgumentException("credentials must be set")
  val client = builder.client ?: OkHttpClient()
  val environment = builder.environment

  class Builder {
    var credentials: HMKitCredentials? = null
      private set
    var environment: HMKitFleet.Environment = HMKitFleet.Environment.PRODUCTION
      private set
    var client: OkHttpClient? = null
      private set

    /**
     * Set the credentials to be used for the SDK. Choose from either [HMKitOAuthCredentials] or
     * [HMKitPrivateKeyCredentials]. This is a mandatory field.
     */
    fun credentials(credentials: HMKitCredentials) = apply { this.credentials = credentials }

    /**
     * Set the SDK environment. Default is Production.
     */
    fun environment(environment: HMKitFleet.Environment) = apply { this.environment = environment }

    /**
     * Optionally, set the OkHttpClient to be used for network requests.
     */
    fun client(client: OkHttpClient) = apply { this.client = client }

    fun build() = HMKitConfiguration(this)
  }
}
