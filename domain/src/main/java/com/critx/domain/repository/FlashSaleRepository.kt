package com.critx.domain.repository

import com.critx.commonkotlin.util.Resource
import com.critx.domain.model.SimpleData
import com.critx.domain.model.flashSales.UserPointsDomain
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface FlashSaleRepository {

    fun addToFlashSale(
        token: String,
        title: RequestBody,
        discount_amount: RequestBody,
        time_from: RequestBody,
        time_to: RequestBody,
        productIds: List<RequestBody>,
        image: MultipartBody.Part,
    ):Flow<Resource<SimpleData>>


    fun getUserPoint(
         token: String,
    ): Flow<Resource<UserPointsDomain>>


    fun manualPointsAddOrReduce(
         token: String,
         title: RequestBody,
         discount_amount: RequestBody,
         time_from: RequestBody,
         time_to: RequestBody,
    ): Flow<Resource<SimpleData>>
}