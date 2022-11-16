package com.critx.data.datasource.orderStock

import com.critx.data.network.dto.orderStock.BookMarkStockDto
import com.critx.data.network.dto.orderStock.BookMarkedStocksResponse

interface OrderStockDataSource {
    suspend fun getBookMarkStockList(token:String,jewelleryType:String,page:Int):BookMarkedStocksResponse
}