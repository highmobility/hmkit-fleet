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
import com.highmobility.hmkitfleet.notExpiredAccessToken
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.net.HttpURLConnection

internal class VehicleDataRequestsTest : BaseTest() {
  val mockWebServer = MockWebServer()
  val client = OkHttpClient()
  val authToken = notExpiredAccessToken()
  val accessTokenRequests = mockk<AccessTokenRequests>()
  val testVin = "C0NNECT0000000000"

  @BeforeEach
  fun setUp() {
    mockWebServer.start()
    coEvery { accessTokenRequests.getAccessToken() } returns Response(authToken)
  }

  @AfterEach
  fun tearDown() {
    mockWebServer.shutdown()
  }

  private val successResponses = listOf(
    """
    {
      "vin": $testVin,
      "status": "vehicle_running"
    }
    """.trimIndent(),
    """
    {
      "vin": $testVin,
      "status": "vehicle_running",
      "unknown": "value"
    }
    """.trimIndent()
  )

  @Test
  fun vehicleStatusSuccess() {
    successResponses.forEach {
      val responseJson = it

      val mockResponse = MockResponse().setResponseCode(HttpURLConnection.HTTP_OK).setBody(responseJson)

      mockWebServer.enqueue(mockResponse)
      val mockUrl = mockWebServer.url("").toString()
      val requests = VehicleDataRequests(client, mockLogger, mockUrl, accessTokenRequests)

      val response = runBlocking {
        requests.getVehicleStatus(testVin)
      }

      coVerify { accessTokenRequests.getAccessToken() }

      val recordedRequest: RecordedRequest = mockWebServer.takeRequest()
      assertTrue(recordedRequest.path!!.endsWith("/vehicle-data/autoapi-13/$testVin"))

      // verify request
      assertTrue(recordedRequest.headers["Authorization"] == "Bearer ${authToken.accessToken}")

      // verify response
      val status = response.response!!
      assertTrue(status == responseJson)
    }
  }

  @Test
  fun vehicleStatusAuthTokenError() = runBlocking {
    testAuthTokenErrorReturned(mockWebServer, accessTokenRequests) { mockUrl ->
      val requests = VehicleDataRequests(client, mockLogger, mockUrl, accessTokenRequests)
      requests.getVehicleStatus(
        testVin,
      )
    }
  }

  @Test
  fun vehicleStatusErrorResponse() = runBlocking {
    testErrorResponseReturned(mockWebServer) { mockUrl ->
      val requests = VehicleDataRequests(client, mockLogger, mockUrl, accessTokenRequests)

      requests.getVehicleStatus(
        testVin,
      )
    }
  }

  @Test
  fun vehicleStatusUnknownResponse() = runBlocking {
    testForUnknownResponseGenericErrorReturned(mockWebServer) { mockUrl ->
      val requests = VehicleDataRequests(client, mockLogger, mockUrl, accessTokenRequests)
      requests.getVehicleStatus(
        testVin,
      )
    }
  }

  @Test
  fun vehicleStaticDataSuccess() {
    successResponses.forEach {
      val responseJson = it

      val mockResponse = MockResponse().setResponseCode(HttpURLConnection.HTTP_OK).setBody(responseJson)

      mockWebServer.enqueue(mockResponse)
      val mockUrl = mockWebServer.url("").toString()
      val requests = VehicleDataRequests(client, mockLogger, mockUrl, accessTokenRequests)

      val response = runBlocking {
        requests.getStaticData(testVin)
      }

      coVerify { accessTokenRequests.getAccessToken() }

      val recordedRequest: RecordedRequest = mockWebServer.takeRequest()
      assertTrue(recordedRequest.path!!.endsWith("/vehicle-static-data/$testVin"))

      // verify request
      assertTrue(recordedRequest.headers["Authorization"] == "Bearer ${authToken.accessToken}")

      // verify response
      val status = response.response!!
      assertTrue(status == responseJson)
    }
  }

  @Test
  fun vehicleStaticDataAuthTokenError() = runBlocking {
    testAuthTokenErrorReturned(mockWebServer, accessTokenRequests) { mockUrl ->
      val requests = VehicleDataRequests(client, mockLogger, mockUrl, accessTokenRequests)
      requests.getStaticData(
        testVin,
      )
    }
  }

  @Test
  fun vehicleStaticDataErrorResponse() = runBlocking {
    testErrorResponseReturned(mockWebServer) { mockUrl ->
      val requests = VehicleDataRequests(client, mockLogger, mockUrl, accessTokenRequests)

      requests.getStaticData(
        testVin,
      )
    }
  }

  @Test
  fun vehicleStaticDataUnknownResponse() = runBlocking {
    testForUnknownResponseGenericErrorReturned(mockWebServer) { mockUrl ->
      val requests = VehicleDataRequests(client, mockLogger, mockUrl, accessTokenRequests)
      requests.getStaticData(
        testVin,
      )
    }
  }
}
