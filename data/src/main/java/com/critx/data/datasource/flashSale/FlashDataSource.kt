package com.critx.data.datasource.flashSale

import com.critx.commonkotlin.util.Resource
import com.critx.data.network.dto.SimpleResponseDto
import com.critx.data.network.dto.flashSales.CustomerIdDto
import com.critx.data.network.dto.flashSales.UserPointsDto
import com.critx.domain.model.SimpleData
import com.critx.domain.model.flashSales.UserPointsDomain
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Header
import retrofit2.http.Part
import retrofit2.http.Path

interface FlashDataSource {
    suspend fun addToFlashSale(
        token: String,
        title: RequestBody,
        discount_amount: RequestBody,
        time_from: RequestBody,
        time_to: RequestBody,
        productIds: List<RequestBody>,
        image: MultipartBody.Part,
    ): SimpleResponseDto


    suspend fun getUserPoint(
        token: String,
        userCode: String,
    ): UserPointsDto


    suspend fun manualPointsAddOrReduce(
        token: String,
        user_id:RequestBody,
        point:RequestBody,
        reason:RequestBody,
        action:RequestBody,
    ): SimpleResponseDto

    suspend fun userScan(
         token: String,
         userCode:String
    ):CustomerIdDto
}