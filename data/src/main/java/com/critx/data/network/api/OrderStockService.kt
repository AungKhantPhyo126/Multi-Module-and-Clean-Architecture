package com.critx.data.network.api

import com.critx.data.network.dto.orderStock.BookMarkedStocksResponse
import com.critx.data.network.dto.repairStock.JobDoneResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface OrderStockService {
    @GET("api/category-bookmarks")
    suspend fun getBookMarks(
        @Header("Authorization") token: String,
        @Query("jewellery_type_id") jewelleryTypeId:String,
        @Query("page") page:Int,
    ): Response<BookMarkedStocksResponse>
}