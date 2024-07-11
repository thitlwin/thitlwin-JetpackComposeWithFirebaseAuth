package com.thit.firebaseauthentication.data.domain.usecase

import com.thit.firebaseauthentication.data.repositories.AuthRepository
import com.thit.firebaseauthentication.data.repositories.SignOutResponse
import javax.inject.Inject

class SignOutUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): SignOutResponse {
        return authRepository.signOut()
    }
}