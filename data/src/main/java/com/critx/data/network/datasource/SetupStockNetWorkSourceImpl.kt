package com.critx.data.network.datasource

import com.critx.commonkotlin.util.Resource
import com.critx.data.datasource.setupstock.SetupStockNetWorkDatasource
import com.critx.data.network.api.SetUpStockService
import com.critx.data.network.dto.SimpleResponse
import com.critx.data.network.dto.SimpleResponseDto
import com.critx.data.network.dto.setupStock.jewelleryCategory.CalculateKPYDto
import com.critx.data.network.dto.setupStock.jewelleryCategory.DesignDto
import com.critx.data.network.dto.setupStock.jewelleryCategory.JewelleryCatDto
import com.critx.data.network.dto.setupStock.jewelleryCategory.error.CreateCategoryError
import com.critx.data.network.dto.setupStock.jewelleryGroup.JewelleryGroupDto
import com.critx.data.network.dto.setupStock.jewelleryQuality.JewelleryQualityData
import com.critx.data.network.dto.setupStock.jewelleryQuality.JewelleryQualityDto
import com.critx.data.network.dto.setupStock.jewelleryType.JewelleryTypeDto
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.Moshi
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import javax.inject.Inject

class SetupStockNetWorkSourceImpl @Inject constructor(
    private val setUpStockService: SetUpStockService,
) : SetupStockNetWorkDatasource {
    override suspend fun getJewelleryType(token: String): JewelleryTypeDto {
        val response = setUpStockService.getJewelleryType(token)
        return if (response.isSuccessful) {
            response.body() ?: throw Exception("Response body Null")
        } else {
            throw  Exception(
                when (response.code()) {
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
        return if (response.isSuccessful) {
            response.body() ?: throw Exception("Response body Null")
        } else {
            throw  Exception(
                when (response.code()) {
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

    override suspend fun getJewelleryGroup(
        token: String,
        frequentUse: Int,
        firstCatId: Int,
        secondCatId: Int
    ): JewelleryGroupDto {
        val response =
            setUpStockService.getJewelleryGroup(token, frequentUse, firstCatId, secondCatId)
        return if (response.isSuccessful) {
            response.body() ?: throw Exception("Response body Null")
        } else {
            throw  Exception(
                when (response.code()) {
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
        return if (response.isSuccessful) {
            response.body() ?: throw Exception("Response body Null")
        } else {
            throw  Exception(
                when (response.code()) {
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
    ): JewelleryCatDto {
        val response = setUpStockService.getJewelleryCategory(
            token,
            frequentUse,
            firstCatId,
            secondCatId,
            thirdCatId
        )
        return if (response.isSuccessful) {
            response.body() ?: throw Exception("Response body Null")
        } else {
            throw  Exception(
                when (response.code()) {
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

    override suspend fun createJewelleryCategory(
        token: String,
        jewellery_type_id: RequestBody,
        jewellery_quality_id: RequestBody,
        groupId: RequestBody,
        is_frequently_used: RequestBody,
        name: RequestBody,
        avgWeigh: RequestBody,
        avgWastage: RequestBody,
        images: MutableList<MultipartBody.Part>,
        video: MultipartBody.Part,
        specification: RequestBody,
        design: MutableList<RequestBody>,
        orderToGs: RequestBody
    ): SimpleResponse {
        val response = setUpStockService.createJewelleryCategory(
            token,
            jewellery_type_id, jewellery_quality_id, groupId, is_frequently_used, name, avgWeigh,
            avgWastage, images.toList(), video, specification, design.toList(), orderToGs
        )
        return if (response.isSuccessful) {
            response.body() ?: throw Exception("Response body Null")
        } else {
            throw  Exception(
                when (response.code()) {
                    400 -> {
                        response.errorBody()?.parseError<CreateCategoryError>()
                        "Bad request"
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

    override suspend fun calculateKPYtoGram(
        token: String,
        kyat: Double,
        pae: Double,
        ywae: Double
    ): CalculateKPYDto {
        val response = setUpStockService.calculateKPYtoGram(
            token, kyat, pae, ywae
        )
        return if (response.isSuccessful) {
            response.body() ?: throw Exception("Response body Null")
        } else {
            throw  Exception(
                when (response.code()) {
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

    override suspend fun getDesign(token: String): DesignDto {
        val response = setUpStockService.getDesignList(
            token)
        return if (response.isSuccessful) {
            response.body() ?: throw Exception("Response body Null")
        } else {
            throw  Exception(
                when (response.code()) {
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


inline fun <reified T> ResponseBody.parseError(): T? {
    val moshi = Moshi.Builder().build()
    val parser = moshi.adapter(T::class.java)
    val response = this.string()
    try {
        return parser.fromJson(response)
    } catch (e: JsonDataException) {
        e.printStackTrace()
    }
    return null
}