package com.co.mercapp.data.products.datasource.remote

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface ProductsApiService {

    @GET("/sites/{sideId}/search")
    suspend fun searchByName(
        @Path("sideId") sideId: String,
        @QueryMap query: HashMap<String, Any>
    ): Response<JsonObject>

    @GET("/items")
    suspend fun getDetails(@Query("ids") id: String): Response<JsonArray>

}