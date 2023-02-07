package com.critx.domain.repository

import com.critx.commonkotlin.util.Resource
import com.critx.domain.model.SimpleData
import com.critx.domain.model.collectStock.GoldSmithListDomain
import com.critx.domain.model.collectStock.JewellerySizeDomain
import com.critx.domain.model.collectStock.ProductIdWithTypeDomain
import kotlinx.coroutines.flow.Flow
import okhttp3.RequestBody

interface CollectStockRepository {
    fun getProductId(token:String,productCode:String):Flow<Resource<String>>

    fun collectSingle(token:String,productCode:String,weight:RequestBody):Flow<Resource<String>>

//    fun getProductIdList(token: String,productCodeList: List<RequestBody>):Flow<Resource<List<String>>>

    fun scanProductCode(token: String,productCode: String):Flow<Resource<ProductIdWithTypeDomain>>

    fun getJewellerySize(token: String,type:String):Flow<Resource<List<JewellerySizeDomain>>>

    fun getGoldSmithList(token:String,type: String):Flow<Resource<List<GoldSmithListDomain>>>

    fun collectBatch(
        token: String,
        method:RequestBody,
        ywae:RequestBody?,
        goldSmithId:RequestBody?,
        bonus:RequestBody?,
        jewellerySizeId:RequestBody?,
        productIds:List<RequestBody>
    ):Flow<Resource<SimpleData>>

}