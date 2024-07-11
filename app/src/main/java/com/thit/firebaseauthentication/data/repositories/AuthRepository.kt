package com.thit.firebaseauthentication.data.repositories

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import com.thit.firebaseauthentication.data.domain.model.SafeResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow

typealias FirebaseSignInResponse = SafeResult<AuthResult>
typealias SignOutResponse = SafeResult<Boolean>
typealias AuthStateResponse = StateFlow<FirebaseUser?>

interface AuthRepository {
    fun getAuthState(viewModelScope: CoroutineScope): AuthStateResponse

    suspend fun signInWithEmailAndPassword(email: String, password: String): FirebaseSignInResponse
    suspend fun signInAnonymously(): FirebaseSignInResponse

    suspend fun signOut(): SignOutResponse
}