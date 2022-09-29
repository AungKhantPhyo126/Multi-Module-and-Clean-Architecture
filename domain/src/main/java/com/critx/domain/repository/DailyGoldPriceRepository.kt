package com.critx.domain.repository

import com.critx.commonkotlin.util.Resource
import com.critx.domain.model.dailyGoldAndPrice.GoldPriceDomain
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface DailyGoldPriceRepository {
    fun getGoldPrice(token:String):Flow<Resource<List<GoldPriceDomain>>>
}