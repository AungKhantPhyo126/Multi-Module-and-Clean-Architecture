package com.critx.domain.useCase.box

import com.critx.domain.repository.BoxRepository
import javax.inject.Inject

class ArrangeBoxesUseCase  @Inject constructor(
    private val boxRepository: BoxRepository
) {
    operator fun invoke(token:String,boxes:List<String>)=boxRepository.arrangeBox(token,boxes)
}