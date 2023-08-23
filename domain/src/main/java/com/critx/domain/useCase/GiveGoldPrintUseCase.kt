package com.critx.domain.useCase

import com.critx.domain.repository.ConfirmVoucherRepository
import com.critx.domain.repository.GiveGoldRepository
import javax.inject.Inject

class GiveGoldPrintUseCase@Inject constructor(
    private val giveGoldRepository: GiveGoldRepository
) {
    operator fun invoke(
        token: String,
       voucherID:String
    ) = giveGoldRepository.getPdfPrint(token,voucherID)
}