package com.thit.firebaseauthentication.ui.login

sealed class AuthUIEvent {
    data class SignUp(val email: String, val password: String): AuthUIEvent()
    data class SignIn(val email: String, val password: String): AuthUIEvent()
    data object SignOut: AuthUIEvent()
    data object SignInAnonymously: AuthUIEvent()

//    data object ResendVerificationLink: AuthUIEvent()
}