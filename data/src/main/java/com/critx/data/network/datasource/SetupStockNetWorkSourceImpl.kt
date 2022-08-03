package com.critx.data.network.datasource

import com.critx.data.datasource.setupstock.SetupStockNetWorkDatasource
import com.critx.data.network.api.SetUpStockService
import com.critx.data.network.dto.setupStock.jewelleryType.JewelleryTypeDto
import javax.inject.Inject

class SetupStockNetWorkSourceImpl @Inject constructor(
    private val setUpStockService: SetUpStockService
) :SetupStockNetWorkDatasource{
    override suspend fun getJewelleryType(token: String): JewelleryTypeDto {
        val response = setUpStockService.getJewelleryType(token)
        return  if (response.isSuccessful){
            response.body()?:throw Exception("Response body Null")
        }else{
            throw  Exception(
                when(response.code()){
                    400 -> "Bad request"
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