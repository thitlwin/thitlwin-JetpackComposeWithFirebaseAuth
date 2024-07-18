package com.thit.firebaseauthentication.ui.login

import com.google.firebase.auth.FirebaseUser

data class AuthUiState(
    val alreadySignUp: Boolean = false,
    val isAnonymous: Boolean = false,
    val isAuthenticated: Boolean = false,
    val user: FirebaseUser? = null,
    val authState: AuthState? = null,
    val isLoading: Boolean = false,
    val error: Throwable? = null
)

enum class AuthState {
    Authenticated, // Anonymously authenticated in Firebase.
    SignedIn, // Authenticated in Firebase using one of service providers, and not anonymous.
    SignedOut; // Not authenticated in Firebase.
}