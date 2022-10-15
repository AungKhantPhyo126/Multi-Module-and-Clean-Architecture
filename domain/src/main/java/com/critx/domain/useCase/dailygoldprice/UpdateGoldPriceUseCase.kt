package com.critx.domain.useCase.dailygoldprice

import com.critx.domain.repository.DailyGoldPriceRepository
import javax.inject.Inject

class UpdateGoldPriceUseCase@Inject constructor(
    private val dailyGoldPriceRepository: DailyGoldPriceRepository
) {
    operator fun invoke(token:String,price:HashMap<String,String>)=dailyGoldPriceRepository.updateGoldPrice(token,price)
}