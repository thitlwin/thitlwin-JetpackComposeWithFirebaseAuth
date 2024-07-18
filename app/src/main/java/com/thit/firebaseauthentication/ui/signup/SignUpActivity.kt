package com.thit.firebaseauthentication.ui.signup

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.thit.firebaseauthentication.ui.login.AuthViewModel
import com.thit.firebaseauthentication.ui.login.LoginActivity
import com.thit.firebaseauthentication.ui.theme.AppTheme
import com.thit.firebaseauthentication.ui.verify.VerifyEmailActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val authViewModel = viewModel<AuthViewModel>()
            AppTheme {
                SignupScreen(
                    onEvent = authViewModel::onEvent,
                    onLoginClick = {
                        startActivity(LoginActivity.newInstance(this))
                    },
                    onSignUpSuccess = {
                        startActivity(VerifyEmailActivity.newInstance(this, it))
                    },
                )
            }
        }
    }

    companion object {
        const val TAG = "SignUpActivity"
        fun newInstance(context: Context) = Intent(context, SignUpActivity::class.java)
    }
}