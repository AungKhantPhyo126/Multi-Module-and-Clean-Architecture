package com.critx.data.network.api

import com.critx.data.network.dto.SimpleResponse
import com.critx.data.network.dto.collectStock.ProductIdResponse
import com.critx.data.network.dto.dailygoldAndPrice.GoldPriceResponse
import com.critx.data.network.dto.dailygoldAndPrice.RebuyPriceResponse
import retrofit2.Response
import retrofit2.http.*

interface DailyGoldPriceService {
    @GET("api/gold_types")
    suspend fun getGoldPrice(
        @Header("Authorization") token: String,
    ): Response<GoldPriceResponse>

    @FormUrlEncoded
    @POST("api/gold_prices/update")
    suspend fun updateGoldPrice(
        @Header("Authorization") token: String,
        @FieldMap price :HashMap<String,String>
    ): Response<SimpleResponse>

    @GET("api/rebuy_prices")
    suspend fun getRebuyGoldPrice(
        @Header("Authorization") token: String,
    ): Response<RebuyPriceResponse>

    @FormUrlEncoded
    @POST("api/rebuy_prices")
    suspend fun updateRebuyPrice(
        @Header("Authorization") token: String,
        @FieldMap horizontal_option_name :HashMap<String,String>,
        @FieldMap vertical_option_name :HashMap<String,String>,
        @FieldMap horizontal_option_level :HashMap<String,String>,
        @FieldMap vertical_option_level :HashMap<String,String>,
        @FieldMap size :HashMap<String,String>,
        @FieldMap price :HashMap<String,String>,
    ): Response<SimpleResponse>
}