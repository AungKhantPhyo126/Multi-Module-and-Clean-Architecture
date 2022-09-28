package com.critx.domain.useCase.collectStock

import com.critx.domain.repository.CollectStockRepository
import javax.inject.Inject

class GetJewellerySizeUseCase @Inject constructor(
    private val collectStockRepository: CollectStockRepository
) {
    operator fun invoke(token:String,type:String) =
        collectStockRepository.getJewellerySize(token, type)
}