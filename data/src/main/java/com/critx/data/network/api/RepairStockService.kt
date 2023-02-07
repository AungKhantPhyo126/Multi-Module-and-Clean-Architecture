package com.critx.data.network.api

import com.critx.data.network.dto.SimpleResponse
import com.critx.data.network.dto.giveGold.GoldBoxResponse
import com.critx.data.network.dto.repairStock.JobDoneResponse
import com.critx.data.network.dto.repairStock.RepairJobResponse
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface RepairStockService {
    @GET("api/repair_stocks")
    suspend fun getJobDoneData(
        @Header("Authorization") token: String,
        @Query("goldsmith_id") goldSmithId:String,
    ): Response<JobDoneResponse>

    @GET("api/repair_jobs/quicklist")
    suspend fun getRepairJob(
        @Header("Authorization") token: String,
        @Query("jewellery_type_id") jewelleryTypeId:String,
    ): Response<RepairJobResponse>

    @Multipart
    @POST("api/repair_stocks")
    suspend fun assignGoldSmith(
        @Header("Authorization") token: String,
        @Part("goldsmith_id") goldSmithId:RequestBody,
        @Part("jewellery_type_id") jewelleryTypeId:RequestBody,
        @Part("repair_job_id") repairJobId:RequestBody,
        @Part("quantity") quantity:RequestBody,
        @Part("weight_gm") weightGm:RequestBody,
    ): Response<SimpleResponse>


    @JvmSuppressWildcards
    @Multipart
    @POST("api/repair_stocks/charge")
    suspend fun chargeRepairSTock(
        @Header("Authorization") token: String,
        @Part("amount") amount:RequestBody,
        @Part("repair_stocks[]") repairStockList:List<RequestBody>,
    ): Response<SimpleResponse>

    @POST("api/repair_stocks/{repairStockId}/delete")
    suspend fun deleteRepairStock(
        @Header("Authorization") token: String,
        @Path("repairStockId") repairStockId:String
    ): Response<SimpleResponse>
}