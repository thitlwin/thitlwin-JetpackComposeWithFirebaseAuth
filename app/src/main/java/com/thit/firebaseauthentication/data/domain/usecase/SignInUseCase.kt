package com.thit.firebaseauthentication.data.domain.usecase

import com.thit.firebaseauthentication.data.repositories.AuthRepository
import com.thit.firebaseauthentication.data.repositories.FirebaseSignInResponse
import javax.inject.Inject

class SignInUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): FirebaseSignInResponse {
        return authRepository.signInWithEmailAndPassword(email, password)
    }
}