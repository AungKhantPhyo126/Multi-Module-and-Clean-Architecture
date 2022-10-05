package com.critx.domain.useCase.sampleTakeAndReturn

import com.critx.domain.repository.SampleTakeAndReturnRepository
import javax.inject.Inject

class GetOutsideSampleUseCase @Inject constructor(
    private val sampleTakeAndReturnRepository: SampleTakeAndReturnRepository
){
    operator fun invoke(token:String)=sampleTakeAndReturnRepository.getOutsideSample(token)
}