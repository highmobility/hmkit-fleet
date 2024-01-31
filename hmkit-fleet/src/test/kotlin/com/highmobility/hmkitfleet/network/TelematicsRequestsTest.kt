/*
 * The MIT License
 *
 * Copyright (c) 2023- High-Mobility GmbH (https://high-mobility.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.highmobility.hmkitfleet.network

import com.highmobility.autoapi.Diagnostics
import com.highmobility.autoapi.property.Property
import com.highmobility.autoapi.value.measurement.Length
import com.highmobility.crypto.Crypto
import com.highmobility.crypto.value.DeviceSerial
import com.highmobility.crypto.value.Issuer
import com.highmobility.crypto.value.PrivateKey
import com.highmobility.hmkitfleet.BaseTest
import com.highmobility.hmkitfleet.ClientCertificate
import com.highmobility.hmkitfleet.mockAccessCert
import com.highmobility.hmkitfleet.mockSignature
import com.highmobility.value.Bytes
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonPrimitive
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.net.HttpURLConnection

internal class TelematicsRequestsTest : BaseTest() {
    private val mockWebServer = MockWebServer()
    private val client = OkHttpClient()

    private val privateKey = mockk<PrivateKey> {
        every { base64 } returns mockSignature.base64
    }
    private val nonce = Bytes("AAAAAAAAAAAAAAAAAA")
    private val encryptedSentCommand = nonce
    private val encryptedReceivedCommand = nonce

    private val decryptedReceivedCommand =
        Diagnostics.State.Builder().setOdometer(Property(Length(11000.0, Length.Unit.KILOMETERS))).build()

    private val certificate = mockk<ClientCertificate> {
        every { serial } returns DeviceSerial(nonce)
        every { issuer } returns Issuer("00000000")
    }

    private val crypto = mockk<Crypto> {
        // random encryption ok here
        every {
            createTelematicsContainer(
                Diagnostics.GetState(), privateKey, certificate.serial, mockAccessCert, nonce
            )
        } returns encryptedSentCommand

        every {
            getPayloadFromTelematicsContainer(
                Bytes(encryptedReceivedCommand), privateKey, mockAccessCert
            )
        } returns decryptedReceivedCommand
    }

    @BeforeEach
    fun setUp() {
        mockWebServer.start()
        every { certificate.serial.hex } returns "AAAAAAAAAAAAAAAAAA" // not sure why this needs to be defined for a real object
    }

    @AfterEach
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun createAccessToken() {
        mockNonceResponse()
        mockTelematicsResponse()

        val baseUrl: HttpUrl = mockWebServer.url("")

        val telematicsRequests = TelematicsRequests(
            client, mockLogger, baseUrl.toString(), privateKey, certificate, crypto
        )

        val response = runBlocking {
            telematicsRequests.sendCommand(Diagnostics.GetState(), mockAccessCert)
        }

        verify {
            crypto.createTelematicsContainer(
                Diagnostics.GetState(), privateKey, certificate.serial, mockAccessCert, nonce
            )
        }

        // first request is nonce
        val nonceRequest: RecordedRequest = mockWebServer.takeRequest()
        assert(nonceRequest.path!!.endsWith("/nonces"))

        // verify request
        val nonceRequestBody = Json.parseToJsonElement(nonceRequest.body.readUtf8()) as JsonObject
        assert(nonceRequestBody["serial_number"]!!.jsonPrimitive.contentOrNull == certificate.serial.hex)

        // second request is telematics command
        val commandRequest: RecordedRequest = mockWebServer.takeRequest()
        assert(commandRequest.path!!.endsWith("/telematics_commands"))

        // verify request
        val jsonBody = Json.parseToJsonElement(commandRequest.body.readUtf8()) as JsonObject
        assert(jsonBody["serial_number"]!!.jsonPrimitive.contentOrNull == certificate.serial.hex)
        assert(jsonBody["issuer"]!!.jsonPrimitive.contentOrNull == certificate.issuer.hex)
        assert(jsonBody["data"]!!.jsonPrimitive.contentOrNull == encryptedSentCommand.base64)

        // verify command decrypted
        verify {
            crypto.getPayloadFromTelematicsContainer(
                encryptedReceivedCommand, privateKey, mockAccessCert
            )
        }

        // verify final telematics command response
        assert(response.response?.responseData!! == decryptedReceivedCommand)
    }

  @Test
  fun createAccessTokenV05() {
    mockNonceResponse()
    mockTelematicsResponse()

    val baseUrl: HttpUrl = mockWebServer.url("")

    val telematicsRequests = TelematicsRequests(
      client, mockLogger, baseUrl.toString(), privateKey, certificate, crypto
    )

    val response = runBlocking {
      telematicsRequests.sendCommandV05(Diagnostics.GetState(), mockAccessCert)
    }

    verify {
      crypto.createTelematicsContainer(
        Diagnostics.GetState(), privateKey, certificate.serial, mockAccessCert, nonce
      )
    }

    // first request is nonce
    val nonceRequest: RecordedRequest = mockWebServer.takeRequest()
    assert(nonceRequest.path!!.endsWith("/nonces"))

    // verify request
    val nonceRequestBody = Json.parseToJsonElement(nonceRequest.body.readUtf8()) as JsonObject
    assert(nonceRequestBody["serial_number"]!!.jsonPrimitive.contentOrNull == certificate.serial.hex)

    // second request is telematics command
    val commandRequest: RecordedRequest = mockWebServer.takeRequest()
    assert(commandRequest.path!!.endsWith("/telematics_commands"))

    // verify request
    val jsonBody = Json.parseToJsonElement(commandRequest.body.readUtf8()) as JsonObject
    assert(jsonBody["serial_number"]!!.jsonPrimitive.contentOrNull == certificate.serial.hex)
    assert(jsonBody["issuer"]!!.jsonPrimitive.contentOrNull == certificate.issuer.hex)
    assert(jsonBody["data"]!!.jsonPrimitive.contentOrNull == encryptedSentCommand.base64)

    // verify command decrypted
    verify {
      crypto.getPayloadFromTelematicsContainer(
        encryptedReceivedCommand, privateKey, mockAccessCert
      )
    }

    // verify final telematics command response
    assert(response.response!! == decryptedReceivedCommand)
  }

    private fun mockTelematicsResponse() {
        val mockResponse = MockResponse().setResponseCode(HttpURLConnection.HTTP_OK).setBody(
            """
                    {
                      "status": "ok",
                      "response_data": "${encryptedReceivedCommand.base64}",
                      "message": "Response received"
                    }
                """.trimIndent()
        )
        mockWebServer.enqueue(mockResponse)
    }

    private fun mockNonceResponse() {
        val mockResponse = MockResponse().setResponseCode(HttpURLConnection.HTTP_CREATED).setBody(
            """
                    {
                        "nonce": "${nonce.base64}"
                    }
                """.trimIndent()
        )
        mockWebServer.enqueue(mockResponse)
    }

    // error responses

    @Test
    fun nonceErrorResponse() = runBlocking {
        val mockResponse = MockResponse().setResponseCode(HttpURLConnection.HTTP_UNAUTHORIZED).setBody(
            "{\"errors\":" + "[{\"detail\":\"Missing or invalid assertion. It must be a JWT signed with the service account key.\"," + "\"source\":\"assertion\"," + "\"title\":\"Not authorized\"}]}"
        )
        mockWebServer.enqueue(mockResponse)
        val mockUrl = mockWebServer.url("").toString()

        val webService = TelematicsRequests(client, mockLogger, mockUrl, privateKey, certificate, crypto)
        val response = webService.sendCommand(Diagnostics.GetState(), mockAccessCert)

        Assertions.assertTrue(response.errors[0].title == "Not authorized")
    }

    @Test
    fun nonceUnknownResponse() = runBlocking {
        val mockResponse = MockResponse().setResponseCode(HttpURLConnection.HTTP_UNAUTHORIZED).setBody(
            """
                    {"invalidKey":"invalidValue"}
                """.trimIndent()
        )
        mockWebServer.enqueue(mockResponse)
        val mockUrl = mockWebServer.url("").toString()

        val webService = TelematicsRequests(client, mockLogger, mockUrl, privateKey, certificate, crypto)
        val response = webService.sendCommand(Diagnostics.GetState(), mockAccessCert)

        val genericError = genericError("")
        Assertions.assertTrue(response.errors[0].title == genericError.title)
    }

    @Test
    fun telematicsCommandErrorResponse() = runBlocking {
        mockNonceResponse()
        val unencryptedData = Bytes("AAIz6waDffJYyCEZyYrwZnZXf8VAn66SGfEgJy7+AP4A/gD+AP4A/gD+AAMCAQT/")

        val mockResponse = MockResponse().setResponseCode(404).setBody(
            """
                {
                    "message": "invalid_data",
                    "response_data": "${unencryptedData.base64}",
                    "status": "error"
                }
                """.trimIndent()
        )
        // successful nonce

        mockWebServer.enqueue(mockResponse)
        val mockUrl = mockWebServer.url("").toString()

        val crypto = mockk<Crypto> {
            every {
                createTelematicsContainer(Diagnostics.GetState(), privateKey, certificate.serial, mockAccessCert, nonce)
            } returns encryptedSentCommand
        }

        val webService = TelematicsRequests(client, mockLogger, mockUrl, privateKey, certificate, crypto)
        val response = webService.sendCommand(Diagnostics.GetState(), mockAccessCert)

        Assertions.assertTrue(response.response?.message == "invalid_data")
        Assertions.assertTrue(response.response?.responseData == unencryptedData)
        Assertions.assertTrue(response.response?.status == TelematicsCommandResponse.Status.ERROR)
    }

    @Test
    fun generalServerError() = runBlocking {
        mockNonceResponse()

        val mockResponse = MockResponse().setResponseCode(401).setBody(
            """
                [
                  {
                    "title": "title1",
                    "source": "source1",
                    "detail": "detail1"
                  }
                ]
                """.trimIndent()
        )
        // successful nonce

        mockWebServer.enqueue(mockResponse)
        val mockUrl = mockWebServer.url("").toString()

        val crypto = mockk<Crypto> {
            every {
                createTelematicsContainer(Diagnostics.GetState(), privateKey, certificate.serial, mockAccessCert, nonce)
            } returns encryptedSentCommand
        }

        val webService = TelematicsRequests(client, mockLogger, mockUrl, privateKey, certificate, crypto)
        val response = webService.sendCommand(Diagnostics.GetState(), mockAccessCert)

        Assertions.assertTrue(response.errors[0].title == "title1")
        Assertions.assertTrue(response.errors[0].source == "source1")
        Assertions.assertTrue(response.errors[0].detail == "detail1")
    }

    @Test
    fun telematicsCommandErrorResponseUnencryptedData() = runBlocking {
        mockNonceResponse()

        val mockResponse = MockResponse().setResponseCode(404).setBody(
            """
                {
                    "message": "invalid_data",
                    "response_data": "AAKmNHIgCtR0hrKisC6a185zHW9RLMFyrZr3vEH+AP4AAQH+AP4A/gAF0sTqC04m6PDp6NXc1fRzPSsWCLajtlYraiDimxVTZQqrbo+8/v//",
                    "status": "error"
                }
                """.trimIndent()
        )
        // successful nonce

        mockWebServer.enqueue(mockResponse)
        val mockUrl = mockWebServer.url("").toString()

        val unencryptedResponse =
            Bytes("AAKmNHIgCtR0hrKisC6a185zHW9RLMFyrZr3vEH+AP4AAQH+AP4A/gAF0sTqC04m6PDp6NXc1fRzPSsWCLajtlYraiDimxVTZQqrbo+8/v//")
        val crypto = mockk<Crypto> {
            every {
                createTelematicsContainer(Diagnostics.GetState(), privateKey, certificate.serial, mockAccessCert, nonce)
            } returns encryptedSentCommand

            // fail the response parsing
            every {
                getPayloadFromTelematicsContainer(
                    unencryptedResponse,
                    privateKey,
                    mockAccessCert
                )
            } throws (IllegalArgumentException("crypto error"))
        }

        val webService = TelematicsRequests(client, mockLogger, mockUrl, privateKey, certificate, crypto)
        val response = webService.sendCommand(Diagnostics.GetState(), mockAccessCert)

        Assertions.assertTrue(response.response?.message == "invalid_data")
        Assertions.assertTrue(response.response?.responseData == unencryptedResponse)
        Assertions.assertTrue(response.response?.status == TelematicsCommandResponse.Status.ERROR)
    }

    @Test
    fun telematicsCommandUnknownResponse() = runBlocking {
        mockNonceResponse()

        val mockResponse = MockResponse().setResponseCode(404).setBody(
            "{\"invalidKey\":\"invalidValue\"}"
        )
        mockWebServer.enqueue(mockResponse)
        val mockUrl = mockWebServer.url("").toString()

        val webService = TelematicsRequests(client, mockLogger, mockUrl, privateKey, certificate, crypto)
        val response = webService.sendCommand(Diagnostics.GetState(), mockAccessCert)

        val genericError = genericError("")
        Assertions.assertTrue(response.errors[0].title == genericError.title)
    }
}