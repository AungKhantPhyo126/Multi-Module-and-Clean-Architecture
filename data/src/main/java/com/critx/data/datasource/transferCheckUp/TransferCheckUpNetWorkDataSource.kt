package com.critx.data.datasource.transferCheckUp

import com.critx.data.network.dto.SimpleResponseDto
import com.critx.data.network.dto.transferCheckUp.CheckUpDto

interface TransferCheckUpNetWorkDataSource {
    suspend fun checkUp(token:String,boxCode:String,productIdList:List<String>):List<CheckUpDto>

    suspend fun transfer(token: String, boxCode: String, productIdList: List<String>, rfidCode : HashMap<String,String>):SimpleResponseDto
}