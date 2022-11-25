package com.critx.domain.useCase.dailygoldprice

import com.critx.domain.repository.DailyGoldPriceRepository
import javax.inject.Inject

class UpdateRebuyPriceUseCase @Inject constructor(
    private val dailyGoldPriceRepository: DailyGoldPriceRepository
) {
    operator fun invoke(
        token: String,
        horizontal_option_name: HashMap<String, String>,
        vertical_option_name: HashMap<String, String>,
        horizontal_option_level: HashMap<String, String>,
        vertical_option_level: HashMap<String, String>,
        size: HashMap<String, String>,
        price: HashMap<String, String>
    ) = dailyGoldPriceRepository.updateRebuyPrice(
        token,
        horizontal_option_name,
        vertical_option_name,
        horizontal_option_level,
        vertical_option_level,
        size,
        price
    )
}