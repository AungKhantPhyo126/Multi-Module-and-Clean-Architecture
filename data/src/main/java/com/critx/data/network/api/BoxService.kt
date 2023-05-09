package com.critx.data.network.api

import com.critx.data.network.dto.SimpleResponse
import com.critx.data.network.dto.box.BoxScanResponse
import com.critx.data.network.dto.box.BoxWeightResponse
import com.critx.data.network.dto.dailygoldAndPrice.GoldPriceResponse
import retrofit2.Response
import retrofit2.http.*

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


    @FormUrlEncoded
    @POST("api/boxes/check-up-weight")
    suspend fun getBoxWeight(
        @Header("Authorization") token: String,
        @Field("box_id[]") boxIdList:List<String>
    ):Response<BoxWeightResponse>

    @FormUrlEncoded
    @JvmSuppressWildcards
    @POST("api/boxes/arrange")
    suspend fun arrangeBox(
        @Header("Authorization") token: String,
        @Field("boxes[]") boxIdList:List<String>
    ):Response<SimpleResponse>


}