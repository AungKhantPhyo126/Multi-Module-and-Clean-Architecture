package com.critx.data.network.datasource

import com.critx.data.datasource.setupstock.SetupStockNetWorkDatasource
import com.critx.data.network.api.SetUpStockService
import com.critx.data.network.dto.SimpleResponse
import com.critx.data.network.dto.SimpleResponseDto
import com.critx.data.network.dto.setupStock.ProductCodeResponse
import com.critx.data.network.dto.setupStock.ProductSingleDto
import com.critx.data.network.dto.setupStock.jewelleryCategory.*
import com.critx.data.network.dto.setupStock.jewelleryCategory.error.CreateCategoryError
import com.critx.data.network.dto.setupStock.jewelleryCategory.error.SimpleError
import com.critx.data.network.dto.setupStock.jewelleryGroup.Data
import com.critx.data.network.dto.setupStock.jewelleryGroup.JewelleryGroupDto
import com.critx.data.network.dto.setupStock.jewelleryQuality.JewelleryQualityData
import com.critx.data.network.dto.setupStock.jewelleryType.JewelleryTypeDto
import com.critx.data.parseError
import com.critx.data.parseErrorWithDataClass
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.MultipartBody
import okhttp3.RequestBody
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
                    500 -> {
                        "Unhandled error occurred!!!"
                    }
                    else -> {
                        val errorJsonString = response.errorBody()?.string().orEmpty()
                        val singleError =
                            response.errorBody()
                                ?.parseErrorWithDataClass<SimpleError>(errorJsonString)
                        if (singleError != null) {
                            singleError.response.message
                        } else {
                            val errorMessage =
                                response.errorBody()?.parseError(errorJsonString)
                            val list: List<Map.Entry<String, Any>> =
                                ArrayList<Map.Entry<String, Any>>(errorMessage!!.entries)
                            val (key, value) = list[0]
                            value.toString()
                        }
                    }
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
                    500 -> {
                        "Unhandled error occurred!!!"
                    }
                    else -> {
                        val errorJsonString = response.errorBody()?.string().orEmpty()
                        val singleError =
                            response.errorBody()
                                ?.parseErrorWithDataClass<SimpleError>(errorJsonString)
                        if (singleError != null) {
                            singleError.response.message
                        } else {
                            val errorMessage =
                                response.errorBody()?.parseError(errorJsonString)
                            val list: List<Map.Entry<String, Any>> =
                                ArrayList<Map.Entry<String, Any>>(errorMessage!!.entries)
                            val (key, value) = list[0]
                            value.toString()
                        }
                    }
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
                    500 -> {
                        "Unhandled error occurred!!!"
                    }
                    else -> {
                        val errorJsonString = response.errorBody()?.string().orEmpty()
                        val singleError =
                            response.errorBody()
                                ?.parseErrorWithDataClass<SimpleError>(errorJsonString)
                        if (singleError != null) {
                            singleError.response.message
                        } else {
                            val errorMessage =
                                response.errorBody()?.parseError(errorJsonString)
                            val list: List<Map.Entry<String, Any>> =
                                ArrayList<Map.Entry<String, Any>>(errorMessage!!.entries)
                            val (key, value) = list[0]
                            value.toString()
                        }
                    }
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
                    500 -> {
                        "Unhandled error occurred!!!"
                    }
                    else -> {
                        val errorJsonString = response.errorBody()?.string().orEmpty()
                        val singleError =
                            response.errorBody()
                                ?.parseErrorWithDataClass<SimpleError>(errorJsonString)
                        if (singleError != null) {
                            singleError.response.message
                        } else {
                            val errorMessage =
                                response.errorBody()?.parseError(errorJsonString)
                            val list: List<Map.Entry<String, Any>> =
                                ArrayList<Map.Entry<String, Any>>(errorMessage!!.entries)
                            val (key, value) = list[0]
                            value.toString()
                        }
                    }
                }
            )
        }
    }

    override suspend fun editJewelleryGroup(
        token: String,
        method: RequestBody,
        groupId: String,
        image: MultipartBody.Part?,
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
                    500 -> {
                        "Unhandled error occurred!!!"
                    }
                    else -> {
                        val errorJsonString = response.errorBody()?.string().orEmpty()
                        val singleError =
                            response.errorBody()
                                ?.parseErrorWithDataClass<SimpleError>(errorJsonString)
                        if (singleError != null) {
                            singleError.response.message
                        } else {
                            val errorMessage =
                                response.errorBody()?.parseError(errorJsonString)
                            val list: List<Map.Entry<String, Any>> =
                                ArrayList<Map.Entry<String, Any>>(errorMessage!!.entries)
                            val (key, value) = list[0]
                            value.toString()
                        }
                    }
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
                    500 -> {
                        "Unhandled error occurred!!!"
                    }
                    else -> {
                        val errorJsonString = response.errorBody()?.string().orEmpty()
                        val singleError =
                            response.errorBody()
                                ?.parseErrorWithDataClass<SimpleError>(errorJsonString)
                        if (singleError != null) {
                            singleError.response.message
                        } else {
                            val errorMessage =
                                response.errorBody()?.parseError(errorJsonString)
                            val list: List<Map.Entry<String, Any>> =
                                ArrayList<Map.Entry<String, Any>>(errorMessage!!.entries)
                            val (key, value) = list[0]
                            value.toString()
                        }
                    }
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
                    500 -> {
                        "Unhandled error occurred!!!"
                    }
                    else -> {
                        val errorJsonString = response.errorBody()?.string().orEmpty()
                        val singleError =
                            response.errorBody()
                                ?.parseErrorWithDataClass<SimpleError>(errorJsonString)
                        if (singleError != null) {
                            singleError.response.message
                        } else {
                            val errorMessage =
                                response.errorBody()?.parseError(errorJsonString)
                            val list: List<Map.Entry<String, Any>> =
                                ArrayList<Map.Entry<String, Any>>(errorMessage!!.entries)
                            val (key, value) = list[0]
                            value.toString()
                        }
                    }
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
                    500 -> {
                        "Unhandled error occurred!!!"
                    }
                    else -> {
                        val errorJsonString = response.errorBody()?.string().orEmpty()
                        val singleError =
                            response.errorBody()
                                ?.parseErrorWithDataClass<SimpleError>(errorJsonString)
                        if (singleError != null) {
                            singleError.response.message
                        } else {
                            val errorMessage =
                                response.errorBody()?.parseError(errorJsonString)
                            val list: List<Map.Entry<String, Any>> =
                                ArrayList<Map.Entry<String, Any>>(errorMessage!!.entries)
                            val (key, value) = list[0]
                            value.toString()
                        }
                    }
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
                    500 -> {
                        "Unhandled error occurred!!!"
                    }
                    else -> {
                        val errorJsonString = response.errorBody()?.string().orEmpty()
                        val singleError =
                            response.errorBody()
                                ?.parseErrorWithDataClass<SimpleError>(errorJsonString)
                        if (singleError != null) {
                            singleError.response.message
                        } else {
                            val errorMessage =
                                response.errorBody()?.parseError(errorJsonString)
                            val list: List<Map.Entry<String, Any>> =
                                ArrayList<Map.Entry<String, Any>>(errorMessage!!.entries)
                            val (key, value) = list[0]
                            value.toString()
                        }
                    }
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
        withGem: RequestBody,
        name: RequestBody,
        avgWeigh: RequestBody,
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
            avgYwae,
            images, video, specification, design, orderToGs, recommendCat
        )

        return if (response.isSuccessful) {
            response.body() ?: throw Exception("Response body Null")
        } else {
            throw  Exception(
                when (response.code()) {
                    500 -> {
                        "Unhandled error occurred!!!"
                    }
                    else -> {
                        val errorJsonString = response.errorBody()?.string().orEmpty()
                        val singleError =
                            response.errorBody()
                                ?.parseErrorWithDataClass<SimpleError>(errorJsonString)
                        if (singleError != null) {
                            singleError.response.message
                        } else {
                            val errorMessage =
                                response.errorBody()?.parseError(errorJsonString)
                            val list: List<Map.Entry<String, Any>> =
                                ArrayList<Map.Entry<String, Any>>(errorMessage!!.entries)
                            val (key, value) = list[0]
                            value.toString()
                        }
                    }
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
        avgYwae: RequestBody,
        image1: MultipartBody.Part?,
        image1Id: MultipartBody.Part?,
        image2: MultipartBody.Part?,
        image2Id: MultipartBody.Part?,
        image3: MultipartBody.Part?,
        image3Id: MultipartBody.Part?,
        gif: MultipartBody.Part?,
        gifId: MultipartBody.Part?,
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
            avgYwae,
            image1,
            image1Id,
            image2,
            image2Id,
            image3,
            image3Id,
            gif,
            gifId,
            video,
            specification,
            design,
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
                    500 -> {
                        "Unhandled error occurred!!!"
                    }
                    else -> {
                        val errorJsonString = response.errorBody()?.string().orEmpty()
                        val singleError =
                            response.errorBody()
                                ?.parseErrorWithDataClass<SimpleError>(errorJsonString)
                        if (singleError != null) {
                            singleError.response.message
                        } else {
                            val errorMessage =
                                response.errorBody()?.parseError(errorJsonString)
                            val list: List<Map.Entry<String, Any>> =
                                ArrayList<Map.Entry<String, Any>>(errorMessage!!.entries)
                            val (key, value) = list[0]
                            value.toString()
                        }
                    }
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
                    500 -> {
                        "Unhandled error occurred!!!"
                    }
                    else -> {
                        val errorJsonString = response.errorBody()?.string().orEmpty()
                        val singleError =
                            response.errorBody()
                                ?.parseErrorWithDataClass<SimpleError>(errorJsonString)
                        if (singleError != null) {
                            singleError.response.message
                        } else {
                            val errorMessage =
                                response.errorBody()?.parseError(errorJsonString)
                            val list: List<Map.Entry<String, Any>> =
                                ArrayList<Map.Entry<String, Any>>(errorMessage!!.entries)
                            val (key, value) = list[0]
                            value.toString()
                        }
                    }
                }
            )
        }
    }

    override suspend fun getDesign(token: String, jewelleryType: String): DesignDto {
        val response = setUpStockService.getDesignList(
            token,
            jewelleryType
        )
        return if (response.isSuccessful) {
            response.body() ?: throw Exception("Response body Null")
        } else {
            throw  Exception(
                when (response.code()) {
                    500 -> {
                        "Unhandled error occurred!!!"
                    }
                    else -> {
                        val errorJsonString = response.errorBody()?.string().orEmpty()
                        val singleError =
                            response.errorBody()
                                ?.parseErrorWithDataClass<SimpleError>(errorJsonString)
                        if (singleError != null) {
                            singleError.response.message
                        } else {
                            val errorMessage =
                                response.errorBody()?.parseError(errorJsonString)
                            val list: List<Map.Entry<String, Any>> =
                                ArrayList<Map.Entry<String, Any>>(errorMessage!!.entries)
                            val (key, value) = list[0]
                            value.toString()
                        }
                    }
                }
            )
        }
    }

    override suspend fun createProduct(
        token: String,
        name: RequestBody?,
        productCode: RequestBody,
        type: RequestBody,
        quality: RequestBody,
        group: RequestBody?,
        categoryId: RequestBody?,
        goldAndGemWeight: RequestBody?,
        gemWeightYwae: RequestBody?,
        gemValue: RequestBody?,
        ptAndClipCost: RequestBody?,
        maintenanceCost: RequestBody?,
        diamondInfo: RequestBody?,
        diamondPriceFromGS: RequestBody?,
        diamondValueFromGS: RequestBody?,
        diamondPriceForSale: RequestBody?,
        diamondValueForSale: RequestBody?,
        image1: MultipartBody.Part?,
        image1Id: MultipartBody.Part?,
        image2: MultipartBody.Part?,
        image2Id: MultipartBody.Part?,
        image3: MultipartBody.Part?,
        image3Id: MultipartBody.Part?,
        gif: MultipartBody.Part?,
        gifId: MultipartBody.Part?,
        video: MultipartBody.Part?
    ): SimpleResponseDto {
        val response = setUpStockService.createProduct(
            token,
            name,
            productCode,
            type,
            quality,
            group,
            categoryId,
            goldAndGemWeight,
            gemWeightYwae,
            gemValue,
            ptAndClipCost,
            maintenanceCost,
            diamondInfo,
            diamondPriceFromGS,
            diamondValueFromGS,
            diamondPriceForSale,
            diamondValueForSale,
            image1, image1Id, image2, image2Id, image3, image3Id, gif, gifId, video
        )

        return if (response.isSuccessful) {
            response.body()?.response ?: throw Exception("Response body Null")
        } else {
            throw  Exception(
                when (response.code()) {
                    500 -> {
                        "Unhandled error occurred!!!"
                    }
                    else -> {
                        val errorJsonString = response.errorBody()?.string().orEmpty()
                        val singleError =
                            response.errorBody()
                                ?.parseErrorWithDataClass<SimpleError>(errorJsonString)
                        if (singleError != null) {
                            singleError.response.message
                        } else {
                            val errorMessage =
                                response.errorBody()?.parseError(errorJsonString)
                            val list: List<Map.Entry<String, Any>> =
                                ArrayList<Map.Entry<String, Any>>(errorMessage!!.entries)
                            val (key, value) = list[0]
                            value.toString()
                        }
                    }
                }
            )
        }
    }

    override suspend fun editProduct(
        token: String,
        method: RequestBody,
        productCode: String,
        name: RequestBody?,
        type: RequestBody,
        quality: RequestBody,
        group: RequestBody?,
        categoryId: RequestBody?,
        goldAndGemWeight: RequestBody?,
        gemWeightYwae: RequestBody?,
        gemValue: RequestBody?,
        ptAndClipCost: RequestBody?,
        maintenanceCost: RequestBody?,
        diamondInfo: RequestBody?,
        diamondPriceFromGS: RequestBody?,
        diamondValueFromGS: RequestBody?,
        diamondPriceForSale: RequestBody?,
        diamondValueForSale: RequestBody?,
        image1: MultipartBody.Part?,
        image1Id: MultipartBody.Part?,
        image2: MultipartBody.Part?,
        image2Id: MultipartBody.Part?,
        image3: MultipartBody.Part?,
        image3Id: MultipartBody.Part?,
        gif: MultipartBody.Part?,
        gifId: MultipartBody.Part?,
        video: MultipartBody.Part?
    ): SimpleResponse {
        val response = setUpStockService.editProduct(
            token,
            productCode,
            name,
            method,
            type,
            quality,
            group,
            categoryId,
            goldAndGemWeight,
            gemWeightYwae,
            gemValue,
            ptAndClipCost,
            maintenanceCost,
            diamondInfo,
            diamondPriceFromGS,
            diamondValueFromGS,
            diamondPriceForSale,
            diamondValueForSale,
            image1, image1Id, image2, image2Id, image3, image3Id, gif, gifId, video
        )
        return if (response.isSuccessful) {
            response.body() ?: throw Exception("Response body Null")
        } else {
            throw  Exception(
                when (response.code()) {
                    500 -> {
                        "Unhandled error occurred!!!"
                    }
                    else -> {
                        val errorJsonString = response.errorBody()?.string().orEmpty()
                        val singleError =
                            response.errorBody()
                                ?.parseErrorWithDataClass<SimpleError>(errorJsonString)
                        if (singleError != null) {
                            singleError.response.message
                        } else {
                            val errorMessage =
                                response.errorBody()?.parseError(errorJsonString)
                            val list: List<Map.Entry<String, Any>> =
                                ArrayList<Map.Entry<String, Any>>(errorMessage!!.entries)
                            val (key, value) = list[0]
                            value.toString()
                        }
                    }
                }
            )
        }
    }

    override suspend fun getProductCode(
        token: String,
        jewelleryQualityId: String
    ): ProductCodeResponse {
        val response = setUpStockService.getProductCode(
            token,
            jewelleryQualityId
        )
        return if (response.isSuccessful) {
            response.body() ?: throw Exception("Response body Null")
        } else {
            throw  Exception(
                when (response.code()) {
                    500 -> {
                        "Unhandled error occurred!!!"
                    }
                    else -> {
                        val errorJsonString = response.errorBody()?.string().orEmpty()
                        val singleError =
                            response.errorBody()
                                ?.parseErrorWithDataClass<SimpleError>(errorJsonString)
                        if (singleError != null) {
                            singleError.response.message
                        } else {
                            val errorMessage =
                                response.errorBody()?.parseError(errorJsonString)
                            val list: List<Map.Entry<String, Any>> =
                                ArrayList<Map.Entry<String, Any>>(errorMessage!!.entries)
                            val (key, value) = list[0]
                            value.toString()
                        }
                    }
                }
            )
        }
    }

    override suspend fun getProduct(token: String, productCode: String): ProductSingleDto {
        val response = setUpStockService.getProduct(
            token, productCode
        )
        return if (response.isSuccessful) {
            response.body()?.data?.get(0) ?: throw Exception("Response body Null")
        } else {
            throw  Exception(
                when (response.code()) {
                    500 -> {
                        "Unhandled error occurred!!!"
                    }
                    else -> {
                        val errorJsonString = response.errorBody()?.string().orEmpty()
                        val singleError =
                            response.errorBody()
                                ?.parseErrorWithDataClass<SimpleError>(errorJsonString)
                        if (singleError != null) {
                            singleError.response.message
                        } else {
                            val errorMessage =
                                response.errorBody()?.parseError(errorJsonString)
                            val list: List<Map.Entry<String, Any>> =
                                ArrayList<Map.Entry<String, Any>>(errorMessage!!.entries)
                            val (key, value) = list[0]
                            value.toString()
                        }
                    }
                }
            )
        }
    }
}


