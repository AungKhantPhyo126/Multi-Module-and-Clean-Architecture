package com.critx.domain.useCase.auth

import com.critx.domain.repository.AuthRepository
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(token:String)=authRepository.logout(token)
}