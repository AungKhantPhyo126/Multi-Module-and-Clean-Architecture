package com.critx.domain.useCase.orderStock

import com.critx.domain.repository.OrderStockRepository
import okhttp3.MultipartBody
import javax.inject.Inject

class OrderStockUseCase @Inject constructor(
    private val orderStockRepository: OrderStockRepository
) {
    operator fun invoke(
        token: String,
        bookMarkAvgKyat: MultipartBody.Part?,
        bookMarkAvgPae: MultipartBody.Part?,
        bookMarkAvgYwae: MultipartBody.Part?,
        goldQuality: MultipartBody.Part,
        goldSmith: MultipartBody.Part,
        bookMarkId: MultipartBody.Part,
        equivalent_pure_gold_weight_kpy: MultipartBody.Part,
        jewellery_type_size_id: List<MultipartBody.Part>,
        order_qty: List<MultipartBody.Part>,
        sample_id: List<MultipartBody.Part>
    ) = orderStockRepository.orderStock(
        token,
        bookMarkAvgKyat,
        bookMarkAvgPae,
        bookMarkAvgYwae,
        goldQuality,
        goldSmith,
        bookMarkId,
        equivalent_pure_gold_weight_kpy,
        jewellery_type_size_id,
        order_qty,
        sample_id
    )
}