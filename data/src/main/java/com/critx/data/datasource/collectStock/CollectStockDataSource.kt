package com.critx.data.datasource.collectStock

import com.critx.data.network.dto.SimpleResponse
import okhttp3.RequestBody
import java.awt.Stroke

interface CollectStockDataSource {
    suspend fun getProductId(token:String,productCode:String):String

    suspend fun collectSingle(token:String,productCode:String,weight:RequestBody):String

    suspend fun getProductIdList(token: String,productCodeList: List<RequestBody>):List<String>
}