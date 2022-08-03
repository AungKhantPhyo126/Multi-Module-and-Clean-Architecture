package com.critx.data.network.api

import com.critx.data.network.dto.auth.ProfileDto
import com.critx.data.network.dto.setupStock.jewelleryType.JewelleryTypeDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface SetUpStockService {

    @GET("api/jewellery_types")
    suspend fun getJewelleryType(
        @Header("Authorization") token:String
    ): Response<JewelleryTypeDto>
}