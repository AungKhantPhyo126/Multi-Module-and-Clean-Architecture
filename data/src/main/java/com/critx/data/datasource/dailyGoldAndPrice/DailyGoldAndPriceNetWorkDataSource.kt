package com.critx.data.datasource.dailyGoldAndPrice

import com.critx.data.network.dto.SimpleResponseDto
import com.critx.data.network.dto.dailygoldAndPrice.GoldPriceDto
import com.critx.data.network.dto.dailygoldAndPrice.RebuyPriceDto
import retrofit2.http.FieldMap

interface DailyGoldAndPriceNetWorkDataSource {
    suspend fun getGoldPrice(token: String): List<GoldPriceDto>
    suspend fun updateGoldPrice(token: String, price: HashMap<String, String>): SimpleResponseDto
    suspend fun getRebuyPrice(token: String): RebuyPriceDto
    suspend fun updateRebuyPrice(
        token: String,
        horizontal_option_name: HashMap<String, String>,
        vertical_option_name: HashMap<String, String>,
        horizontal_option_level: HashMap<String, String>,
        vertical_option_level: HashMap<String, String>,
        size: HashMap<String, String>,
        price: HashMap<String, String>,
    ):SimpleResponseDto
}