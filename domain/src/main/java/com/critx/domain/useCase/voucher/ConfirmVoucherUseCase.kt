package com.critx.domain.useCase.voucher

import com.critx.domain.repository.ConfirmVoucherRepository
import javax.inject.Inject

class ConfirmVoucherUseCase @Inject constructor(
    private val confirmVoucherRepository: ConfirmVoucherRepository
) {
    operator fun invoke(
        token: String,
        voucherCode:String
    ) = confirmVoucherRepository.confirmVoucher(token,voucherCode)
}