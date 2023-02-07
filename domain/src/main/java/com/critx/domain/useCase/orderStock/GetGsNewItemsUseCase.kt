package com.critx.domain.useCase.orderStock

import com.critx.domain.repository.OrderStockRepository
import javax.inject.Inject

class GetGsNewItemsUseCase @Inject constructor(
    private val orderStockRepository: OrderStockRepository
) {
    operator fun invoke(
        token: String,
        page:Int,
        jewelleryType: String
    )=orderStockRepository.getGsNewItems(token,page,jewelleryType)
}