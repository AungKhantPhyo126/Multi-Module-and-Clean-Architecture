package com.critx.domain.useCase.SetUpStock

import com.critx.domain.repository.SetupStockRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class EditJewelleryCategoryUseCase @Inject constructor(
    private val setupStockRepository: SetupStockRepository
) {
    operator fun invoke(
        token: String,
        method: RequestBody,
        categoryId: String,
        jewellery_type_id: RequestBody,
        jewellery_quality_id: RequestBody,
        groupId: RequestBody,
        is_frequently_used: RequestBody,
        withGem: RequestBody,
        name: RequestBody,
        avgWeigh: RequestBody,
        avgYwae: RequestBody,
        image1: MultipartBody.Part?,
        image1Id: MultipartBody.Part?,
        image2: MultipartBody.Part?,
        image2Id: MultipartBody.Part?,
        image3: MultipartBody.Part?,
        image3Id: MultipartBody.Part?,
        gif: MultipartBody.Part?,
        gifId: MultipartBody.Part?,
        video: MultipartBody.Part?,
        specification: RequestBody,
        design: MutableList<RequestBody>,
        orderToGs: RequestBody,
        recommendCat: MutableList<RequestBody>

    ) = setupStockRepository.editJewelleryCategory(
        token,
        method,
        categoryId,
        jewellery_type_id,
        jewellery_quality_id,
        groupId,
        is_frequently_used,
        withGem,
        name,
        avgWeigh,
        avgYwae,
        image1,
        image1Id,
        image2,
        image2Id,
        image3,
        image3Id,
        gif,
        gifId,
        video,
        specification,
        design,
        orderToGs,
        recommendCat
    )
}