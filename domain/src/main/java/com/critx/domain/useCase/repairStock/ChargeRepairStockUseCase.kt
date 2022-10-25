package com.critx.domain.useCase.repairStock

import com.critx.domain.repository.RepairStockRepository
import okhttp3.RequestBody
import javax.inject.Inject

class ChargeRepairStockUseCase @Inject constructor(
    private val repairStockRepository: RepairStockRepository
) {
    operator fun invoke(
        token: String,
        amount: RequestBody,
        repairStockList: List<RequestBody>
    )=repairStockRepository.chargeRepairSTock(token, amount, repairStockList)
}