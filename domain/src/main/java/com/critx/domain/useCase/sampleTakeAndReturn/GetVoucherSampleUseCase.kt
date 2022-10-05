package com.critx.domain.useCase.sampleTakeAndReturn

import com.critx.domain.repository.SampleTakeAndReturnRepository
import javax.inject.Inject

class GetVoucherSampleUseCase  @Inject constructor(
    private val sampleTakeAndReturnRepository: SampleTakeAndReturnRepository
){
    operator fun invoke(token:String,invoiceId:String)=sampleTakeAndReturnRepository.getVoucherSample(token,invoiceId)
}