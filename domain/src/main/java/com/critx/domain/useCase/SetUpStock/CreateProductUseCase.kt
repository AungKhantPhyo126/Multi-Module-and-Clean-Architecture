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
        productCode:RequestBody,
        type: RequestBody,
        quality: RequestBody,
        group: RequestBody?,
        categoryId: RequestBody?,
        goldAndGemWeight: RequestBody?,
        gemWeightKyat: RequestBody?,
        gemWeightPae: RequestBody?,
        gemWeightYwae: RequestBody?,
        gemValue: RequestBody?,
        ptAndClipCost: RequestBody?,
        maintenanceCost: RequestBody?,
        diamondInfo: RequestBody?,
        diamondPriceFromGS: RequestBody?,
        diamondValueFromGS: RequestBody?,
        diamondPriceForSale: RequestBody?,
        diamondValueForSale: RequestBody?,
        images: List<MultipartBody.Part>,
        video: MultipartBody.Part?
    )=setupStockRepository.createProduct(token, name,productCode, type, quality, group, categoryId, goldAndGemWeight, gemWeightKyat, gemWeightPae, gemWeightYwae, gemValue, ptAndClipCost, maintenanceCost, diamondInfo, diamondPriceFromGS, diamondValueFromGS, diamondPriceForSale, diamondValueForSale, images, video)
}