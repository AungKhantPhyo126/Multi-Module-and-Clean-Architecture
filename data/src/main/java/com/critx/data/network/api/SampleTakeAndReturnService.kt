package com.critx.data.network.api

import com.critx.data.network.dto.SimpleResponse
import com.critx.data.network.dto.sampleTakeAndReturn.HandedListResponse
import com.critx.data.network.dto.sampleTakeAndReturn.SampleCheckResponse
import com.critx.data.network.dto.sampleTakeAndReturn.VoucherSampleResponse
import com.critx.data.network.dto.sampleTakeAndReturn.VoucherScanResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface SampleTakeAndReturnService {

    @GET("api/sales/{invoiceCode}/scan")
    suspend fun scanInvoice(
        @Header("Authorization") token: String,
        @Path("invoiceCode") invoiceCode: String
    ): Response<VoucherScanResponse>

    @GET("api/samples/{invoiceId}")
    suspend fun getVoucherSamples(
        @Header("Authorization") token: String,
        @Path("invoiceId") invoiceId: String
    ): Response<VoucherSampleResponse>

    @GET("api/samples/outside/list")
    suspend fun getOutsideSample(
        @Header("Authorization") token: String,
    ): Response<VoucherSampleResponse>


    @GET("api/sales/{invoiceId}/check-samples")
    suspend fun checkSamples(
        @Header("Authorization") token: String,
        @Path("invoiceId") invoiceId: String
    ): Response<SampleCheckResponse>

    @FormUrlEncoded
    @POST("api/samples/inventory/take")
    suspend fun saveSample(
        @Header("Authorization") token: String,
        @FieldMap sample: HashMap<String, String>
    ): Response<SimpleResponse>

    @GET("api/samples/handed-list")
    suspend fun getHandedList(
        @Header("Authorization") token: String,
    ): Response<HandedListResponse>


    @FormUrlEncoded
    @POST("api/samples/add/handed-list")
    suspend fun addToHandedList(
        @Header("Authorization") token: String,
        @Field("sample_id[]") sampleId: List<String>
    ): Response<SimpleResponse>

    @FormUrlEncoded
    @POST("api/samples/1/remove-handed-list")
    suspend fun removeFromHandedList(
        @Header("Authorization") token: String,
        @Field("sample_id[]") sampleId: List<String>
    ): Response<SimpleResponse>

    @FormUrlEncoded
    @POST("api/samples/return")
    suspend fun returnSample(
        @Header("Authorization") token: String,
        @Field("sample_id[]") sampleId: List<String>
    ): Response<SimpleResponse>


    @FormUrlEncoded
    @Multipart
    @POST("api/samples/outside/take")
    suspend fun saveOutsideSample(
        @Header("Authorization") token: String,
        @Field("name") name: String,
        @Field("weight_gm") weight_gm: String,
        @Field("specification") specification: String,
        @Part image: MultipartBody.Part
    ):Response<SimpleResponse>
}