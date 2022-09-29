package com.critx.data.datasource.dailyGoldAndPrice

import com.critx.data.network.dto.dailygoldAndPrice.GoldPriceDto

interface DailyGoldAndPriceNetWorkDataSource {
    suspend fun getGoldPrice(token:String):List<GoldPriceDto>
}