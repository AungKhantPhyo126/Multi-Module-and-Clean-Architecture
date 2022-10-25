package com.critx.domain.useCase.repairStock

import com.critx.domain.repository.RepairStockRepository
import javax.inject.Inject

class GetRepairJobUseCase @Inject constructor(
    private val repairStockRepository: RepairStockRepository
) {
    operator fun invoke(
        token: String, jewelleryTypeId: String
    )=repairStockRepository.getRepairJob(token, jewelleryTypeId)
}