package com.critx.domain.useCase.dailygoldprice

import com.critx.domain.repository.DailyGoldPriceRepository
import javax.inject.Inject

class GetRebuyPriceUseCase @Inject constructor(
    private val dailyGoldPriceRepository: DailyGoldPriceRepository
) {
    operator fun invoke(token:String)=dailyGoldPriceRepository.getRebuyPrice(token)
}