package com.highmobility.hmkitfleet

import io.jsonwebtoken.Jwts
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import java.security.KeyFactory
import java.security.interfaces.ECPrivateKey
import java.security.spec.PKCS8EncodedKeySpec
import java.util.Base64

/**
 * The credentials to be used for the SDK. Choose from either [HMKitOAuthCredentials] or
 * [HMKitPrivateKeyCredentials].
 *
 * [Check out the Javadoc](https://highmobility.github.io/hmkit-fleet/v2/javadoc/com/highmobility/hmkitfleet/HMKitCredentials.html)
 *
 */
abstract class HMKitCredentials {
  internal abstract fun getTokenRequestBody(jwtProvider: JwtProvider?): String

  interface JwtProvider {
    fun getBaseUrl(): String
    fun generateUuid(): String
    fun getTimestamp(): Long
  }
}

/**
 * The OAuth credentials to be used for the SDK.
 *
 * [Check out the Javadoc](https://highmobility.github.io/hmkit-fleet/v2/javadoc/com/highmobility/hmkitfleet/HMKitOAuthCredentials.html)
 */
@Serializable
data class HMKitOAuthCredentials(
  /**
   * The OAuth client ID.
   */
  val clientId: String,

  /**
   * The OAuth client secret.
   */
  val clientSecret: String,
) : HMKitCredentials() {
  override fun getTokenRequestBody(
    jwtProvider: JwtProvider?
  ): String {
    return buildJsonObject {
      put("client_id", clientId)
      put("client_secret", clientSecret)
      put("grant_type", "client_credentials")
    }.toString()
  }
}

/**
 * The private key credentials to be used for the SDK.
 *
 * [Check out the Javadoc](https://highmobility.github.io/hmkit-fleet/v2/javadoc/com/highmobility/hmkitfleet/HMKitPrivateKeyCredentials.html)
 */
@Serializable
data class HMKitPrivateKeyCredentials(
  /**
   * The OAuth client ID.
   */
  val clientId: String,
  /**
   * The PKCS8 formatted private key. It is included in the .json file downloaded from the developer console after
   * creating a new private key in the App>OAuth section.
   */
  val privateKey: String,
  /**
   * The private key ID. It is included in the .json file downloaded from the developer console after creating a new
   * private key in the App>OAuth section.
   */
  val privateKeyId: String
) : HMKitCredentials() {
  override fun getTokenRequestBody(
    jwtProvider: JwtProvider?
  ): String {
    val jwt = getJwt(
      privateKey,
      privateKeyId,
      jwtProvider!!
    )

    return buildJsonObject {
      put("client_id", clientId)
      put("client_assertion", jwt)
      put("grant_type", "client_credentials")
      put("client_assertion_type", "urn:ietf:params:oauth:client-assertion-type:jwt-bearer")
    }.toString()
  }
}

internal fun getPrivateKey(pkcs8: String): ECPrivateKey {
  val encodedKeyString = pkcs8
    .replace("-----BEGIN PRIVATE KEY-----", "")
    .replace("-----END PRIVATE KEY-----", "")
    .replace("\n", "")

  val decodedPrivateKey = Base64.getDecoder().decode(encodedKeyString)
  val keySpec = PKCS8EncodedKeySpec(decodedPrivateKey)
  val kf = KeyFactory.getInstance("EC")
  return kf.generatePrivate(keySpec) as ECPrivateKey
}

internal fun getJwt(
  privateKey: String,
  privateKeyId: String,
  jwtProvider: HMKitCredentials.JwtProvider
): String {
  val key = getPrivateKey(privateKey)

  val jwt = Jwts.builder()
    .header() // alg is set automatically
    .type("JWT")
    .and()
    .claim("ver", 2)
    .claim("iss", privateKeyId)
    .claim("jti", jwtProvider.generateUuid())
    .claim("iat", jwtProvider.getTimestamp())
    .audience()
    .single(jwtProvider.getBaseUrl())
    .signWith(key)
    .compact()

  return jwt
}
