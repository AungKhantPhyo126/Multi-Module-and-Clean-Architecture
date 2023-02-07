package com.critx.data.datasource.repairStock

import com.critx.data.network.dto.SimpleResponse
import com.critx.data.network.dto.SimpleResponseDto
import com.critx.data.network.dto.repairStock.JobDoneResponse
import com.critx.data.network.dto.repairStock.RepairJobDto
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface RepairStockDataSource {
    suspend fun getJobDoneData(
        token: String,
        goldSmithId: String,
    ): JobDoneResponse

    suspend fun getRepairJobs(
        token: String,
        jewelleryTypeId: String,
    ): List<RepairJobDto>


    suspend fun assignGoldSmith(
        token: String,
        goldSmithId: RequestBody,
        jewelleryTypeId: RequestBody,
        repairJobId: RequestBody,
        quantity: RequestBody,
        weightGm: RequestBody,
    ): SimpleResponseDto


    suspend fun chargeRepairSTock(
        token: String,
        amount: RequestBody,
        repairStockList: List<RequestBody>,
    ):SimpleResponseDto

    suspend fun deleteRepairStock(
        repairStockId:String
    ):SimpleResponseDto
}