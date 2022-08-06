package com.critx.domain.useCase.SetUpStock

import com.critx.domain.repository.SetupStockRepository
import javax.inject.Inject

class GetJewelleryGroupUseCase @Inject constructor(
    private val setupStockRepository: SetupStockRepository
) {
    operator fun invoke(token:String,freqentUse:Int,firstCatId:Int,secondCatId:Int)=setupStockRepository.getJewelleryGroup(token,freqentUse,firstCatId,secondCatId)
}