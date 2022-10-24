package com.critx.domain.useCase.giveGold

import com.critx.domain.repository.GiveGoldRepository
import javax.inject.Inject

class ServiceChargeUseCase @Inject constructor(
    private val giveGoldRepository: GiveGoldRepository
) {
    operator fun invoke(
        token: String,
        chargeAmount:String,
        wastageGm:String,
        invoice:String
    )=giveGoldRepository.serviceCharge(token,chargeAmount,wastageGm, invoice)
}