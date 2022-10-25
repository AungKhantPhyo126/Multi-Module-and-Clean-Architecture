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
        weightK: String,
        weighP: String,
        weightY: String,
        goldBoxId: String,
        goldWeight: String,
        gemWeight: String,
        goldAndGemWeight:String,
        wastageK: String,
        wastageP: String,
        wastageY: String,
        dueDate: String,
        sampleList: List<String>?
    )=giveGoldRepository.giveGold(token, goldSmithId, orderItem, orderQty, weightK, weighP,
        weightY, goldBoxId, goldWeight, gemWeight,goldAndGemWeight, wastageK, wastageP, wastageY, dueDate, sampleList)
}