package com.critx.data.network.datasource

import com.critx.data.datasource.repairStock.RepairStockDataSource
import com.critx.data.localdatabase.LocalDatabase
import com.critx.data.network.api.RepairStockService
import com.critx.data.network.dto.SimpleResponseDto
import com.critx.data.network.dto.repairStock.JobDoneResponse
import com.critx.data.network.dto.repairStock.RepairJobDto
import com.critx.data.network.dto.setupStock.jewelleryCategory.error.SimpleError
import com.critx.data.parseError
import com.critx.data.parseErrorWithDataClass
import okhttp3.RequestBody

class RepairStockDataSourceImpl constructor(
    private val repairStockService: RepairStockService,
    private val localDatabase: LocalDatabase
) : RepairStockDataSource {
    override suspend fun getJobDoneData(token: String, goldSmithId: String): JobDoneResponse {
        val response = repairStockService.getJobDoneData(token, goldSmithId)
        return if (response.isSuccessful) {
            response.body() ?: throw Exception("Response body Null")
        } else {
            throw  Exception(
                when (response.code()) {
                    400 -> {
                            val singleError = response.errorBody()?.parseErrorWithDataClass<SimpleError>()
                        if (singleError != null){
                            singleError.response.message
                        }else{
                            val errorMessage =
                                response.errorBody()?.parseError()

                            val list: List<Map.Entry<String, Any>> =
                                ArrayList<Map.Entry<String, Any>>(errorMessage!!.entries)
                            val (key, value) = list[0]
                            value.toString()
                        }
                    }
                    401 -> "You are not Authorized"
                    402 -> "Payment required!!!"
                    403 -> "Forbidden"
                    404 -> "You request not found"
                    405 -> "Method is not allowed!!!"
                    else -> "Unhandled error occurred!!!"
                }
            )
        }
    }

    override suspend fun getRepairJobs(token: String, jewelleryTypeId: String): List<RepairJobDto> {
        val response = repairStockService.getRepairJob(token, jewelleryTypeId)
        return if (response.isSuccessful) {
            response.body()?.data ?: throw Exception("Response body Null")
        } else {
            throw  Exception(
                when (response.code()) {
                    400 -> {
                            val singleError = response.errorBody()?.parseErrorWithDataClass<SimpleError>()
                        if (singleError != null){
                            singleError.response.message
                        }else{
                            val errorMessage =
                                response.errorBody()?.parseError()

                            val list: List<Map.Entry<String, Any>> =
                                ArrayList<Map.Entry<String, Any>>(errorMessage!!.entries)
                            val (key, value) = list[0]
                            value.toString()
                        }
                    }
                    401 -> "You are not Authorized"
                    402 -> "Payment required!!!"
                    403 -> "Forbidden"
                    404 -> "You request not found"
                    405 -> "Method is not allowed!!!"
                    else -> "Unhandled error occurred!!!"
                }
            )
        }    }

    override suspend fun assignGoldSmith(
        token: String,
        goldSmithId: RequestBody,
        jewelleryTypeId: RequestBody,
        repairJobId: RequestBody,
        quantity: RequestBody,
        weightGm: RequestBody
    ): SimpleResponseDto {
        val response = repairStockService.assignGoldSmith(
            token,
            goldSmithId,
            jewelleryTypeId,
            repairJobId,
            quantity,
            weightGm
        )
        return if (response.isSuccessful) {
            response.body()?.response ?: throw Exception("Response body Null")
        } else {
            throw  Exception(
                when (response.code()) {
                    400 -> {
                            val singleError = response.errorBody()?.parseErrorWithDataClass<SimpleError>()
                        if (singleError != null){
                            singleError.response.message
                        }else{
                            val errorMessage =
                                response.errorBody()?.parseError()

                            val list: List<Map.Entry<String, Any>> =
                                ArrayList<Map.Entry<String, Any>>(errorMessage!!.entries)
                            val (key, value) = list[0]
                            value.toString()
                        }
                    }
                    401 -> "You are not Authorized"
                    402 -> "Payment required!!!"
                    403 -> "Forbidden"
                    404 -> "You request not found"
                    405 -> "Method is not allowed!!!"
                    else -> "Unhandled error occurred!!!"
                }
            )
        }
    }

    override suspend fun chargeRepairSTock(
        token: String,
        amount: RequestBody,
        repairStockList: List<RequestBody>
    ): SimpleResponseDto {
        val response = repairStockService.chargeRepairSTock(token, amount, repairStockList)
        return if (response.isSuccessful) {
            response.body()?.response ?: throw Exception("Response body Null")
        } else {
            throw  Exception(
                when (response.code()) {
                    400 -> {
                            val singleError = response.errorBody()?.parseErrorWithDataClass<SimpleError>()
                        if (singleError != null){
                            singleError.response.message
                        }else{
                            val errorMessage =
                                response.errorBody()?.parseError()

                            val list: List<Map.Entry<String, Any>> =
                                ArrayList<Map.Entry<String, Any>>(errorMessage!!.entries)
                            val (key, value) = list[0]
                            value.toString()
                        }
                    }
                    401 -> "You are not Authorized"
                    402 -> "Payment required!!!"
                    403 -> "Forbidden"
                    404 -> "You request not found"
                    405 -> "Method is not allowed!!!"
                    else -> "Unhandled error occurred!!!"
                }
            )
        }
    }

    override suspend fun deleteRepairStock(repairStockId: String): SimpleResponseDto {
        val response = repairStockService.deleteRepairStock(localDatabase.getToken().orEmpty(), repairStockId)
        return if (response.isSuccessful) {
            response.body()?.response ?: throw Exception("Response body Null")
        } else {
            throw  Exception(
                when (response.code()) {
                    400 -> {
                        val errorMessage =
                            response.errorBody()?.parseError()

                        val list: List<Map.Entry<String, Any>> =
                            ArrayList<Map.Entry<String, Any>>(errorMessage!!.entries)
                        val (key, value) = list[0]
                        value.toString()
                    }
                    401 -> "You are not Authorized"
                    402 -> "Payment required!!!"
                    403 -> "Forbidden"
                    404 -> "You request not found"
                    405 -> "Method is not allowed!!!"
                    else -> "Unhandled error occurred!!!"
                }
            )
        }
    }
}