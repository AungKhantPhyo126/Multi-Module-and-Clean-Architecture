package com.critx.data.network.api

import com.critx.data.network.dto.SimpleResponse
import com.critx.data.network.dto.SimpleResponseWithData
import com.critx.data.network.dto.sampleTakeAndReturn.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
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
    ): Response<OutsideSampleResponse>

    @GET("api/samples/inventory/list")
    suspend fun getInventorySample(
        @Header("Authorization") token: String,
    ): Response<OutsideSampleResponse>


//    @GET("api/sales/{invoiceId}/check-samples")
@GET("api/products/{invoiceId}/check-samples")
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

    @POST("api/samples/{sample_id}/remove-handed-list")
    suspend fun removeFromHandedList(
        @Header("Authorization") token: String,
        @Path("sample_id") sampleId: String
    ): Response<SimpleResponse>

    @FormUrlEncoded
    @POST("api/samples/return")
    suspend fun returnSample(
        @Header("Authorization") token: String,
        @Field("sample_id[]") sampleId: List<String>
    ): Response<SimpleResponse>


    @Multipart
    @POST("api/samples/outside/take")
    suspend fun saveOutsideSample(
        @Header("Authorization") token: String,
        @Part("name") name: RequestBody?,
        @Part("weight_gm") weight_gm: RequestBody?,
        @Part("specification") specification: RequestBody?,
        @Part image: MultipartBody.Part
    ):Response<SampleCheckDto>
}