package com.critx.data.network.api

import com.critx.data.network.dto.SimpleResponse
import com.critx.data.network.dto.SimpleResponseDto
import com.critx.data.network.dto.box.BoxScanResponse
import com.critx.data.network.dto.flashSales.UserPointsResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface FlashSaleService {

    @POST("api/flash-sale-campaigns")
    @Multipart
    @JvmSuppressWildcards
    suspend fun addToFlashSale(
        @Header("Authorization") token: String,
        @Part("title") title:RequestBody,
        @Part("discount_amount") discount_amount:RequestBody,
        @Part("time_from") time_from:RequestBody,
        @Part("time_to") time_to:RequestBody,
        @Part("product_id[]") productIds:List<RequestBody>,
        @Part image:MultipartBody.Part,
    ): Response<SimpleResponse>

    @GET("api/users/{userId}/points")
    suspend fun getUserPoint(
        @Header("Authorization") token: String,
        ):Response<UserPointsResponse>

    @POST("api/points/manual-process")
    @Multipart
    suspend fun manualPointsAddOrReduce(
        @Header("Authorization") token: String,
        @Part("user_id") title:RequestBody,
        @Part("point") discount_amount:RequestBody,
        @Part("reason") time_from:RequestBody,
        @Part("action") time_to:RequestBody,
    ): Response<SimpleResponse>
}