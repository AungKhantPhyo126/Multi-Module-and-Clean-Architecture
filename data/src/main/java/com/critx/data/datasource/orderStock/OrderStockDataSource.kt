package com.critx.data.datasource.orderStock

import com.critx.data.network.dto.SimpleResponse
import com.critx.data.network.dto.orderStock.BookMarkStockDto
import com.critx.data.network.dto.orderStock.BookMarkStockInfoDto
import com.critx.data.network.dto.orderStock.BookMarkedStocksResponse
import com.critx.data.network.dto.orderStock.GsNewItemsResponse
import okhttp3.MultipartBody
import retrofit2.http.FieldMap
import retrofit2.http.Part

interface OrderStockDataSource {
    suspend fun getBookMarkStockList(token:String,jewelleryType:String,isItemFromGs:String,page:Int):BookMarkedStocksResponse
    suspend fun getGsNewItems(token:String,page:Int,jewelleryType: String):GsNewItemsResponse
    suspend fun getBookMarkStockInfoList(token:String,bookMarkId:String):List<BookMarkStockInfoDto>
    suspend fun orderStock(
        token: String,
        bookMarkAvgYwae: MultipartBody.Part?,
        orderAvgYwae: MultipartBody.Part?,
        bookMarkJewelleryTypeId: MultipartBody.Part?,
        bookMarkImage: MultipartBody.Part?,
        goldQuality: MultipartBody.Part,
        goldSmith: MultipartBody.Part,
        bookMarkId: MultipartBody.Part?,
        gsNewItemId: MultipartBody.Part?,
        equivalent_pure_gold_weight_kpy: MultipartBody.Part,
        jewellery_type_size_id: List<MultipartBody.Part>,
        order_qty: List<MultipartBody.Part>,
        sample_id: List<MultipartBody.Part>?,
        is_important:MultipartBody.Part?,
        custom_category_name:MultipartBody.Part?

    ):SimpleResponse
}