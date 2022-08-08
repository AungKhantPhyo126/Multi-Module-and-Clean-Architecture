package com.critx.domain.repository

import com.critx.commonkotlin.util.Resource
import com.critx.domain.model.LogInSuccess
import com.critx.domain.model.SetupStock.JewelleryType.JewelleryType
import com.critx.domain.model.SetupStock.jewelleryCategory.JewelleryCategory
import com.critx.domain.model.SetupStock.jewelleryGroup.JewelleryGroup
import com.critx.domain.model.SetupStock.jewelleryGroup.JewelleryGroupDomain
import com.critx.domain.model.SetupStock.jewelleryQuality.JewelleryQuality
import com.critx.domain.model.SimpleData
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Multipart

interface SetupStockRepository {
    fun getJewelleryType(token:String): Flow<Resource<List<JewelleryType>>>
    fun getJewelleryQuality(token:String): Flow<Resource<List<JewelleryQuality>>>
    fun getJewelleryGroup(token:String,frequentUse:Int,firstCatId:Int,secondCatId:Int):Flow<Resource<JewelleryGroupDomain>>
    fun createJewelleryGroup(
        token: String,
        image:MultipartBody.Part,
        jewellery_type_id : RequestBody,
        jewellery_quality_id : RequestBody,
        is_frequently_used : RequestBody,
        name : RequestBody
    ):Flow<Resource<SimpleData>>

    fun getJewelleryCategory(token:String,frequentUse:Int,firstCatId:Int,secondCatId:Int,thirdCatId:Int):
            Flow<Resource<List<JewelleryCategory>>>
}