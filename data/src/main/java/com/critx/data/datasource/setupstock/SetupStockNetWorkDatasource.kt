package com.critx.data.datasource.setupstock

import com.critx.data.network.dto.setupStock.jewelleryQuality.JewelleryQualityData
import com.critx.data.network.dto.setupStock.jewelleryQuality.JewelleryQualityDto
import com.critx.data.network.dto.setupStock.jewelleryType.JewelleryTypeDto

interface SetupStockNetWorkDatasource {
    suspend fun getJewelleryType(token:String):JewelleryTypeDto
    suspend fun getJewelleryQuality(token: String):List<JewelleryQualityData>
}