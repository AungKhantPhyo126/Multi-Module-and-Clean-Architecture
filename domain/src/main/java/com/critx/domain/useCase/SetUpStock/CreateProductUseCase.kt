package com.critx.domain.useCase.SetUpStock

import com.critx.domain.repository.SetupStockRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class CreateProductUseCase @Inject constructor(
    private val setupStockRepository: SetupStockRepository
) {
    operator fun invoke(
        token: String,
        name: RequestBody?,
        productCode: RequestBody,
        type: RequestBody,
        quality: RequestBody,
        group: RequestBody?,
        categoryId: RequestBody?,
        goldAndGemWeight: RequestBody?,
        gemWeightYwae: RequestBody?,
        gemValue: RequestBody?,
        ptAndClipCost: RequestBody?,
        maintenanceCost: RequestBody?,
        diamondInfo: RequestBody?,
        diamondPriceFromGS: RequestBody?,
        diamondValueFromGS: RequestBody?,
        diamondPriceForSale: RequestBody?,
        diamondValueForSale: RequestBody?,
        image1: MultipartBody.Part?,
        image1Id: MultipartBody.Part?,
        image2: MultipartBody.Part?,
        image2Id: MultipartBody.Part?,
        image3: MultipartBody.Part?,
        image3Id: MultipartBody.Part?,
        gif: MultipartBody.Part?,
        gifId: MultipartBody.Part?,
        video: MultipartBody.Part?
    ) = setupStockRepository.createProduct(
        token,
        name,
        productCode,
        type,
        quality,
        group,
        categoryId,
        goldAndGemWeight,
        gemWeightYwae,
        gemValue,
        ptAndClipCost,
        maintenanceCost,
        diamondInfo,
        diamondPriceFromGS,
        diamondValueFromGS,
        diamondPriceForSale,
        diamondValueForSale,
       image1, image1Id, image2, image2Id, image3, image3Id, gif, gifId, video)
}