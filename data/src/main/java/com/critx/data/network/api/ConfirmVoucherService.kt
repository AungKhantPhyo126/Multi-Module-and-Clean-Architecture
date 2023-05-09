package com.critx.data.network.api

import com.critx.data.network.dto.flashSales.UserPointsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ConfirmVoucherService {
    @GET("api/vouchers")
    suspend fun getVouchers(
        @Header("Authorization") token: String,
        @Query("type") type: String,
    ): Response<String>

    @GET("api/vouchers/{voucherCode}/get-stocks-info")
    suspend fun getStockInVoucher(
        @Header("Authorization") token: String,
        @Path("voucherCode") voucherCode: String,
    ): Response<String>

    @POST("api/vouchers/{voucherCode}/confirm")
    suspend fun confirmVoucher(
        @Header("Authorization") token: String,
        @Path("voucherCode") voucherCode: String,
    ): Response<String>
}