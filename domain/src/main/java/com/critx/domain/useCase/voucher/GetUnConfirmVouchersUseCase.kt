package com.critx.domain.useCase.voucher

import com.critx.domain.repository.ConfirmVoucherRepository
import javax.inject.Inject

class GetUnConfirmVouchersUseCase  @Inject constructor(
    private val confirmVoucherRepository: ConfirmVoucherRepository
) {
    operator fun invoke(
        token: String,
        type:String
    ) = confirmVoucherRepository.getVouchers(token,type)
}