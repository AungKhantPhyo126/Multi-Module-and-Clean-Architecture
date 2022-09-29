package com.critx.data.datasource.box

import com.critx.data.network.dto.box.BoxWeightDto

interface BoxNetWorkDataSource {
    suspend fun getBoxWeight(token:String,boxIdList:List<String>):List<BoxWeightDto>
}