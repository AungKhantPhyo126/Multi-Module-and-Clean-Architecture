package com.critx.domain.repository

import com.critx.commonkotlin.util.Resource
import com.critx.domain.model.LogInSuccess
import com.critx.domain.model.SetupStock.JewelleryType.JewelleryType
import com.critx.domain.model.SetupStock.ProductCodeDomain
import com.critx.domain.model.SetupStock.jewelleryCategory.CalculateKPY
import com.critx.domain.model.SetupStock.jewelleryCategory.DesignDomain
import com.critx.domain.model.SetupStock.jewelleryCategory.JewelleryCategory
import com.critx.domain.model.SetupStock.jewelleryGroup.JewelleryGroup
import com.critx.domain.model.SetupStock.jewelleryGroup.JewelleryGroupDomain
import com.critx.domain.model.SetupStock.jewelleryQuality.JewelleryQuality
import com.critx.domain.model.SimpleData
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Multipart
import retrofit2.http.Part

interface SetupStockRepository {
    fun getJewelleryType(token: String): Flow<Resource<List<JewelleryType>>>
    fun getJewelleryQuality(token: String): Flow<Resource<List<JewelleryQuality>>>
    fun getJewelleryGroup(
        token: String,
        frequentUse: Int,
        firstCatId: Int,
        secondCatId: Int
    ): Flow<Resource<JewelleryGroupDomain>>

    fun createJewelleryGroup(
        token: String,
        image: MultipartBody.Part,
        jewellery_type_id: RequestBody,
        jewellery_quality_id: RequestBody,
        is_frequently_used: RequestBody,
        name: RequestBody
    ): Flow<Resource<JewelleryGroup>>

    fun editJewelleryGroup(
        token: String,
        method: RequestBody,
        groupId: String,
        image: MultipartBody.Part?,
        jewellery_type_id: RequestBody,
        jewellery_quality_id: RequestBody,
        is_frequently_used: RequestBody,
        name: RequestBody
    ): Flow<Resource<SimpleData>>

    fun deleteJewelleryGroup(
        token: String,
        method: RequestBody,
        groupId: String,
    ): Flow<Resource<SimpleData>>

    fun deleteJewelleryCategory(
        token: String,
        method: RequestBody,
        catId: String,
    ): Flow<Resource<SimpleData>>


    fun getJewelleryCategory(
        token: String,
        frequentUse: Int?,
        firstCatId: Int?,
        secondCatId: Int?,
        thirdCatId: Int?
    ):
            Flow<Resource<List<JewelleryCategory>>>

    fun createJewelleryCategory(
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
    ): Flow<Resource<JewelleryCategory>>

    fun editJewelleryCategory(
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
    ): Flow<Resource<SimpleData>>

    fun getRelatedCategories(
        token: String,
        categoryId: String
    ): Flow<Resource<List<JewelleryCategory>>>

    fun calculateKPYtoGram(
        token: String,
        kyat: Double,
        pae: Double,
        ywae: Double
    ): Flow<Resource<CalculateKPY>>

    fun getDesignList(token: String,jewelleryType:String): Flow<Resource<List<DesignDomain>>>

    fun createProduct(
        token: String,
        name: RequestBody?,
        productCode: RequestBody,
        type: RequestBody,
        quality: RequestBody,
        group: RequestBody?,
        categoryId: RequestBody?,
        goldAndGemWeight: RequestBody?,
        gemWeightKyat: RequestBody?,
        gemWeightPae: RequestBody?,
        gemWeightYwae: RequestBody?,
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
    ): Flow<Resource<SimpleData>>

    fun getProductCode(
        token: String
    ): Flow<Resource<ProductCodeDomain>>
}
