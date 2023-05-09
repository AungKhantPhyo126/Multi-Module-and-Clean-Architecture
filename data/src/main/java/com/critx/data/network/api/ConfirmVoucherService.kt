package com.critx.data.network.api

import com.critx.data.network.dto.DiscountVoucherScanResponse
import com.critx.data.network.dto.ScanVoucherToConfirmResponse
import com.critx.data.network.dto.SimpleResponse
import com.critx.data.network.dto.StockInVoucherResponse
import com.critx.data.network.dto.UnConfirmVoucherResponse
import com.critx.data.network.dto.flashSales.UserPointsResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
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
    ): Response<UnConfirmVoucherResponse>

    @GET("api/vouchers/{voucherCode}/get-stocks-info")
    suspend fun getStockInVoucher(
        @Header("Authorization") token: String,
        @Path("voucherCode") voucherCode: String,
    ): Response<StockInVoucherResponse>

    @POST("api/vouchers/{voucherCode}/confirm")
    suspend fun confirmVoucher(
        @Header("Authorization") token: String,
        @Path("voucherCode") voucherCode: String,
    ): Response<SimpleResponse>

    @GET("api/vouchers/{voucherCode}/view")
    suspend fun scanVoucherToConfirm(
        @Header("Authorization") token: String,
        @Path("voucherCode") voucherCode: String,
    ): Response<ScanVoucherToConfirmResponse>

    @GET("api/vouchers/{voucherCode}/scan")
    suspend fun scanDiscountVoucher(
        @Header("Authorization") token: String,
        @Path("voucherCode") voucherCode: String,
    ): Response<DiscountVoucherScanResponse>

    @POST("api/vouchers/add-to-discount")
    @FormUrlEncoded
    suspend fun addDiscount(
        @Header("Authorization") token: String,
        @Field("voucher_id[]") voucherCode: List<String>,
        @Field("amount") amount:String,
    ): Response<SimpleResponse>
}