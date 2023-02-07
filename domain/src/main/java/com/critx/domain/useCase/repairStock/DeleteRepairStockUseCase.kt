package com.critx.domain.useCase.repairStock

import com.critx.domain.repository.RepairStockRepository
import okhttp3.RequestBody
import javax.inject.Inject

class DeleteRepairStockUseCase @Inject constructor(
    private val repairStockRepository: RepairStockRepository
) {
    operator fun invoke(
      repairStockId:String
    )=repairStockRepository.deleteRepairStock(repairStockId)
}