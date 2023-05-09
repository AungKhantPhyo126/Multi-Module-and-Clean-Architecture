package com.critx.domain.useCase.voucher

import com.critx.domain.repository.ConfirmVoucherRepository
import com.critx.domain.repository.FlashSaleRepository
import javax.inject.Inject

class ScanDiscountVoucherUseCase @Inject constructor(
    private val confirmVoucherRepository: ConfirmVoucherRepository
) {
    operator fun invoke(
        token: String,
        voucherCode:String
    ) = confirmVoucherRepository.scanDiscountVoucher(token,voucherCode)
}