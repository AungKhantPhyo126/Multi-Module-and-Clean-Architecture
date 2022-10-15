package com.critx.domain.useCase.sampleTakeAndReturn

import com.critx.domain.repository.SampleTakeAndReturnRepository
import javax.inject.Inject

class AddToHandedListUseCase @Inject constructor(
    private val sampleTakeAndReturnRepository: SampleTakeAndReturnRepository
){
    operator fun invoke(token:String,sampleId:List<String>)=sampleTakeAndReturnRepository.addToHandedList(token,sampleId)
}