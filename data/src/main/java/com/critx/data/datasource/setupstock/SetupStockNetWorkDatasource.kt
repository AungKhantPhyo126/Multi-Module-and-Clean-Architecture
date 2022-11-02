package com.critx.data.datasource.setupstock

import com.critx.commonkotlin.util.Resource
import com.critx.data.network.dto.SimpleResponse
import com.critx.data.network.dto.SimpleResponseDto
import com.critx.data.network.dto.setupStock.ProductCodeResponse
import com.critx.data.network.dto.setupStock.jewelleryCategory.*
import com.critx.data.network.dto.setupStock.jewelleryGroup.Data
import com.critx.data.network.dto.setupStock.jewelleryGroup.JewelleryGroupDto
import com.critx.data.network.dto.setupStock.jewelleryQuality.JewelleryQualityData
import com.critx.data.network.dto.setupStock.jewelleryQuality.JewelleryQualityDto
import com.critx.data.network.dto.setupStock.jewelleryType.JewelleryTypeDto
import com.critx.domain.model.SetupStock.jewelleryCategory.JewelleryCategory
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Header
import retrofit2.http.Part

interface SetupStockNetWorkDatasource {
    suspend fun getJewelleryType(token: String): JewelleryTypeDto
    suspend fun getJewelleryQuality(token: String): List<JewelleryQualityData>
    suspend fun getJewelleryGroup(
        token: String,
        frequentUse: Int,
        firstCatId: Int,
        secondCatId: Int
    ): JewelleryGroupDto

    suspend fun createJewelleryGroup(
        token: String,
        image: MultipartBody.Part,
        jewellery_type_id: RequestBody,
        jewellery_quality_id: RequestBody,
        is_frequently_used: RequestBody,
        name: RequestBody
    ): Data

    suspend fun editJewelleryGroup(
        token: String,
        method: RequestBody,
        groupId: String,
        image: MultipartBody.Part,
        jewellery_type_id: RequestBody,
        jewellery_quality_id: RequestBody,
        is_frequently_used: RequestBody,
        name: RequestBody
    ): SimpleResponse

    suspend fun deleteJewelleryGroup(
        token: String,
        method: RequestBody,
        groupId: String,
    ): SimpleResponse

    suspend fun deleteJewelleryCategory(
        token: String,
        method: RequestBody,
        catId: String,
    ):SimpleResponse

    suspend fun getJewelleryCategory(
        token: String,
        frequentUse: Int?,
        firstCatId: Int?,
        secondCatId: Int?,
        thirdCatId: Int?
    ): JewelleryCatDto

    suspend fun getRelatedJewelleryCategories(
        token: String,
        categoryId: String
    ):JewelleryCatDto

    suspend fun createJewelleryCategory(
        token: String,
        jewellery_type_id: RequestBody,
        jewellery_quality_id: RequestBody,
        groupId: RequestBody,
        is_frequently_used: RequestBody,
        withGem:RequestBody,
        name: RequestBody,
        avgWeigh: RequestBody,
        avgKyat:RequestBody,
        avgPae:RequestBody,
        avgYwae:RequestBody,
        images: MutableList<MultipartBody.Part>,
        video: MultipartBody.Part?,
        specification: RequestBody,
        design: MutableList<RequestBody>,
        orderToGs: RequestBody,
        recommendCat: MutableList<RequestBody>?
    ): JewelleryCatCreatedData

    suspend fun editJewelleryCategory(
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
        avgKyat:RequestBody,
        avgPae:RequestBody,
        avgYwae:RequestBody,
        images: MutableList<MultipartBody.Part>,
        video: MultipartBody.Part?,
        specification: RequestBody,
        design: MutableList<RequestBody>,
        orderToGs: RequestBody,
        recommendCat: MutableList<RequestBody>
    ): SimpleResponse

    suspend fun calculateKPYtoGram(
        token: String,
        kyat: Double,
        pae: Double,
        ywae: Double
    ): CalculateKPYDto

    suspend fun getDesign(
        token: String,
        jewelleryType:String
    ): DesignDto

    suspend fun createProduct(
        token: String,
        name: RequestBody,
        productCode:RequestBody,
        type: RequestBody,
        quality: RequestBody,
        group: RequestBody?,
        categoryId: RequestBody?,
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
        video: MultipartBody.Part?,
    ): SimpleResponse

    suspend fun getProductCode(
        token: String
    ):ProductCodeResponse
}