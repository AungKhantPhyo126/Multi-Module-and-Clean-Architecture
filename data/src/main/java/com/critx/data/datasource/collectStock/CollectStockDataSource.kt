package com.critx.data.datasource.collectStock

import com.critx.data.network.dto.SimpleResponse
import com.critx.data.network.dto.collectStock.*
import okhttp3.RequestBody
import java.awt.Stroke

interface CollectStockDataSource {
    suspend fun getProductId(token: String, productCode: String): String

    suspend fun collectSingle(token: String, productCode: String, weight: RequestBody): String

//    suspend fun getProductIdList(token: String,productCodeList: List<RequestBody>):List<String>

    suspend fun scanProductCode(token: String, productCode: String): ProductIdWithType

    suspend fun getJewellerySize(token: String, type: String): List<JewellerySizeDto>

    suspend fun getGoldSmithList(token: String):List<GoldSmithListDto>

    suspend fun collectBatch(
        token: String,
        method:RequestBody,
        kyat:RequestBody?,
        pae:RequestBody?,
        ywae:RequestBody?,
        goldSmithId:RequestBody?,
        bonus:RequestBody?,
        jewellerySizeId:RequestBody?,
        productIds:List<RequestBody>
    ):SimpleResponse
}