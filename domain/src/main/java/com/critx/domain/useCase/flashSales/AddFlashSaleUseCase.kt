package com.critx.domain.useCase.flashSales

import com.critx.domain.repository.FlashSaleRepository
import com.critx.domain.repository.OrderStockRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class AddFlashSaleUseCase @Inject constructor(
    private val flashSaleRepository: FlashSaleRepository
) {
    operator fun invoke(
        token: String,
        title: RequestBody,
        discount_amount: RequestBody,
        time_from: RequestBody,
        time_to: RequestBody,
        productIds: List<RequestBody>,
        image: MultipartBody.Part,
    )=flashSaleRepository.addToFlashSale(token,title, discount_amount, time_from, time_to, productIds, image)
}