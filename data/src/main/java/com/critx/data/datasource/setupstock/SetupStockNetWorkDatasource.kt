package com.critx.data.datasource.setupstock

import com.critx.commonkotlin.util.Resource
import com.critx.data.network.dto.SimpleResponse
import com.critx.data.network.dto.SimpleResponseDto
import com.critx.data.network.dto.setupStock.jewelleryCategory.JewelleryCatDto
import com.critx.data.network.dto.setupStock.jewelleryCategory.JewelleryCategoryData
import com.critx.data.network.dto.setupStock.jewelleryGroup.JewelleryGroupDto
import com.critx.data.network.dto.setupStock.jewelleryQuality.JewelleryQualityData
import com.critx.data.network.dto.setupStock.jewelleryQuality.JewelleryQualityDto
import com.critx.data.network.dto.setupStock.jewelleryType.JewelleryTypeDto
import com.critx.domain.model.SetupStock.jewelleryCategory.JewelleryCategory
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface SetupStockNetWorkDatasource {
    suspend fun getJewelleryType(token: String): JewelleryTypeDto
    suspend fun getJewelleryQuality(token: String): List<JewelleryQualityData>
    suspend fun getJewelleryGroup(token: String,frequentUse:Int,firstCatId:Int,secondCatId:Int): JewelleryGroupDto
    suspend fun createJewelleryGroup(
        token: String,
        image: MultipartBody.Part,
        jewellery_type_id : RequestBody,
        jewellery_quality_id : RequestBody,
        is_frequently_used : RequestBody,
        name : RequestBody
    ):SimpleResponse

    suspend fun getJewelleryCategory(token:String,frequentUse:Int,firstCatId:Int,secondCatId:Int,thirdCatId:Int):JewelleryCatDto
}