package com.critx.data.network.api

import com.critx.data.network.dto.SimpleResponse
import com.critx.data.network.dto.SimpleResponseDto
import com.critx.data.network.dto.auth.ProfileDto
import com.critx.data.network.dto.setupStock.jewelleryCategory.CalculateKPYDto
import com.critx.data.network.dto.setupStock.jewelleryCategory.DesignDto
import com.critx.data.network.dto.setupStock.jewelleryCategory.JewelleryCatDto
import com.critx.data.network.dto.setupStock.jewelleryGroup.CreateGroupDto
import com.critx.data.network.dto.setupStock.jewelleryGroup.Data
import com.critx.data.network.dto.setupStock.jewelleryGroup.JewelleryGroupDto
import com.critx.data.network.dto.setupStock.jewelleryQuality.JewelleryQualityData
import com.critx.data.network.dto.setupStock.jewelleryQuality.JewelleryQualityDto
import com.critx.data.network.dto.setupStock.jewelleryType.JewelleryTypeDto
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface SetUpStockService {

    @GET("api/jewellery_types")
    suspend fun getJewelleryType(
        @Header("Authorization") token: String
    ): Response<JewelleryTypeDto>

    @GET("api/jewellery_qualities")
    suspend fun getJewelleryQuality(
        @Header("Authorization") token: String
    ): Response<List<JewelleryQualityData>>

    @GET("api/groups")
    suspend fun getJewelleryGroup(
        @Header("Authorization") token: String,
        @Query("is_frequently_used") frequentUse: Int,
        @Query("jewellery_type") firstCatId: Int,
        @Query("jewellery_quality") secondCatId: Int
    ): Response<JewelleryGroupDto>


    //Create Methods
    @Multipart
    @POST("api/groups/store")
    suspend fun createJewelleryGroup(
        @Header("Authorization") token: String,
        @Part("jewellery_type_id") type: RequestBody?,
        @Part("jewellery_quality_id") quality: RequestBody?,
        @Part("is_frequently_used") isFrequentUsed: RequestBody?,
        @Part("name") name: RequestBody?,
        @Part image: MultipartBody.Part?
    ): Response<CreateGroupDto>

    @Multipart
    @POST("api/groups/update/{groupId}")
    suspend fun editJewelleryGroup(
        @Header("Authorization") token: String,
        @Part("_method")methodName:RequestBody,
        @Path("groupId")groupId:String,
        @Part("jewellery_type_id") type: RequestBody?,
        @Part("jewellery_quality_id") quality: RequestBody?,
        @Part("is_frequently_used") isFrequentUsed: RequestBody?,
        @Part("name") name: RequestBody?,
        @Part image: MultipartBody.Part?
    ): Response<SimpleResponse>

    @GET("api/categories")
    suspend fun getJewelleryCategory(
        @Header("Authorization") token: String,
        @Query("is_frequently_used") frequentUse: Int?,
        @Query("jewellery_type") firstCatId: Int?,
        @Query("jewellery_quality") secondCatId: Int?,
        @Query("group") group: Int?
    ): Response<JewelleryCatDto>

    @JvmSuppressWildcards
    @Multipart
    @POST("api/categories/store")
    suspend fun createJewelleryCategory(
        @Header("Authorization") token: String,
        @Part("jewellery_type_id") type: RequestBody,
        @Part("jewellery_quality_id") quality: RequestBody,
        @Part("group_id") group: RequestBody,
        @Part("is_frequently_used") isFrequentUsed: RequestBody,
        @Part("name") name: RequestBody,
        @Part("avg_weight_per_unit_gm") avgWeighPerUnitGm: RequestBody,
        @Part("avg_wastage_per_unit_kpy") avgWastagePerUnitKpy: RequestBody,
        @Part images:List<MultipartBody.Part>,
        @Part video:MultipartBody.Part,
        @Part("specification") specification: RequestBody,
        @Part("designs[]") design: List<RequestBody>,
        @Part("order_to_goldsmith") orderToGs: RequestBody,
        @Part("related_categories[]") recommendCat:List<RequestBody>
    ): Response<SimpleResponse>

    @POST("api/kpy/calculate")
    suspend fun calculateKPYtoGram(
        @Header("Authorization") token: String,
        @Query("kyat") kyat: Double,
        @Query("pae") pae: Double,
        @Query("ywae") ywae: Double,
    ): Response<CalculateKPYDto>

    @GET("api/designs/quicklist")
    suspend fun getDesignList(
        @Header("Authorization") token: String
        ):Response<DesignDto>


    @JvmSuppressWildcards
    @Multipart
    @POST("api/products/store")
    suspend fun createProduct(
        @Header("Authorization") token: String,
        @Part("name") name:RequestBody,
        @Part("jewellery_type_id") type: RequestBody,
        @Part("jewellery_quality_id") quality: RequestBody,
        @Part("group_id") group: RequestBody,
        @Part("category_id") categoryId:RequestBody,
        @Part("gold_and_gem_weight_gm") goldAndGemWeight:RequestBody,
        @Part("gem_weight_kyat") gemWeightKyat:RequestBody,
        @Part("gem_weight_pae") gemWeightPae:RequestBody,
        @Part("gem_weight_ywae") gemWeightYwae:RequestBody,
        @Part("gem_value") gemValue:RequestBody?,
        @Part("pt_and_clip_cost") ptAndClipCost:RequestBody?,
        @Part("maintenance_cost") maintenanceCost:RequestBody?,
        @Part("diamond[diamond_info]") diamondInfo:RequestBody?,
        @Part("diamond[price_from_goldsmith]") diamondPriceFromGS:RequestBody?,
        @Part("diamond[value_from_goldsmith]") diamondValueFromGS:RequestBody?,
        @Part("diamond[price_for_sale]") diamondPriceForSale:RequestBody?,
        @Part("diamond[value_for_sale]") diamondValueForSale:RequestBody?,
        @Part images:List<MultipartBody.Part>,
        @Part video:MultipartBody.Part,
    ):Response<SimpleResponse>
}