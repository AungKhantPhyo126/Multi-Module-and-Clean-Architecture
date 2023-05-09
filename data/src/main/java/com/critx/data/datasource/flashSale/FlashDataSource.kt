package com.critx.data.datasource.flashSale

import com.critx.commonkotlin.util.Resource
import com.critx.data.network.dto.SimpleResponseDto
import com.critx.data.network.dto.flashSales.UserPointsDto
import com.critx.domain.model.SimpleData
import com.critx.domain.model.flashSales.UserPointsDomain
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

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
    ): UserPointsDto


    suspend fun manualPointsAddOrReduce(
        token: String,
        title: RequestBody,
        discount_amount: RequestBody,
        time_from: RequestBody,
        time_to: RequestBody,
    ): SimpleResponseDto
}