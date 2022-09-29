package com.critx.domain.useCase.dailygoldprice

import com.critx.domain.repository.DailyGoldPriceRepository
import javax.inject.Inject

class GetGoldPriceUseCase @Inject constructor(
    private val dailyGoldPriceRepository: DailyGoldPriceRepository
) {
    operator fun invoke(token:String)=dailyGoldPriceRepository.getGoldPrice(token)
}