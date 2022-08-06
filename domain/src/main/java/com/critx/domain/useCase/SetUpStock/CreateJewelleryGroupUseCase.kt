package com.critx.domain.useCase.SetUpStock

import com.critx.domain.repository.SetupStockRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class CreateJewelleryGroupUseCase @Inject constructor(
    private val setupStockRepository: SetupStockRepository
) {
    operator fun invoke(
        token: String,
        image: MultipartBody.Part,
        jewellery_type_id : RequestBody,
        jewellery_quality_id : RequestBody,
        is_frequently_used : RequestBody,
        name : RequestBody
    )=setupStockRepository.createJewelleryGroup(token,image, jewellery_type_id, jewellery_quality_id, is_frequently_used, name)
}