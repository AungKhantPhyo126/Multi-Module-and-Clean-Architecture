package com.critx.domain.useCase.flashSales

import com.critx.domain.repository.FlashSaleRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class ManualPointsAddorReduceUseCase @Inject constructor(
    private val flashSaleRepository: FlashSaleRepository
) {
    operator fun invoke(
        token: String,
        user_id:RequestBody,
        point:RequestBody,
        reason:RequestBody,
        action:RequestBody,
    ) = flashSaleRepository.manualPointsAddOrReduce(
        token,
  user_id, point, reason, action
    )
}