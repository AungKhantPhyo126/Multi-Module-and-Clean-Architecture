package com.critx.domain.useCase.sampleTakeAndReturn

import com.critx.domain.repository.SampleTakeAndReturnRepository
import javax.inject.Inject

class ReturnSampleUseCase @Inject constructor(
    private val sampleTakeAndReturnRepository: SampleTakeAndReturnRepository
){
    operator fun invoke(token:String,sampleId: List<String>)=sampleTakeAndReturnRepository.returnSample(token,sampleId)
}