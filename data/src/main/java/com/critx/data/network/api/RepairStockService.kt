package com.critx.data.network.api

import com.critx.data.network.dto.SimpleResponse
import com.critx.data.network.dto.giveGold.GoldBoxResponse
import com.critx.data.network.dto.repairStock.JobDoneResponse
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface RepairStockService {
    @FormUrlEncoded
    @GET("api/repair_stocks")
    suspend fun getJobDoneData(
        @Header("Authorization") token: String,
        @Query("goldsmith_id") goldSmithId:String,
    ): Response<JobDoneResponse>

    @Multipart
    @FormUrlEncoded
    @POST("api/repair_stocks")
    suspend fun assignGoldSmith(
        @Header("Authorization") token: String,
        @Part("goldsmith_id") goldSmithId:RequestBody,
        @Part("jewellery_type_id") jewelleryTypeId:RequestBody,
        @Part("repair_job_id") repairJobId:RequestBody,
        @Part("quantity") quantity:RequestBody,
        @Part("weight_gm") weightGm:RequestBody,
    ): Response<SimpleResponse>


    @Multipart
    @FormUrlEncoded
    @POST("api/repair_stocks")
    suspend fun chargeRepairSTock(
        @Header("Authorization") token: String,
        @Part("amount") goldSmithId:RequestBody,
        @Part("repair_stocks[]") repairStockList:List<RequestBody>,
    ): Response<SimpleResponse>
}