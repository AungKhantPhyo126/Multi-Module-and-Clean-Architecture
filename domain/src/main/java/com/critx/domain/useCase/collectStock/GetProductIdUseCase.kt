package com.critx.domain.useCase.collectStock

import com.critx.domain.repository.CollectStockRepository
import javax.inject.Inject

class GetProductIdUseCase @Inject constructor(
    private val collectStockRepository: CollectStockRepository
) {
    operator fun invoke(token:String,productCode:String) =
        collectStockRepository.getProductId(token, productCode)
}