package com.critx.domain.useCase.giveGold

import com.critx.domain.repository.GiveGoldRepository
import javax.inject.Inject

class GiveGoldUseCase @Inject constructor(
    private val giveGoldRepository: GiveGoldRepository
) {
    operator fun invoke(
        token: String,
        goldSmithId: String,
        orderItem: String,
        orderQty: String,
        weightY: String,
        goldBoxId: String,
        goldWeight: String,
        gemWeight: String,
        goldAndGemWeight:String,
        wastageY: String,
        dueDate: String?,
        sampleList: List<String>?
    )=giveGoldRepository.giveGold(token, goldSmithId, orderItem, orderQty,
        weightY, goldBoxId, goldWeight, gemWeight,goldAndGemWeight,wastageY, dueDate, sampleList)
}