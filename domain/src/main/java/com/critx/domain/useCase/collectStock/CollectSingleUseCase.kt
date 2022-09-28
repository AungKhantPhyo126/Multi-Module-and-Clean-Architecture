package com.critx.domain.useCase.collectStock

import com.critx.domain.repository.CollectStockRepository
import okhttp3.RequestBody
import javax.inject.Inject

class CollectSingleUseCase @Inject constructor(
    private val collectStockRepository: CollectStockRepository
) {
    operator fun invoke(token:String,productCode:String,weight:RequestBody) =
        collectStockRepository.collectSingle(token, productCode,weight)
}