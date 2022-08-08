package com.critx.data.network.datasource

import com.critx.commonkotlin.util.Resource
import com.critx.data.datasource.setupstock.SetupStockNetWorkDatasource
import com.critx.data.network.api.SetUpStockService
import com.critx.data.network.dto.SimpleResponse
import com.critx.data.network.dto.SimpleResponseDto
import com.critx.data.network.dto.setupStock.jewelleryCategory.JewelleryCatDto
import com.critx.data.network.dto.setupStock.jewelleryGroup.JewelleryGroupDto
import com.critx.data.network.dto.setupStock.jewelleryQuality.JewelleryQualityData
import com.critx.data.network.dto.setupStock.jewelleryQuality.JewelleryQualityDto
import com.critx.data.network.dto.setupStock.jewelleryType.JewelleryTypeDto
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
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

    override suspend fun getJewelleryQuality(token: String): List<JewelleryQualityData> {
        val response = setUpStockService.getJewelleryQuality(token)
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

    override suspend fun getJewelleryGroup(token: String,frequentUse:Int,firstCatId:Int,secondCatId:Int): JewelleryGroupDto {
        val response = setUpStockService.getJewelleryGroup(token,frequentUse,firstCatId,secondCatId)
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

    override suspend fun createJewelleryGroup(
        token: String,
        image: MultipartBody.Part,
        jewellery_type_id: RequestBody,
        jewellery_quality_id: RequestBody,
        is_frequently_used: RequestBody,
        name: RequestBody
    ): SimpleResponse {
        val response = setUpStockService.createJewelleryGroup(
            token,
           jewellery_type_id, jewellery_quality_id, is_frequently_used, name, image
        )
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

    override suspend fun getJewelleryCategory(
        token: String,
        frequentUse: Int,
        firstCatId: Int,
        secondCatId: Int,
        thirdCatId: Int
    ): JewelleryCatDto{
        val response = setUpStockService.getJewelleryCategory(token,frequentUse,firstCatId,secondCatId,thirdCatId)
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
        }    }
}