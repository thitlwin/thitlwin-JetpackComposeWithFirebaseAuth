package com.thit.firebaseauthentication.ui.verify

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.thit.firebaseauthentication.ui.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VerifyEmailActivity : ComponentActivity() {
    val email: String by lazy {
        intent.getStringExtra(EMAIL) ?: ""
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                VerifyEmailScreen(email = email)
            }
        }
    }

    companion object {
        const val TAG = "SignUpActivity"
        const val EMAIL = "email"
        fun newInstance(context: Context, email: String) = Intent(context, VerifyEmailActivity::class.java).apply {
            putExtra(EMAIL, email)
        }
    }
}