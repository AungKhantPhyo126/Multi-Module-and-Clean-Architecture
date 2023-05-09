package com.critx.domain.useCase.flashSales

import com.critx.domain.repository.FlashSaleRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class GetUserPointsUseCase @Inject constructor(
    private val flashSaleRepository: FlashSaleRepository
) {
    operator fun invoke(
        token: String,
    ) = flashSaleRepository.getUserPoint(token)
}