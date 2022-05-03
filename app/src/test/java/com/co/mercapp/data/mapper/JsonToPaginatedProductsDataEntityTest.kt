package com.co.mercapp.data.mapper

import com.co.mercapp.data.products.datasource.remote.mapper.JsonToPaginatedProductsData
import com.co.mercapp.data.products.extensions.toJsonObject
import com.co.mercapp.domain.base.Result
import com.google.gson.JsonObject
import org.junit.Assert
import org.junit.Test

class JsonToPaginatedProductsDataEntityTest {

    @Test
    fun unsuccessfulResponseCode() {
        val httpNotFound = 404
        val result =
            JsonToPaginatedProductsData.map(responseCode = httpNotFound, json = JsonObject())
        Assert.assertTrue(
            result is Result.Failure && checkFailureResponseCode(
                result.error,
                httpNotFound
            )
        )
    }

    @Test
    fun invalidResponseFormat() {
        val httpSuccessful = 200
        val result =
            JsonToPaginatedProductsData.map(responseCode = httpSuccessful, json = JsonObject())
        Assert.assertTrue(
            result is Result.Failure && checkFailureResponseCode(result.error, httpSuccessful)
        )
    }

    @Test
    fun invalidPagingValuesResponse() {
        val httpSuccessful = 200
        val json = "{\"paging\":{},\"results\":[]}"
        val result =
            JsonToPaginatedProductsData.map(
                responseCode = httpSuccessful,
                json = json.toJsonObject() ?: JsonObject()
            )
        Assert.assertTrue(
            result is Result.Failure && checkFailureResponseCode(
                result.error,
                httpSuccessful
            )
        )
    }

    @Test
    fun successEmptyResponse() {
        val httpSuccessful = 200
        val json = "{\"paging\":{\"total\":1,\"offset\":0,\"limit\":25},\"results\":[]}"

        val result =
            JsonToPaginatedProductsData.map(
                responseCode = httpSuccessful,
                json = json.toJsonObject() ?: JsonObject()
            )
        Assert.assertTrue(result is Result.Success && result.data.isEmpty())
    }

}