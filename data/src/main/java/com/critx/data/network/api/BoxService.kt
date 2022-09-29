package com.critx.data.network.api

import com.critx.data.network.dto.box.BoxScanResponse
import com.critx.data.network.dto.dailygoldAndPrice.GoldPriceResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface BoxService {
    @GET("api/boxes/{boxCode}/scan")
    suspend fun getBoxData(
        @Header("Authorization") token: String,
        @Path("boxCode") boxCode:String
    ): Response<BoxScanResponse>

    @GET("api/products/{productCode}/scan")
    suspend fun getProductData(
        @Header("Authorization") token: String,
        @Path("productCode") productCode:String
    ): Response<BoxScanResponse>
}