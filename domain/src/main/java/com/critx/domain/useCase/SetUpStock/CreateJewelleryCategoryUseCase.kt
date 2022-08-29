package com.critx.domain.useCase.SetUpStock

import com.critx.domain.repository.SetupStockRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject


class CreateJewelleryCategoryUseCase @Inject constructor(
    private val setupStockRepository: SetupStockRepository
) {
    operator fun invoke(
        token: String,
        jewellery_type_id: RequestBody,
        jewellery_quality_id: RequestBody,
        groupId: RequestBody,
        is_frequently_used: RequestBody,
        name: RequestBody,
        avgWeigh: RequestBody,
        avgKyat: RequestBody,
        avgPae: RequestBody,
        avgYwae: RequestBody,
        images: MutableList<MultipartBody.Part>,
        video: MultipartBody.Part,
        specification: RequestBody,
        design: MutableList<RequestBody>,
        orderToGs: RequestBody,
        recommendCat: MutableList<RequestBody>

    ) = setupStockRepository.createJewelleryCategory(
        token,
        jewellery_type_id,
        jewellery_quality_id,
        groupId,
        is_frequently_used,
        name,
        avgWeigh,
        avgKyat,
        avgPae,
        avgYwae,
        images,
        video,
        specification,
        design,
        orderToGs,
        recommendCat
    )
}