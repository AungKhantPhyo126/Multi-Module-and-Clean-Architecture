package com.critx.data.network.api

import com.critx.data.network.dto.SimpleResponse
import com.critx.data.network.dto.collectStock.ProductIdListResponse
import com.critx.data.network.dto.collectStock.ProductIdResponse
import com.critx.data.network.dto.setupStock.ProductCodeResponse
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface CollectStockService {

    @GET("api/products/update/weight_with_label/{productCode}")
    suspend fun getProductId(
        @Header("Authorization") token: String,
        @Path("productCode") productCode:String
    ): Response<ProductIdResponse>

    @Multipart
    @POST("api/products/update/weight_with_label/{productCode}")
    suspend fun singleStockUpdate(
        @Header("Authorization") token: String,
        @Path("productCode") productCode:String,
        @Part("weight_including_label_gm") weight:RequestBody
    ): Response<SimpleResponse>

    @GET("api/scan/product_codes")
    suspend fun getProductIdList(
        @Header("Authorization") token: String,
        @Part("product_codes[]") productCodeList:List<RequestBody>
    ):Response<ProductIdListResponse>
}