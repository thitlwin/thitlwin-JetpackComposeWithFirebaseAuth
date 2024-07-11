package com.thit.firebaseauthentication.ui.login

import androidx.lifecycle.viewModelScope
import com.thit.firebaseauthentication.data.domain.model.getErrorOrNull
import com.thit.firebaseauthentication.data.domain.model.getSuccessOrNull
import com.thit.firebaseauthentication.data.domain.model.succeeded
import com.thit.firebaseauthentication.data.domain.usecase.SignInUseCase
import com.thit.firebaseauthentication.data.domain.usecase.SignOutUseCase
import com.thit.firebaseauthentication.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val signInUseCase: SignInUseCase,
    private val signOutUseCase: SignOutUseCase
) : BaseViewModel<AuthUiState, AuthUIEvent>(AuthUiState()) {

    private fun signInAnonymously() = viewModelScope.launch((Dispatchers.IO)) {
//        val res = signInUseCase.invoke()
//        if (res.getSuccessOrNull() != null) {
//            val user = res.getSuccessOrNull()!!.user
//            updateUiState {
//                AuthUiState(
//                    user = user,
//                    isAnonymous = user?.isAnonymous ?: false,
//                    isAuthenticated = user != null,
//                    authState = if (user != null) {
//                        if (user.isAnonymous) AuthState.Authenticated else AuthState.SignedIn
//                    } else {
//                        AuthState.SignedOut
//                    }
//                )
//            }
//        }
    }

    private fun signOut() {
        viewModelScope.launch(Dispatchers.IO) {
            val res = signOutUseCase.invoke()
            if (res.succeeded) {
                updateUiState {
                    AuthUiState(
                        user = null,
                        isAnonymous = false,
                        isAuthenticated = false,
                        authState = AuthState.SignedOut
                    )
                }
            }
        }
    }

    override suspend fun handleEvent(event: AuthUIEvent) {
        when (event) {
            AuthUIEvent.SignInAnonymously -> signInAnonymously()
            AuthUIEvent.SignOut -> signOut()
            is AuthUIEvent.SignIn -> signIn(event.username, event.password)
        }
    }

    private fun signIn(username: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            updateUiState {
                AuthUiState(
                    isLoading = true
                )
            }
            val res = signInUseCase.invoke(username, password)
            if (res.getSuccessOrNull() != null) {
                val user = res.getSuccessOrNull()!!.user
                updateUiState {
                    AuthUiState(
                        isLoading = false,
                        user = user,
                        isAnonymous = user?.isAnonymous ?: false,
                        isAuthenticated = user != null,
                        authState = if (user != null) {
                            if (user.isAnonymous) AuthState.Authenticated else AuthState.SignedIn
                        } else {
                            AuthState.SignedOut
                        }
                    )
                }
            } else if (res.getErrorOrNull() != null) {
                updateUiState {
                    AuthUiState(
                        isLoading = false,
                        error = res.getErrorOrNull()!!.exception
                    )
                }
            }
        }
    }
}