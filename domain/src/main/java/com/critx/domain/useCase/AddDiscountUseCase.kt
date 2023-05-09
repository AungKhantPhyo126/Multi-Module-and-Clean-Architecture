package com.critx.domain.useCase

import com.critx.domain.repository.ConfirmVoucherRepository
import javax.inject.Inject

class AddDiscountUseCase @Inject constructor(
    private val confirmVoucherRepository: ConfirmVoucherRepository
) {
    operator fun invoke(
        token: String,
        voucherCodes:List<String>,
        amount:String
    ) = confirmVoucherRepository.addDiscount(token,voucherCodes,amount)
}