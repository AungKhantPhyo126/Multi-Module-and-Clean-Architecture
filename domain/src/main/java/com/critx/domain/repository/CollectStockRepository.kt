package com.critx.domain.repository

import com.critx.commonkotlin.util.Resource
import kotlinx.coroutines.flow.Flow
import okhttp3.RequestBody

interface CollectStockRepository {
    fun getProductId(token:String,productCode:String):Flow<Resource<String>>

    fun collectSingle(token:String,productCode:String,weight:RequestBody):Flow<Resource<String>>

    fun getProductIdList(token: String,productCodeList: List<RequestBody>):Flow<Resource<List<String>>>

}