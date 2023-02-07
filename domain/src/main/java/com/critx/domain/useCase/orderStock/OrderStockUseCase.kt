package com.critx.domain.useCase.orderStock

import com.critx.domain.repository.OrderStockRepository
import okhttp3.MultipartBody
import javax.inject.Inject

class OrderStockUseCase @Inject constructor(
    private val orderStockRepository: OrderStockRepository
) {
    operator fun invoke(
        token: String,
        bookMarkAvgYwae: MultipartBody.Part?,
        orderAvgYwae: MultipartBody.Part?,
        bookMarkJewelleryTypeId: MultipartBody.Part?,
        bookMarkImage: MultipartBody.Part?,
        goldQuality: MultipartBody.Part,
        goldSmith: MultipartBody.Part,
        bookMarkId: MultipartBody.Part?,
        gsNewItemId: MultipartBody.Part?,
        equivalent_pure_gold_weight_kpy: MultipartBody.Part,
        jewellery_type_size_id: List<MultipartBody.Part>,
        order_qty: List<MultipartBody.Part>,
        sample_id: List<MultipartBody.Part>?,
        is_important:MultipartBody.Part?,
        custom_category_name:MultipartBody.Part?,

        ) = orderStockRepository.orderStock(
        token,
        bookMarkAvgYwae,
        orderAvgYwae,
        bookMarkJewelleryTypeId,
        bookMarkImage,
        goldQuality,
        goldSmith,
        bookMarkId,
        gsNewItemId,
        equivalent_pure_gold_weight_kpy,
        jewellery_type_size_id,
        order_qty,
        sample_id,
        is_important,
        custom_category_name
    )
}