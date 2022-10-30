package com.critx.domain.useCase.giveGold

import com.critx.domain.repository.GiveGoldRepository
import javax.inject.Inject

class GiveGoldScanUseCase @Inject constructor(
    private val giveGoldRepository: GiveGoldRepository
) {
    operator fun invoke(
        token: String,
        invoiceNumber:String
    )=giveGoldRepository.giveGoldScan(token,invoiceNumber)
}