package com.thit.firebaseauthentication.ui.login

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest

sealed class AuthUIEvent {
    data class SignUp(val email: String, val password: String) : AuthUIEvent()
    data class SignIn(val email: String, val password: String) : AuthUIEvent()
    data object SignOut : AuthUIEvent()
    data object SignInAnonymously : AuthUIEvent()

    //    data object ResendVerificationLink: AuthUIEvent()
    data class SignInWithGoogle(
        val credentialManager: CredentialManager,
        val request: GetCredentialRequest,
        val context: Context
    ) : AuthUIEvent()
}