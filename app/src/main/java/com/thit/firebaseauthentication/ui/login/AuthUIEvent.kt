package com.thit.firebaseauthentication.ui.login

sealed class AuthUIEvent {
    data class SignIn(val username: String, val password: String): AuthUIEvent()
    data object SignOut: AuthUIEvent()
    data object SignInAnonymously: AuthUIEvent()
}