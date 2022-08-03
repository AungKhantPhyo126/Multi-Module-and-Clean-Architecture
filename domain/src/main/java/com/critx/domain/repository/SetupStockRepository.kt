package com.critx.domain.repository

import com.critx.commonkotlin.util.Resource
import com.critx.domain.model.LogInSuccess
import com.critx.domain.model.SetupStock.JewelleryType.JewelleryType
import kotlinx.coroutines.flow.Flow

interface SetupStockRepository {
    fun getJewelleryType(token:String): Flow<Resource<List<JewelleryType>>>
}