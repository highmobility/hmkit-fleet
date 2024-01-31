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
import com.highmobility.hmkitfleet.model.ClearanceStatus
import com.highmobility.hmkitfleet.model.ControlMeasure
import com.highmobility.hmkitfleet.model.Odometer
import com.highmobility.hmkitfleet.notExpiredAccessToken
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
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

private const val CHANGE_LOG = """[
                {
                   "brand":"bmw",
                   "changelog":[
                      {
                         "status":"pending",
                         "timestamp":"2022-03-30T12:27:25"
                      },
                      {
                         "status":"approved",
                         "timestamp":"2022-03-30T12:27:27"
                      },
                      {
                         "status":"revoked",
                         "timestamp":"2022-04-30T12:27:27"
                      }
                   ],
                   "status":"revoked",
                   "vin":"WBY8P210X07J49112"
                },
                {
                   "brand":"bmw",
                   "changelog":[
                      {
                         "status":"pending",
                         "timestamp":"2022-04-01T12:27:25"
                      },
                      {
                         "status":"approved",
                         "timestamp":"2022-04-01T12:27:27"
                      }
                   ],
                   "status":"approved",
                   "vin":"WBY8P210X07J49112"
                },
                {
                   "brand":"bmw",
                   "changelog":[],
                   "status":"approved",
                   "vin":"WBY8P210X07J49112"
                },
                {
                   "brand":"bmw",
                   "status":"approved",
                   "vin":"WBY8P210X07J49112"
                }
                ]
                """

internal class ClearanceRequestsTest : BaseTest() {
  val mockWebServer = MockWebServer()
  val client = OkHttpClient()
  val accessToken = notExpiredAccessToken()
  val accessTokenRequests = mockk<AccessTokenRequests>()

  val controlMeasures = listOf<ControlMeasure>(
    Odometer(
      100000L,
      Odometer.Length.KILOMETERS
    )
  )

  @BeforeEach
  fun setUp() {
    mockWebServer.start()
    coEvery { accessTokenRequests.getAccessToken() } returns Response(accessToken)
  }

  @AfterEach
  fun tearDown() {
    mockWebServer.shutdown()
  }

  // request clearance

  private val requestClearanceSuccessResponses = listOf(
    """
    {
      "vehicles": [
        {
          "vin": "C0NNECT0000000001",
          "status": "pending"
        }
      ]
    }
    """.trimIndent(),
    """
    {
      "vehicles": [
        {
          "vin": "C0NNECT0000000001",
          "status": "pending",
          "unknown" : "value"
        }
      ]
    }
  """.trimIndent()
  )

  @Test
  fun requestClearanceSuccessResponse() {
    requestClearanceSuccessResponses.forEach {
      val mockResponse = MockResponse()
        .setResponseCode(HttpURLConnection.HTTP_OK)
        .setBody(it)
      mockWebServer.enqueue(mockResponse)
      val mockUrl = mockWebServer.url("").toString()
      val webService = ClearanceRequests(client, mockLogger, mockUrl, accessTokenRequests)

      val status = runBlocking {
        webService.requestClearance("C0NNECT0000000001", Brand.MERCEDES_BENZ, controlMeasures)
      }

      coVerify { accessTokenRequests.getAccessToken() }

      val recordedRequest: RecordedRequest = mockWebServer.takeRequest()
      assertTrue(recordedRequest.path!!.endsWith("/fleets/vehicles"))

      // verify request
      assertTrue(recordedRequest.headers["Authorization"] == "Bearer ${accessToken.accessToken}")

      val jsonBody = Json.parseToJsonElement(recordedRequest.body.readUtf8())
      val array = jsonBody.jsonObject["vehicles"] as JsonArray
      val firstVehicle = array.first() as JsonObject
      assertTrue(firstVehicle["vin"]?.jsonPrimitive?.contentOrNull == "C0NNECT0000000001")
      val controlMeasures = firstVehicle["control_measures"]?.jsonObject
      val odometer = controlMeasures?.get("odometer")?.jsonObject
      assertTrue(odometer?.get("value")?.jsonPrimitive?.contentOrNull == "100000")
      assertTrue(odometer?.get("unit")?.jsonPrimitive?.contentOrNull == "kilometers")

      // verify response
      assertTrue(status.response!!.status == ClearanceStatus.Status.PENDING)
      assertTrue(status.response!!.vin == "C0NNECT0000000001")
    }
  }

  @Test
  fun requestClearanceAuthTokenError() = runBlocking {
    testAuthTokenErrorReturned(mockWebServer, accessTokenRequests) { mockUrl ->
      val webService = ClearanceRequests(client, mockLogger, mockUrl, accessTokenRequests)
      webService.requestClearance(
        "C0NNECT0000000001",
        Brand.MERCEDES_BENZ,
        controlMeasures
      )
    }
  }

  @Test
  fun requestClearanceErrorResponse() = runBlocking {
    testErrorResponseReturned(mockWebServer) { mockUrl ->
      val webService = ClearanceRequests(client, mockLogger, mockUrl, accessTokenRequests)
      webService.requestClearance(
        "C0NNECT0000000001",
        Brand.MERCEDES_BENZ,
        controlMeasures
      )
    }
  }

  @Test
  fun requestClearanceUnknownResponse() = runBlocking {
    testForUnknownResponseGenericErrorReturned(mockWebServer) { mockUrl ->
      val webService = ClearanceRequests(client, mockLogger, mockUrl, accessTokenRequests)
      webService.requestClearance(
        "C0NNECT0000000001",
        Brand.MERCEDES_BENZ,
        controlMeasures
      )
    }
  }

  // get clearance statuses

  private val getClearanceStatusesSuccessResponses = listOf(
    """
    [
      {
        "brand":"bmw",
        "vin": "C0NNECT0000000001",
        "status": "pending"
      },
      {
        "brand":"bmw",
        "vin": "WBADT43452G296404",
        "status": "pending"
      }
    ]
    """.trimIndent(),
    """
    [
      {
        "brand":"bmw",
        "vin": "C0NNECT0000000001",
        "status": "pending",
        "unknown" : "value"
      },
      {
        "brand":"bmw",
        "vin": "WBADT43452G296404",
        "status": "pending"
      }
    ]
  """.trimIndent()
  )

  @Test
  fun getClearanceStatusesSuccessResponse() {
    getClearanceStatusesSuccessResponses.forEach {
      val mockResponse = MockResponse()
        .setResponseCode(HttpURLConnection.HTTP_OK)
        .setBody(it)
      mockWebServer.enqueue(mockResponse)
      val mockUrl = mockWebServer.url("").toString()
      val webService = ClearanceRequests(client, mockLogger, mockUrl, accessTokenRequests)

      val status = runBlocking {
        webService.getClearanceStatuses()
      }

      coVerify { accessTokenRequests.getAccessToken() }

      val recordedRequest: RecordedRequest = mockWebServer.takeRequest()
      assertTrue(recordedRequest.headers["Authorization"] == "Bearer ${accessToken.accessToken}")

      assertTrue(recordedRequest.path!!.endsWith("/fleets/vehicles"))
      assertTrue(recordedRequest.method == "GET")

      assertTrue(status.response!![0].status == ClearanceStatus.Status.PENDING)
      assertTrue(status.response!![0].vin == "C0NNECT0000000001")

      assertTrue(status.response!![1].status == ClearanceStatus.Status.PENDING)
      assertTrue(status.response!![1].vin == "WBADT43452G296404")
    }
  }

  @Test
  fun parsesChangeLog() {
    val mockResponse = MockResponse()
      .setResponseCode(HttpURLConnection.HTTP_OK)
      .setBody(
        CHANGE_LOG.trimIndent()
      )

    mockWebServer.enqueue(mockResponse)
    val mockUrl = mockWebServer.url("").toString()
    val webService = ClearanceRequests(client, mockLogger, mockUrl, accessTokenRequests)

    val status = runBlocking {
      webService.getClearanceStatuses()
    }

    assertTrue(status.response!![0].status == ClearanceStatus.Status.REVOKED)
    assertTrue(status.response!![0].vin == "WBY8P210X07J49112")
    assertTrue(status.response!![0].brand == Brand.BMW)
    assertTrue(status.response!![0].changelog.size == 3)

    assertTrue(status.response!![1].status == ClearanceStatus.Status.APPROVED)
    assertTrue(status.response!![1].vin == "WBY8P210X07J49112")
    assertTrue(status.response!![1].changelog.size == 2)

    assertTrue(status.response!![2].changelog.isEmpty())
    assertTrue(status.response!![2].changelog.isEmpty())
  }

  @Test
  fun getClearanceStatusesAuthTokenError() = runBlocking {
    testAuthTokenErrorReturned(mockWebServer, accessTokenRequests) { mockUrl ->
      val webService = ClearanceRequests(client, mockLogger, mockUrl, accessTokenRequests)
      webService.getClearanceStatuses()
    }
  }

  @Test
  fun getClearanceStatusesErrorResponse() = runBlocking {
    testErrorResponseReturned(mockWebServer) { mockUrl ->
      val webService = ClearanceRequests(client, mockLogger, mockUrl, accessTokenRequests)
      webService.getClearanceStatuses()
    }
  }

  @Test
  fun getClearanceStatusesUnknownResponse() = runBlocking {
    testForUnknownResponseGenericErrorReturned(mockWebServer) { mockUrl ->
      val webService = ClearanceRequests(client, mockLogger, mockUrl, accessTokenRequests)
      webService.getClearanceStatuses()
    }
  }

  // get clearance status

  private val getClearanceStatusSuccessResponses = listOf(
    """
    {
      "brand":"bmw",
      "vin": "C0NNECT0000000001",
      "status": "pending",
      "changelog": [
          {
            "timestamp": "string",
            "status": "approved",
            "unknown" : "value"
          }
      ]
    }
    """.trimIndent(),
    """
    {
      "brand":"bmw",
      "vin": "C0NNECT0000000001",
      "status": "pending",
      "changelog": [
          {
            "timestamp": "string",
            "status": "approved"
          }
      ],
      "unknown" : "value"
    }
    """.trimIndent(),
  )

  @Test
  fun getClearanceStatusSuccessResponse() {
    getClearanceStatusSuccessResponses.forEach {
      val vin = "C0NNECT0000000001"
      val mockResponse = MockResponse()
        .setResponseCode(HttpURLConnection.HTTP_OK)
        .setBody(it)
      mockWebServer.enqueue(mockResponse)
      val mockUrl = mockWebServer.url("").toString()
      val webService = ClearanceRequests(client, mockLogger, mockUrl, accessTokenRequests)

      val status = runBlocking {
        webService.getClearanceStatus(vin)
      }

      coVerify { accessTokenRequests.getAccessToken() }

      val recordedRequest: RecordedRequest = mockWebServer.takeRequest()
      assertTrue(recordedRequest.headers["Authorization"] == "Bearer ${accessToken.accessToken}")

      assertTrue(recordedRequest.path!!.endsWith("/fleets/vehicles/$vin"))
      assertTrue(recordedRequest.method == "GET")

      assertTrue(status.response!!.status == ClearanceStatus.Status.PENDING)
      assertTrue(status.response?.vin == vin)
      assertTrue(status.response?.brand == Brand.BMW)

      assertTrue(status.response?.changelog?.get(0)?.status == ClearanceStatus.Status.APPROVED)
    }
  }

  @Test
  fun getClearanceStatusAuthTokenError() = runBlocking {
    testAuthTokenErrorReturned(mockWebServer, accessTokenRequests) { mockUrl ->
      val webService = ClearanceRequests(client, mockLogger, mockUrl, accessTokenRequests)
      webService.getClearanceStatus("vin")
    }
  }

  @Test
  fun getClearanceStatusErrorResponse() = runBlocking {
    testErrorResponseReturned(mockWebServer) { mockUrl ->
      val webService = ClearanceRequests(client, mockLogger, mockUrl, accessTokenRequests)
      webService.getClearanceStatus("vin")
    }
  }

  @Test
  fun getClearanceStatusUnknownResponse() = runBlocking {
    testForUnknownResponseGenericErrorReturned(mockWebServer) { mockUrl ->
      val webService = ClearanceRequests(client, mockLogger, mockUrl, accessTokenRequests)
      webService.getClearanceStatus("vin")
    }
  }

  // delete clearance

  private val deleteClearanceSuccessResponses = listOf(
    """
    {
      "vin": "C0NNECT0000000001",
      "status": "revoking"
    }
    """.trimIndent(),
    """
    {
      "vin": "C0NNECT0000000001",
      "status": "revoking",
      "unknown" : "value"
    }
  """.trimIndent()
  )

  @Test
  fun deleteClearanceSuccessResponse() {
    val vin = "C0NNECT0000000001"
    deleteClearanceSuccessResponses.forEach {
      val mockResponse = MockResponse()
        .setResponseCode(HttpURLConnection.HTTP_OK)
        .setBody(it)
      mockWebServer.enqueue(mockResponse)
      val mockUrl = mockWebServer.url("").toString()
      val webService = ClearanceRequests(client, mockLogger, mockUrl, accessTokenRequests)

      val status = runBlocking {
        webService.deleteClearance(vin)
      }

      coVerify { accessTokenRequests.getAccessToken() }

      val recordedRequest: RecordedRequest = mockWebServer.takeRequest()
      assertTrue(recordedRequest.path!!.endsWith("/fleets/vehicles/$vin"))

      // verify request
      assertTrue(recordedRequest.headers["Authorization"] == "Bearer ${accessToken.accessToken}")

      // verify response
      assertTrue(status.response!!.status == ClearanceStatus.Status.REVOKING)
      assertTrue(status.response!!.vin == vin)
    }
  }

  @Test
  fun deleteClearanceAuthTokenError() = runBlocking {
    testAuthTokenErrorReturned(mockWebServer, accessTokenRequests) { mockUrl ->
      val webService = ClearanceRequests(client, mockLogger, mockUrl, accessTokenRequests)
      webService.deleteClearance(
        "C0NNECT0000000001"
      )
    }
  }

  @Test
  fun deleteClearanceErrorResponse() = runBlocking {
    testErrorResponseReturned(mockWebServer) { mockUrl ->
      val webService = ClearanceRequests(client, mockLogger, mockUrl, accessTokenRequests)
      webService.deleteClearance(
        "C0NNECT0000000001"
      )
    }
  }

  @Test
  fun deleteClearanceUnknownResponse() = runBlocking {
    testForUnknownResponseGenericErrorReturned(mockWebServer) { mockUrl ->
      val webService = ClearanceRequests(client, mockLogger, mockUrl, accessTokenRequests)
      webService.deleteClearance(
        "C0NNECT0000000001"
      )
    }
  }
}
