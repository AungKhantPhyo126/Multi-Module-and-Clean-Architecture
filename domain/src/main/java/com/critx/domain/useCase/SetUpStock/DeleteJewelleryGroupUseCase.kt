package com.critx.domain.useCase.SetUpStock

import com.critx.domain.repository.SetupStockRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class DeleteJewelleryGroupUseCase@Inject constructor(
    private val setupStockRepository: SetupStockRepository
) {
    operator fun invoke(
        token: String,
        method: RequestBody,
        groupId: String
    ) = setupStockRepository.deleteJewelleryGroup(
        token,
        method,
        groupId
    )
}