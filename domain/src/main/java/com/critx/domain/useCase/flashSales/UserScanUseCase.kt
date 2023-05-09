package com.critx.domain.useCase.flashSales

import com.critx.domain.repository.FlashSaleRepository
import javax.inject.Inject

class UserScanUseCase @Inject constructor(
    private val flashSaleRepository: FlashSaleRepository
) {
    operator fun invoke(
        token: String,
        userCode:String
    ) = flashSaleRepository.userScan(token,userCode)
}