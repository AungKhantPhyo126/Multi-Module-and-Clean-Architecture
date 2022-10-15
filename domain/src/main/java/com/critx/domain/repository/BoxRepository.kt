package com.critx.domain.repository

import com.critx.commonkotlin.util.Resource
import com.critx.domain.model.box.BoxWeightDomain
import kotlinx.coroutines.flow.Flow

interface BoxRepository {
    fun getBoxWeight(token:String,boxIdList:List<String>):Flow<Resource<List<BoxWeightDomain>>>
}