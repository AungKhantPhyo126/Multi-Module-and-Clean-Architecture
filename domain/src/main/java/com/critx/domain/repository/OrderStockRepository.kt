package com.critx.domain.repository

import com.critx.commonkotlin.util.Resource
import com.critx.domain.model.orderStock.BookMarkStockDomain
import com.critx.domain.model.orderStock.BookMarkedStocksWithPaging
import kotlinx.coroutines.flow.Flow

interface OrderStockRepository {
    fun getBookMarkStockList(token:String,jewlleryType:String,page:Int):Flow<Resource<BookMarkedStocksWithPaging>>
}