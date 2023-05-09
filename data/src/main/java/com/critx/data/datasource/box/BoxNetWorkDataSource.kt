package com.critx.data.datasource.box

import com.critx.data.network.dto.SimpleResponseDto
import com.critx.data.network.dto.box.BoxScanDto
import com.critx.data.network.dto.box.BoxWeightDto

interface BoxNetWorkDataSource {
    suspend fun getBoxWeight(token:String,boxIdList:List<String>):List<BoxWeightDto>

    suspend fun getBoxData(token: String,boxCode:String):BoxScanDto

    suspend fun arrangeBox(token: String,boxes:List<String>):SimpleResponseDto

}