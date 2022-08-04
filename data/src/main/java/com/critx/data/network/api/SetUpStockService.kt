package com.critx.data.network.api

import com.critx.data.network.dto.auth.ProfileDto
import com.critx.data.network.dto.setupStock.jewelleryGroup.JewelleryGroupDto
import com.critx.data.network.dto.setupStock.jewelleryQuality.JewelleryQualityData
import com.critx.data.network.dto.setupStock.jewelleryQuality.JewelleryQualityDto
import com.critx.data.network.dto.setupStock.jewelleryType.JewelleryTypeDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface SetUpStockService {

    @GET("api/jewellery_types")
    suspend fun getJewelleryType(
        @Header("Authorization") token:String
    ): Response<JewelleryTypeDto>

    @GET("api/jewellery_qualities")
    suspend fun getJewelleryQuality(
        @Header("Authorization") token:String
    ): Response<List<JewelleryQualityData>>

    @GET("api/first_sub_categories")
    suspend fun getJewelleryGroup(
        @Header("Authorization") token: String
    ):Response<JewelleryGroupDto>
}