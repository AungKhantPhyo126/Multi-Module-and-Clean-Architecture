package com.critx.domain.useCase.sampleTakeAndReturn

import com.critx.domain.repository.SampleTakeAndReturnRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class SaveOutsideSampleUseCase@Inject constructor(
    private val sampleTakeAndReturnRepository: SampleTakeAndReturnRepository
){
    operator fun invoke(token: String,name:RequestBody?,weight:RequestBody?,specification:RequestBody?,image: MultipartBody.Part)
    =sampleTakeAndReturnRepository.saveOutsideSample(token,name, weight, specification, image)
}