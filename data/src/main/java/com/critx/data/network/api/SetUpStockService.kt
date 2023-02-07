package com.critx.data.network.api

import com.critx.data.network.dto.SimpleResponse
import com.critx.data.network.dto.SimpleResponseDto
import com.critx.data.network.dto.auth.ProfileDto
import com.critx.data.network.dto.setupStock.ProductApiResponse
import com.critx.data.network.dto.setupStock.ProductCodeResponse
import com.critx.data.network.dto.setupStock.jewelleryCategory.*
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

    @GET("api/categories/{catId}/related_categories")
    suspend fun getRelatedCat(
        @Header("Authorization") token: String,
        @Path("catId") categoryId: String
    ): Response<JewelleryCatDto>


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
        @Part("_method") methodName: RequestBody,
        @Path("groupId") groupId: String,
        @Part("jewellery_type_id") type: RequestBody?,
        @Part("jewellery_quality_id") quality: RequestBody?,
        @Part("is_frequently_used") isFrequentUsed: RequestBody?,
        @Part("name") name: RequestBody?,
        @Part image: MultipartBody.Part?
    ): Response<SimpleResponse>

    @Multipart
    @POST("api/groups/delete/{groupId}")
    suspend fun deleteJewelleryGroup(
        @Header("Authorization") token: String,
        @Part("_method") methodName: RequestBody,
        @Path("groupId") groupId: String,
    ): Response<SimpleResponse>

    @Multipart
    @POST("api/categories/delete/{catId}")
    suspend fun deleteJewelleryCategory(
        @Header("Authorization") token: String,
        @Part("_method") methodName: RequestBody,
        @Path("catId") groupId: String,
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
        @Part("with_gem") withGem: RequestBody,
        @Part("name") name: RequestBody,
        @Part("avg_weight_per_unit_gm") avgWeighPerUnitGm: RequestBody,
        @Part("avg_unit_wastage_ywae") avgWastageYwae: RequestBody,
        @Part images:MutableList< MultipartBody.Part>,
        @Part video: MultipartBody.Part?,
        @Part("specification") specification: RequestBody,
        @Part("designs[]") design: List<RequestBody>,
        @Part("order_to_goldsmith") orderToGs: RequestBody,
        @Part("related_categories[]") recommendCat: List<RequestBody>?
    ): Response<JewelleryCatCreatedData>

    @JvmSuppressWildcards
    @Multipart
    @POST("api/categories/update/{catId}")
    suspend fun editJewelleryCategory(
        @Header("Authorization") token: String,
        @Path("catId") categoryId: String,
        @Part("_method") methodName: RequestBody,
        @Part("jewellery_type_id") type: RequestBody,
        @Part("jewellery_quality_id") quality: RequestBody,
        @Part("group_id") group: RequestBody,
        @Part("is_frequently_used") isFrequentUsed: RequestBody,
        @Part("with_gem") withGem: RequestBody,
        @Part("name") name: RequestBody,
        @Part("avg_weight_per_unit_gm") avgWeighPerUnitGm: RequestBody,
        @Part("avg_unit_wastage_ywae") avgWastagYwae: RequestBody,
        @Part image1: MultipartBody.Part?,
        @Part image1Id: MultipartBody.Part?,
        @Part image2: MultipartBody.Part?,
        @Part image2Id: MultipartBody.Part?,
        @Part image3: MultipartBody.Part?,
        @Part image3Id: MultipartBody.Part?,
        @Part gif: MultipartBody.Part?,
        @Part gifId: MultipartBody.Part?,
        @Part video: MultipartBody.Part?,
        @Part("specification") specification: RequestBody,
        @Part("designs[]") design: List<RequestBody>,
        @Part("order_to_goldsmith") orderToGs: RequestBody,
        @Part("related_categories[]") recommendCat: List<RequestBody>?
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
        @Header("Authorization") token: String,
        @Query("jewellery_type") jewelleryType: String
    ): Response<DesignDto>


    @JvmSuppressWildcards
    @Multipart
    @POST("api/products/store")
    suspend fun createProduct(
        @Header("Authorization") token: String,
        @Part("name") name: RequestBody?,
        @Part("product_code") productCode: RequestBody,
        @Part("jewellery_type_id") type: RequestBody,
        @Part("jewellery_quality_id") quality: RequestBody,
        @Part("group_id") group: RequestBody?,
        @Part("category_id") categoryId: RequestBody?,
        @Part("gold_and_gem_weight_gm") goldAndGemWeight: RequestBody?,
        @Part("gem_weight_ywae") gemWeightYwae: RequestBody?,
        @Part("gem_value") gemValue: RequestBody?,
        @Part("pt_and_clip_cost") ptAndClipCost: RequestBody?,
        @Part("maintenance_cost") maintenanceCost: RequestBody?,
        @Part("diamond[diamond_info]") diamondInfo: RequestBody?,
        @Part("diamond[price_from_goldsmith]") diamondPriceFromGS: RequestBody?,
        @Part("diamond[value_from_goldsmith]") diamondValueFromGS: RequestBody?,
        @Part("diamond[price_for_sale]") diamondPriceForSale: RequestBody?,
        @Part("diamond[value_for_sale]") diamondValueForSale: RequestBody?,
        @Part image1: MultipartBody.Part?,
        @Part image1Id: MultipartBody.Part?,
        @Part image2: MultipartBody.Part?,
        @Part image2Id: MultipartBody.Part?,
        @Part image3: MultipartBody.Part?,
        @Part image3Id: MultipartBody.Part?,
        @Part gif: MultipartBody.Part?,
        @Part gifId: MultipartBody.Part?,
        @Part video: MultipartBody.Part?,
    ): Response<SimpleResponse>

    @GET("api/products/code")
    suspend fun getProductCode(
        @Header("Authorization") token: String,
        @Query("jewellery_quality_id")jewelleryQualityId:String,
    ): Response<ProductCodeResponse>

    @GET("api/products")
    suspend fun getProduct(
        @Header("Authorization") token: String,
        @Query("code") productCode: String
    ): Response<ProductApiResponse>

    @JvmSuppressWildcards
    @Multipart
    @POST("api/products/update/{productId}")
    suspend fun editProduct(
        @Header("Authorization") token: String,
        @Path("productId") productId: String,
        @Part("name") name: RequestBody?,
        @Part("_method") method: RequestBody,
        @Part("jewellery_type_id") type: RequestBody,
        @Part("jewellery_quality_id") quality: RequestBody,
        @Part("group_id") group: RequestBody?,
        @Part("category_id") categoryId: RequestBody?,
        @Part("gold_and_gem_weight_gm") goldAndGemWeight: RequestBody?,
        @Part("gem_weight_ywae") gemWeightYwae: RequestBody?,
        @Part("gem_value") gemValue: RequestBody?,
        @Part("pt_and_clip_cost") ptAndClipCost: RequestBody?,
        @Part("maintenance_cost") maintenanceCost: RequestBody?,
        @Part("diamond[diamond_info]") diamondInfo: RequestBody?,
        @Part("diamond[price_from_goldsmith]") diamondPriceFromGS: RequestBody?,
        @Part("diamond[value_from_goldsmith]") diamondValueFromGS: RequestBody?,
        @Part("diamond[price_for_sale]") diamondPriceForSale: RequestBody?,
        @Part("diamond[value_for_sale]") diamondValueForSale: RequestBody?,
        @Part image1: MultipartBody.Part?,
        @Part image1Id: MultipartBody.Part?,
        @Part image2: MultipartBody.Part?,
        @Part image2Id: MultipartBody.Part?,
        @Part image3: MultipartBody.Part?,
        @Part image3Id: MultipartBody.Part?,
        @Part gif: MultipartBody.Part?,
        @Part gifId: MultipartBody.Part?,
        @Part video: MultipartBody.Part?,
    ): Response<SimpleResponse>
}