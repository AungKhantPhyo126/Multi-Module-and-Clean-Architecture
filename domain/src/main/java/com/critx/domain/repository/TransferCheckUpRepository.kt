package com.critx.domain.repository

import com.critx.commonkotlin.util.Resource
import com.critx.domain.model.SimpleData
import com.critx.domain.model.transferCheckUP.CheckUpDomain
import kotlinx.coroutines.flow.Flow

interface TransferCheckUpRepository {
     fun checkUp(token:String,boxCode:String,productIdList:List<String>):Flow<Resource<CheckUpDomain>>

     fun transfer(token: String, boxCode: String, productIdList: List<String>, rfidCode : HashMap<String,String>):Flow<Resource<SimpleData>>
}