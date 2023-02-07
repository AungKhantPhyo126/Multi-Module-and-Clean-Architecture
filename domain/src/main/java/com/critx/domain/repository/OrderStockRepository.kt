package com.critx.domain.repository

import com.critx.commonkotlin.util.Resource
import com.critx.domain.model.SimpleData
import com.critx.domain.model.orderStock.BookMarkStockDomain
import com.critx.domain.model.orderStock.BookMarkStockInfoDomain
import com.critx.domain.model.orderStock.BookMarkedStocksWithPaging
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody

interface OrderStockRepository {
    fun getBookMarkStockList(
        token: String,
        jewlleryType: String,
        isItemFromGs:String,
        page: Int
    ): Flow<Resource<BookMarkedStocksWithPaging>>

    fun getGsNewItems(
        token: String,
        page: Int,
        jewelleryType: String
    ): Flow<Resource<BookMarkedStocksWithPaging>>

    fun getBookMarkStockInfo(
        token: String,
        bookMarkId: String
    ): Flow<Resource<List<BookMarkStockInfoDomain>>>

    fun orderStock(
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
        custom_category_name:MultipartBody.Part?,
    ): Flow<Resource<SimpleData>>
}