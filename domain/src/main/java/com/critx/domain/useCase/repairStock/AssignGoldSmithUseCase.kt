package com.critx.domain.useCase.repairStock

import com.critx.domain.repository.RepairStockRepository
import okhttp3.RequestBody
import javax.inject.Inject

class AssignGoldSmithUseCase @Inject constructor(
    private val repairStockRepository: RepairStockRepository
) {
    operator fun invoke(
        token: String,
        goldSmithId: RequestBody,
        jewelleryTypeId: RequestBody,
        repairJobId: RequestBody,
        quantity: RequestBody,
        weightGm: RequestBody
    )=repairStockRepository.assignGoldSmith(token, goldSmithId, jewelleryTypeId, repairJobId, quantity, weightGm)
}