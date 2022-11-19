package com.critx.domain.useCase.orderStock

import com.critx.domain.repository.GiveGoldRepository
import com.critx.domain.repository.OrderStockRepository
import javax.inject.Inject

class GetBookMarksUseCase @Inject constructor(
    private val orderStockRepository: OrderStockRepository
) {
    operator fun invoke(
        token: String,
        jewelleryType:String,
        isItemFromGs:String,
        page:Int
    )=orderStockRepository.getBookMarkStockList(token,jewelleryType,isItemFromGs,page)
}