package com.critx.domain.useCase.sampleTakeAndReturn

import com.critx.domain.repository.SampleTakeAndReturnRepository
import okhttp3.MultipartBody
import javax.inject.Inject

class SaveOutsideSampleUseCase@Inject constructor(
    private val sampleTakeAndReturnRepository: SampleTakeAndReturnRepository
){
    operator fun invoke(token: String,name:String,weight:String,specification:String,image: MultipartBody.Part)
    =sampleTakeAndReturnRepository.saveOutsideSample(token,name, weight, specification, image)
}