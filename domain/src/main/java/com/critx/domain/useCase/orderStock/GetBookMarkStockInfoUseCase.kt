package com.critx.domain.useCase.orderStock

import com.critx.domain.repository.OrderStockRepository
import javax.inject.Inject

class GetBookMarkStockInfoUseCase @Inject constructor(
    private val orderStockRepository: OrderStockRepository
) {
    operator fun invoke(
        token: String,
       bookMarkId:String
    )=orderStockRepository.getBookMarkStockInfo(token,bookMarkId)
}