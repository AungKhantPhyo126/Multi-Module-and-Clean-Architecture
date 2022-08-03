package com.critx.data.datasource.setupstock

import com.critx.data.network.dto.setupStock.jewelleryType.JewelleryTypeDto

interface SetupStockNetWorkDatasource {
    suspend fun getJewelleryType(token:String):JewelleryTypeDto
}