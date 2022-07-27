package com.critx.domain.useCase.auth

import com.critx.domain.repository.AuthRepository
import javax.inject.Inject

class LogInUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(userName:String,password:String)=authRepository.login(userName,password)
}