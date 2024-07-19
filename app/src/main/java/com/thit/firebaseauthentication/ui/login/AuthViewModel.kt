package com.thit.firebaseauthentication.ui.login

import android.util.Log
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialException
import androidx.lifecycle.viewModelScope
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.thit.firebaseauthentication.data.domain.model.SafeResult
import com.thit.firebaseauthentication.data.domain.model.getErrorOrNull
import com.thit.firebaseauthentication.data.domain.model.getSuccessOrNull
import com.thit.firebaseauthentication.data.domain.model.succeeded
import com.thit.firebaseauthentication.data.domain.usecase.SignInUseCase
import com.thit.firebaseauthentication.data.domain.usecase.SignOutUseCase
import com.thit.firebaseauthentication.data.domain.usecase.SignUpUseCase
import com.thit.firebaseauthentication.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val signInUseCase: SignInUseCase,
    private val signOutUseCase: SignOutUseCase,
    private val signUpUseCase: SignUpUseCase,
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
                        alreadySignUp = false,
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
            is AuthUIEvent.SignIn -> signIn(event.email, event.password)
            is AuthUIEvent.SignUp -> signUp(event.email, event.password)
            is AuthUIEvent.HandleSignInResult -> handleSignIn(event.result)
        }
    }

    private fun signUp(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            updateUiState { AuthUiState(isLoading = true) }
            val res = signUpUseCase.invoke(email, password)
            updateAuthUiState(res, isSignUpFlow = true)
        }
    }

    private fun signIn(username: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            updateUiState { AuthUiState(isLoading = true) }
            val res = signInUseCase.invoke(username, password)
            updateAuthUiState(res, isSignUpFlow = false)
        }
    }

    private fun updateAuthUiState(res: SafeResult<AuthResult>, isSignUpFlow: Boolean) {
        if (res.getSuccessOrNull() != null) {
            val user = res.getSuccessOrNull()!!.user
            if (isSignUpFlow) {
                user!!.sendEmailVerification()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d("TAG", "Verification email has been sent.")
                        }
                    }
            }
            updateUiState {
                AuthUiState(
                    alreadySignUp = isSignUpFlow,
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

    suspend fun handleSignIn(result: GetCredentialResponse) {
        // Handle the successfully returned credential.
        val credential = result.credential

        when (credential) {
            // GoogleIdToken credential
            is CustomCredential -> {
                if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    try {
                        // Use googleIdTokenCredential and extract id to validate and
                        // authenticate on your server.
                        val googleIdTokenCredential = GoogleIdTokenCredential
                            .createFrom(credential.data)
                        val googleIdToken = googleIdTokenCredential.idToken

                        val authCredential =
                            GoogleAuthProvider.getCredential(googleIdToken, null)
                        val user =
                            Firebase.auth.signInWithCredential(authCredential).await().user

                        user?.run {
                            updateUiState {
                                AuthUiState(
                                    alreadySignUp = true,
                                    isLoading = false,
                                    user = user,
                                    isAnonymous = user.isAnonymous,
                                    isAuthenticated = true,
                                    authState = if (user.isAnonymous) AuthState.Authenticated else AuthState.SignedIn
                                )
                            }
                        }
                    } catch (e: GoogleIdTokenParsingException) {
                        Log.e("TAG", "Received an invalid google id token response", e)
                    } catch (e: Exception) {
                        Log.e("TAG", "Unexpected error")
                    }
                }
            }

            else -> {
                // Catch any unrecognized credential type here.
                Log.e("TAG", "Unexpected type of credential")
            }
        }
    }

    private fun handleFailure(e: GetCredentialException) {
        Log.e("TAG", e.localizedMessage)
        updateUiState {
            AuthUiState(
                error = e
            )
        }
    }
}