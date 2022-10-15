package com.critx.data.network.api

import com.critx.data.network.dto.SimpleResponse
import com.critx.data.network.dto.collectStock.ProductIdResponse
import com.critx.data.network.dto.dailygoldAndPrice.GoldPriceResponse
import retrofit2.Response
import retrofit2.http.*

interface DailyGoldPriceService {
    @GET("api/gold_types")
    suspend fun getGoldPrice(
        @Header("Authorization") token: String,
    ): Response<GoldPriceResponse>

    @FormUrlEncoded
    @POST("api/gold_types")
    suspend fun updateGoldPrice(
        @Header("Authorization") token: String,
        @Field("price[2]") price2:String,
        @Field("price[3]") price3:String
    ): Response<SimpleResponse>
}