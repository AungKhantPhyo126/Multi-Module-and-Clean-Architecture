package com.critx.domain.useCase.repairStock

import com.critx.domain.repository.RepairStockRepository
import javax.inject.Inject

class GetJobDoneDataUseCase @Inject constructor(
    private val repairStockRepository: RepairStockRepository
) {
    operator fun invoke(
        token: String, goldSmithId: String
    )=repairStockRepository.getJobDoneData(token, goldSmithId)
}