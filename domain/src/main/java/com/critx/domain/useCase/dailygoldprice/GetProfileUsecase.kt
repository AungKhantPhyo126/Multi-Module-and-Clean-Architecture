package com.critx.domain.useCase.dailygoldprice

import com.critx.domain.repository.AuthRepository
import javax.inject.Inject

class GetProfileUsecase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(token:String)=authRepository.getProfile(token)
}