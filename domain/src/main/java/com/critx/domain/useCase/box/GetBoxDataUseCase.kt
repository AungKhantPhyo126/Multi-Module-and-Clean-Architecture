package com.critx.domain.useCase.box

import com.critx.domain.repository.BoxRepository
import javax.inject.Inject

class GetBoxDataUseCase @Inject constructor(
    private val boxRepository: BoxRepository
) {
    operator fun invoke(token:String,boxCode:String)=boxRepository.getBoxData(token,boxCode)
}