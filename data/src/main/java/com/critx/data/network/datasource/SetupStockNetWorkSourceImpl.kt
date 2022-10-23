package com.critx.data.network.datasource

import com.critx.commonkotlin.util.Resource
import com.critx.data.datasource.setupstock.SetupStockNetWorkDatasource
import com.critx.data.network.api.SetUpStockService
import com.critx.data.network.dto.SimpleResponse
import com.critx.data.network.dto.SimpleResponseDto
import com.critx.data.network.dto.setupStock.ProductCodeResponse
import com.critx.data.network.dto.setupStock.jewelleryCategory.*
import com.critx.data.network.dto.setupStock.jewelleryCategory.error.CreateCategoryError
import com.critx.data.network.dto.setupStock.jewelleryCategory.error.getMessage
import com.critx.data.network.dto.setupStock.jewelleryGroup.Data
import com.critx.data.network.dto.setupStock.jewelleryGroup.JewelleryGroupDto
import com.critx.data.network.dto.setupStock.jewelleryQuality.JewelleryQualityData
import com.critx.data.network.dto.setupStock.jewelleryQuality.JewelleryQualityDto
import com.critx.data.network.dto.setupStock.jewelleryType.JewelleryTypeDto
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
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
                    400 -> {
                        response.errorBody()?.string()?:"Bad request"
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

    override suspend fun getJewelleryQuality(token: String): List<JewelleryQualityData> {
        val response = setUpStockService.getJewelleryQuality(token)
        return if (response.isSuccessful) {
            response.body() ?: throw Exception("Response body Null")
        } else {
            throw  Exception(
                when (response.code()) {
                    400 -> response.errorBody()?.string()?:"Bad request"
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
                    400 -> response.errorBody()?.string()?:"Bad request"
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
    ): Data {
        val response = setUpStockService.createJewelleryGroup(
            token,
            jewellery_type_id, jewellery_quality_id, is_frequently_used, name, image
        )
        return if (response.isSuccessful) {
            response.body()?.data ?: throw Exception("Response body Null")
        } else {
            throw  Exception(
                when (response.code()) {
                    400 -> {
                        response.errorBody()?.parseError<CreateCategoryError>()
                        response.errorBody()?.string()?:"Bad request"
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

    override suspend fun editJewelleryGroup(
        token: String,
        method: RequestBody,
        groupId: String,
        image: MultipartBody.Part,
        jewellery_type_id: RequestBody,
        jewellery_quality_id: RequestBody,
        is_frequently_used: RequestBody,
        name: RequestBody
    ): SimpleResponse {
        val response = setUpStockService.editJewelleryGroup(
            token, method, groupId,
            jewellery_type_id, jewellery_quality_id, is_frequently_used, name, image
        )
        return if (response.isSuccessful) {
            response.body() ?: throw Exception("Response body Null")
        } else {
            throw  Exception(
                when (response.code()) {
                    400 -> {
                        response.errorBody()?.parseError<CreateCategoryError>()
                        response.errorBody()?.string()?:"Bad request"
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

    override suspend fun deleteJewelleryGroup(
        token: String,
        method: RequestBody,
        groupId: String
    ): SimpleResponse {
        val response = setUpStockService.deleteJewelleryGroup(
            token, method, groupId,
        )
        return if (response.isSuccessful) {
            response.body() ?: throw Exception("Response body Null")
        } else {
            throw  Exception(
                when (response.code()) {
                    400 -> {
                        response.errorBody()?.parseError<CreateCategoryError>()
                        response.errorBody()?.string()?:"Bad request"
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

    override suspend fun deleteJewelleryCategory(
        token: String,
        method: RequestBody,
        catId: String
    ): SimpleResponse {
        val response = setUpStockService.deleteJewelleryCategory(
            token, method, catId
        )
        return if (response.isSuccessful) {
            response.body() ?: throw Exception("Response body Null")
        } else {
            throw  Exception(
                when (response.code()) {
                    400 -> {
                        response.errorBody()?.string()?:"Bad Request"
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

    override suspend fun getJewelleryCategory(
        token: String,
        frequentUse: Int?,
        firstCatId: Int?,
        secondCatId: Int?,
        thirdCatId: Int?
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
                    400 -> response.errorBody()?.string()?:"Bad request"
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

    override suspend fun getRelatedJewelleryCategories(
        token: String,
        categoryId: String
    ): JewelleryCatDto {
        val response =
            setUpStockService.getRelatedCat(token, categoryId)
        return if (response.isSuccessful) {
            response.body() ?: throw Exception("Response body Null")
        } else {
            throw  Exception(
                when (response.code()) {
                    400 -> response.errorBody()?.string()?:"Bad request"
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
        withGem:RequestBody,
        name: RequestBody,
        avgWeigh: RequestBody,
        avgKyat: RequestBody,
        avgPae: RequestBody,
        avgYwae: RequestBody,
        images: MutableList<MultipartBody.Part>,
        video: MultipartBody.Part?,
        specification: RequestBody,
        design: MutableList<RequestBody>,
        orderToGs: RequestBody,
        recommendCat: MutableList<RequestBody>?
    ): JewelleryCatCreatedData {
        val response = setUpStockService.createJewelleryCategory(
            token,
            jewellery_type_id,
            jewellery_quality_id,
            groupId,
            is_frequently_used,
            withGem,
            name,
            avgWeigh,
            avgKyat,
            avgPae,
            avgYwae,
            images.toList(),
            video,
            specification,
            design.toList(),
            orderToGs,
            recommendCat
        )
        return if (response.isSuccessful) {
            response.body() ?: throw Exception("Response body Null")
        } else {
            throw  Exception(
                when (response.code()) {
                    400 -> {
                        response.errorBody()?.string()?:"Bad request"
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

    override suspend fun editJewelleryCategory(
        token: String,
        method: RequestBody,
        categoryId: String,
        jewellery_type_id: RequestBody,
        jewellery_quality_id: RequestBody,
        groupId: RequestBody,
        is_frequently_used: RequestBody,
        withGem: RequestBody,
        name: RequestBody,
        avgWeigh: RequestBody,
        avgKyat: RequestBody,
        avgPae: RequestBody,
        avgYwae: RequestBody, images: MutableList<MultipartBody.Part>,
        video: MultipartBody.Part?,
        specification: RequestBody,
        design: MutableList<RequestBody>,
        orderToGs: RequestBody,
        recommendCat: MutableList<RequestBody>
    ): SimpleResponse {
        val response = setUpStockService.editJewelleryCategory(
            token,
            categoryId,
            method,
            jewellery_type_id,
            jewellery_quality_id,
            groupId,
            is_frequently_used,
            withGem,
            name,
            avgWeigh,
            avgKyat,
            avgPae,
            avgYwae,
            images.toList(),
            video,
            specification,
            design.toList(),
            orderToGs,
            recommendCat
        )
        return if (response.isSuccessful) {
            response.body() ?: throw Exception("Response body Null")
        } else {
            val gson = Gson()
            val type = object : TypeToken<CreateCategoryError>() {}.type
            var errorResponse: CreateCategoryError? =
                gson.fromJson(response.errorBody()!!.charStream(), type)
            throw  Exception(

                when (response.code()) {
                    400 -> {
                        response.errorBody()?.string()?:"Bad request"
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
                    400 -> response.errorBody()?.string()?:"Bad request"
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

    override suspend fun getDesign(token: String,jewelleryType:String): DesignDto {
        val response = setUpStockService.getDesignList(
            token,
            jewelleryType
        )
        return if (response.isSuccessful) {
            response.body() ?: throw Exception("Response body Null")
        } else {
            throw  Exception(
                when (response.code()) {
                    400 -> response.errorBody()?.string()?:"Bad request"
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

    override suspend fun createProduct(
        token: String,
        name: RequestBody,
        productCode: RequestBody,
        type: RequestBody,
        quality: RequestBody,
        group: RequestBody,
        categoryId: RequestBody,
        goldAndGemWeight: RequestBody,
        gemWeightKyat: RequestBody,
        gemWeightPae: RequestBody,
        gemWeightYwae: RequestBody,
        gemValue: RequestBody?,
        ptAndClipCost: RequestBody?,
        maintenanceCost: RequestBody?,
        diamondInfo: RequestBody?,
        diamondPriceFromGS: RequestBody?,
        diamondValueFromGS: RequestBody?,
        diamondPriceForSale: RequestBody?,
        diamondValueForSale: RequestBody?,
        images: List<MultipartBody.Part>,
        video: MultipartBody.Part?
    ): SimpleResponse {
        val response = setUpStockService.createProduct(
            token,
            name,
            productCode,
            type,
            quality,
            group,
            categoryId,
            goldAndGemWeight,
            gemWeightKyat,
            gemWeightPae,
            gemWeightYwae,
            gemValue,
            ptAndClipCost,
            maintenanceCost,
            diamondInfo,
            diamondPriceFromGS,
            diamondValueFromGS,
            diamondPriceForSale,
            diamondValueForSale,
            images,
            video
        )
        return if (response.isSuccessful) {
            response.body() ?: throw Exception("Response body Null")
        } else {
            throw  Exception(
                when (response.code()) {
                    400 -> {
                       getErrorString(response.errorBody()?.parseError<CreateCategoryError>()?.response?.message?.getMessage()!!)
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

    override suspend fun getProductCode(token: String): ProductCodeResponse {
        val response = setUpStockService.getProductCode(
            token
        )
        return if (response.isSuccessful) {
            response.body() ?: throw Exception("Response body Null")
        } else {
            throw  Exception(
                when (response.code()) {
                    400 -> {
                        response.errorBody()?.parseError<CreateCategoryError>()
                        response.errorBody()?.string()?:"Bad request"
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

fun getErrorString(errorList:List<String?>):String{
    val gg = errorList.filterNotNull()
    return gg[0]
}