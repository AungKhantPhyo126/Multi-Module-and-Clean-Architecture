package com.critx.domain.useCase.collectStock

import com.critx.domain.repository.CollectStockRepository
import javax.inject.Inject

class GetGoldSmithListUseCase @Inject constructor(
    private val collectStockRepository: CollectStockRepository
) {
    operator fun invoke(token: String) =
        collectStockRepository.getGoldSmithList(token)
}