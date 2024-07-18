package com.thit.firebaseauthentication.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.thit.firebaseauthentication.ui.home.HomeActivity
import com.thit.firebaseauthentication.ui.signup.SignUpActivity
import com.thit.firebaseauthentication.ui.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val authViewModel = viewModel<AuthViewModel>()
            AppTheme {
                LoginScreen(
                    onRegisterClick = {
                        startActivity(SignUpActivity.newInstance(this))
                    },
                    onEvent = authViewModel::onEvent,
                    onLoginSuccess = {
                        startActivity(HomeActivity.newInstance(this))
                        finish()
                    },
                )
            }
        }
    }

    companion object {
        fun newInstance(context: Context) = Intent(context, LoginActivity::class.java)
    }
}