package com.critx.domain.useCase.box

import com.critx.domain.repository.BoxRepository
import com.critx.domain.repository.DailyGoldPriceRepository
import javax.inject.Inject

class GetBoxWeightUseCase @Inject constructor(
    private val boxRepository: BoxRepository
) {
    operator fun invoke(token:String,boxIdList:List<String>)=boxRepository.getBoxWeight(token,boxIdList)
}