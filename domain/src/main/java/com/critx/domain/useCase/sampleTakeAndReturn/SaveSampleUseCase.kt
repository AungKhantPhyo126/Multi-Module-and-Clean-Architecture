package com.critx.domain.useCase.sampleTakeAndReturn

import com.critx.domain.repository.SampleTakeAndReturnRepository
import javax.inject.Inject

class SaveSampleUseCase@Inject constructor(
    private val sampleTakeAndReturnRepository: SampleTakeAndReturnRepository
){
    operator fun invoke(token:String,sample:HashMap<String,String>)=sampleTakeAndReturnRepository.saveSample(token,sample)
}