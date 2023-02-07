package com.critx.domain.useCase.collectStock

import com.critx.domain.repository.CollectStockRepository
import okhttp3.RequestBody
import javax.inject.Inject

class CollectBatchUseCase @Inject constructor(
    private val collectStockRepository: CollectStockRepository
) {
    operator fun invoke(
        token: String,
        method: RequestBody,
        ywae: RequestBody?,
        goldSmithId: RequestBody?,
        bonus: RequestBody?,
        jewellerySizeId: RequestBody?,
        productIds: List<RequestBody>
    ) =
        collectStockRepository.collectBatch(
            token,
            method,
            ywae,
            goldSmithId,
            bonus,
            jewellerySizeId,
            productIds
        )
}