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

import com.highmobility.hmkitfleet.BaseTest
import com.highmobility.hmkitfleet.model.Brand
import com.highmobility.hmkitfleet.model.EligibilityStatus
import com.highmobility.hmkitfleet.notExpiredAccessToken
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.net.HttpURLConnection

internal class UtilityRequestsTest : BaseTest() {
  val mockWebServer = MockWebServer()
  val client = OkHttpClient()
  val authToken = notExpiredAccessToken()
  val accessTokenRequests = mockk<AccessTokenRequests>()

  @BeforeEach
  fun setUp() {
    mockWebServer.start()
    coEvery { accessTokenRequests.getAccessToken() } returns Response(authToken)
  }

  @AfterEach
  fun tearDown() {
    mockWebServer.shutdown()
  }

  // get eligibility

  private val getEligibilitySuccessResponses = listOf(
    """
        {
          "vin": "C0NNECT0000000001",
          "eligible": true,
          "data_delivery": [
            "pull",
            "push"
          ]
        }
    """.trimIndent(),
    """
        {
          "vin": "C0NNECT0000000001",
          "eligible": true,
          "data_delivery": [
            "pull",
            "push"
          ],
          "unknown": true
        }
    """.trimIndent(),
  )

  @Test
  fun getEligibilitySuccess() {
    getEligibilitySuccessResponses.forEach {
      val mockResponse = MockResponse()
        .setResponseCode(HttpURLConnection.HTTP_OK)
        .setBody(it)
      mockWebServer.enqueue(mockResponse)
      val mockUrl = mockWebServer.url("").toString()
      val webService = UtilityRequests(client, mockLogger, mockUrl, accessTokenRequests)

      val response = runBlocking {
        webService.getEligibility("C0NNECT0000000001", Brand.BMW)
      }

      coVerify { accessTokenRequests.getAccessToken() }

      val recordedRequest: RecordedRequest = mockWebServer.takeRequest()
      assertTrue(recordedRequest.path!!.endsWith("/eligibility"))

      // verify request
      assertTrue(recordedRequest.headers["Authorization"] == "Bearer ${authToken.accessToken}")
      val jsonBody = Json.parseToJsonElement(recordedRequest.body.readUtf8())
      assertTrue(jsonBody.jsonObject["vin"]?.jsonPrimitive?.contentOrNull == "C0NNECT0000000001")
      assertTrue(jsonBody.jsonObject["brand"]?.jsonPrimitive?.contentOrNull == "bmw")

      // verify response
      val status = response.response!!
      assertTrue(status.vin == "C0NNECT0000000001")
      assertTrue(status.eligible)
      assertTrue(status.dataDelivery.size == 2)
      assertTrue(status.dataDelivery.find { it == EligibilityStatus.DataDelivery.PULL } != null)
      assertTrue(status.dataDelivery.find { it == EligibilityStatus.DataDelivery.PUSH } != null)
      assertTrue(status.connectivityStatus == null)
      assertTrue(status.primaryUserAssigned == null)
    }
  }

  @Test
  fun getEligibilitySuccessOptionalValues() {
    val mockResponse = MockResponse()
      .setResponseCode(HttpURLConnection.HTTP_OK)
      .setBody(
        """
                {
                  "vin": "C0NNECT0000000001",
                  "eligible": true,
                  "data_delivery": [
                    "pull",
                    "push"
                  ],
                  "connectivity_status": "deactivated",
                  "primary_user_assigned": true
                }
        """.trimIndent()
      )

    mockWebServer.enqueue(mockResponse)
    val mockUrl = mockWebServer.url("").toString()
    val webService = UtilityRequests(client, mockLogger, mockUrl, accessTokenRequests)

    val status = runBlocking {
      webService.getEligibility("C0NNECT0000000001", Brand.BMW)
    }.response!!

    coVerify { accessTokenRequests.getAccessToken() }

    val recordedRequest: RecordedRequest = mockWebServer.takeRequest()
    assertTrue(recordedRequest.path!!.endsWith("/eligibility"))

    // verify request
    assertTrue(recordedRequest.headers["Authorization"] == "Bearer ${authToken.accessToken}")
    val jsonBody = Json.parseToJsonElement(recordedRequest.body.readUtf8())
    assertTrue(jsonBody.jsonObject["vin"]?.jsonPrimitive?.contentOrNull == "C0NNECT0000000001")
    assertTrue(jsonBody.jsonObject["brand"]?.jsonPrimitive?.contentOrNull == "bmw")

    // verify response
    assertTrue(status.vin == "C0NNECT0000000001")
    assertTrue(status.eligible)
    assertTrue(status.dataDelivery.size == 2)
    assertTrue(status.dataDelivery.find { it == EligibilityStatus.DataDelivery.PULL } != null)
    assertTrue(status.dataDelivery.find { it == EligibilityStatus.DataDelivery.PUSH } != null)

    assertTrue(status.connectivityStatus == EligibilityStatus.ConnectivityStatus.DEACTIVATED)
    assertTrue(status.primaryUserAssigned == true)
  }

  @Test
  fun getEligibilityAuthTokenError() = runBlocking {
    testAuthTokenErrorReturned(mockWebServer, accessTokenRequests) { mockUrl ->
      val webService = UtilityRequests(client, mockLogger, mockUrl, accessTokenRequests)
      webService.getEligibility(
        "C0NNECT0000000001",
        Brand.BMW,
      )
    }
  }

  @Test
  fun getEligibilityErrorResponse() = runBlocking {
    testErrorResponseReturned(mockWebServer) { mockUrl ->
      val webService = UtilityRequests(client, mockLogger, mockUrl, accessTokenRequests)
      webService.getEligibility(
        "C0NNECT0000000001",
        Brand.BMW,
      )
    }
  }

  @Test
  fun getEligibilityUnknownResponse() = runBlocking {
    testForUnknownResponseGenericErrorReturned(mockWebServer) { mockUrl ->
      val webService = UtilityRequests(client, mockLogger, mockUrl, accessTokenRequests)
      webService.getEligibility(
        "C0NNECT0000000001",
        Brand.BMW,
      )
    }
  }
}
