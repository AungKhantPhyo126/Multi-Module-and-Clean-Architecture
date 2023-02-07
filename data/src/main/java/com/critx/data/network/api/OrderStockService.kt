package com.critx.data.network.api

import com.critx.data.network.dto.SimpleResponse
import com.critx.data.network.dto.orderStock.BookMarkStockInfoResponse
import com.critx.data.network.dto.orderStock.BookMarkedStocksResponse
import com.critx.data.network.dto.orderStock.GsNewItemsResponse
import com.critx.data.network.dto.repairStock.JobDoneResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface OrderStockService {
    @GET("api/category-bookmarks")
    suspend fun getBookMarks(
        @Header("Authorization") token: String,
        @Query("jewellery_type_id") jewelleryTypeId: String,
        @Query("is_item_from_gs") isItemFromGs: String,
        @Query("page") page: Int,
    ): Response<BookMarkedStocksResponse>


    @GET("api/category-bookmarks/{bookMarkId}/stocks")
    suspend fun getBookMarksStocks(
        @Header("Authorization") token: String,
        @Path("bookMarkId") bookMarkId: String,
    ): Response<BookMarkStockInfoResponse>

    @GET("api/gs-new-items")
    suspend fun getGsNewItems(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("jewellery_type_id")jewellery_type_id:String
        ): Response<GsNewItemsResponse>

    @JvmSuppressWildcards
    @Multipart
    @POST("api/gs-orders/store")
    suspend fun orderStock(
        @Header("Authorization") token: String,
        @Part bookMarkAvgYwae: MultipartBody.Part?,
        @Part orderAvgYwae: MultipartBody.Part?,
        @Part bookMarkJewelleryTypeId: MultipartBody.Part?,
        @Part bookMarkImage: MultipartBody.Part?,
        @Part goldQuality: MultipartBody.Part,
        @Part goldSmith: MultipartBody.Part,
        @Part bookMarkId: MultipartBody.Part?,
        @Part gsNewItemId: MultipartBody.Part?,
        @Part equivalent_pure_gold_weight_kpy: MultipartBody.Part,
        @Part jewellery_type_size_id: List<MultipartBody.Part>,
        @Part order_qty: List<MultipartBody.Part>,
        @Part sample_id: List<MultipartBody.Part>?,
        @Part isImportant: MultipartBody.Part?,
        @Part custom_category_name: MultipartBody.Part?,
        ):Response<SimpleResponse>
}