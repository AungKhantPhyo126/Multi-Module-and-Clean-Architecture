package com.critx.domain.repository

import com.critx.commonkotlin.util.Resource
import com.critx.domain.model.SimpleData
import com.critx.domain.model.dailyGoldAndPrice.GoldPriceDomain
import com.critx.domain.model.dailyGoldAndPrice.RebuyPriceDomain
import com.critx.domain.model.dailyGoldAndPrice.RebuyPriceSmallAndLargeDomain
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface DailyGoldPriceRepository {
    fun getGoldPrice(token:String):Flow<Resource<List<GoldPriceDomain>>>
    fun updateGoldPrice(token: String,price:HashMap<String,String>):Flow<Resource<SimpleData>>
     fun getRebuyPrice(token: String): Flow<Resource<RebuyPriceSmallAndLargeDomain>>
     fun updateRebuyPrice(
        token: String,
        horizontal_option_name: HashMap<String, String>,
        vertical_option_name: HashMap<String, String>,
        horizontal_option_level: HashMap<String, String>,
        vertical_option_level: HashMap<String, String>,
        size: HashMap<String, String>,
        price: HashMap<String, String>,
    ):Flow<Resource<SimpleData>>
}