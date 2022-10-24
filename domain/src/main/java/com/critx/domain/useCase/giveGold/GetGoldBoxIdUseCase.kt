package com.critx.domain.useCase.giveGold

import com.critx.domain.repository.GiveGoldRepository
import javax.inject.Inject

class GetGoldBoxIdUseCase @Inject constructor(
    private val giveGoldRepository: GiveGoldRepository
) {
    operator fun invoke(
        token: String,
    )=giveGoldRepository.getGoldBoxId(token)
}