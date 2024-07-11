package com.thit.firebaseauthentication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.thit.firebaseauthentication.ui.login.AuthViewModel
import com.thit.firebaseauthentication.ui.theme.AppTheme
import com.thit.firebaseauthentication.ui.login.LoginHomeScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val authViewModel: AuthViewModel = viewModel()
            AppTheme {
                LoginHomeScreen(
                    authUiState = authViewModel.uiState.collectAsState().value,
                    onEvent = { authViewModel.onEvent(it) })
            }
        }
    }
}