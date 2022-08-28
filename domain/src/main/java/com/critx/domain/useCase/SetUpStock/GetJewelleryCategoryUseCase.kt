package com.critx.domain.useCase.SetUpStock

import com.critx.domain.repository.SetupStockRepository
import javax.inject.Inject

class GetJewelleryCategoryUseCase @Inject constructor(
    private val setupStockRepository: SetupStockRepository
) {
    operator fun invoke(token:String,freqentUse:Int?,firstCatId:Int?,secondCatId:Int?,thirdCatId:Int?)
    =setupStockRepository.getJewelleryCategory(token,freqentUse,firstCatId,secondCatId,thirdCatId)
}