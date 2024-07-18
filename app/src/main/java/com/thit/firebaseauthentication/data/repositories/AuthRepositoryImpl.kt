package com.thit.firebaseauthentication.data.repositories

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import com.thit.firebaseauthentication.data.domain.model.SafeResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : AuthRepository {
    override fun getAuthState(viewModelScope: CoroutineScope): AuthStateResponse = callbackFlow {
        val authStateListener = AuthStateListener { auth ->
            trySend(auth.currentUser)
            Log.i("TAG", "User: ${auth.currentUser?.uid ?: "Not authenticated"}")
        }

        firebaseAuth.addAuthStateListener(authStateListener)

        awaitClose {
            firebaseAuth.removeAuthStateListener(authStateListener)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), firebaseAuth.currentUser)

    override suspend fun signUp(email: String, password: String): FirebaseSignUpResponse {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            result?.user?.let {
                Log.d("TAG", "Firebase SignUp Success: Email UID: ${it.uid}")
                Log.i("TAG", "isUserVerified: ${it.isEmailVerified}")
            }
            SafeResult.Success(result)
        } catch (error: Exception) {
            SafeResult.Error(error)
        }
    }

    override suspend fun signInWithEmailAndPassword(
        email: String,
        password: String
    ): FirebaseSignInResponse {
        return try {
            val authResult = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            authResult?.user?.let { user ->
                Log.i("TAG", "FirebaseAuthSuccess: Email UID: ${user.uid}")
                Log.i("TAG", "isUserVerified: ${user.isEmailVerified}")

            }
            SafeResult.Success(authResult)
        }catch (error: Exception) {
            Log.e("TAG", "FirebaseAuthError: Failed to Sign in with email and password")
            SafeResult.Error(error)
        }
    }

    override suspend fun signInAnonymously(): FirebaseSignInResponse {
        return try {
            val authResult = firebaseAuth.signInAnonymously().await()
            authResult?.user?.let { user ->
                Log.i("TAG", "FirebaseAuthSuccess: Anonymous UID: ${user.uid}")
                Log.i("TAG", "isUserVerified: ${user.isEmailVerified}")
            }
            SafeResult.Success(authResult)
        } catch (error: Exception) {
            Log.e("TAG", "FirebaseAuthError: Failed to Sign in anonymously")
            SafeResult.Error(error)
        }
    }

    override suspend fun signOut(): SignOutResponse {
        return try {
            firebaseAuth.signOut()
            SafeResult.Success(true)
        } catch (e: Exception) {
            SafeResult.Error(e)
        }
    }
}