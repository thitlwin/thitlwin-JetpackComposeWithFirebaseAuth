package com.thit.firebaseauthentication.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.thit.firebaseauthentication.ui.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : ComponentActivity() {
    val email: String by lazy {
        intent.getStringExtra(KEY_EMAIL) ?: ""
    }
    val name: String by lazy {
        intent.getStringExtra(KEY_NAME) ?: ""
    }
    val imageUrl: String by lazy {
        intent.getStringExtra(KEY_IMAGE_URL) ?: ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AppTheme {
                HomeScreen(name = name, imageUrl = imageUrl, email = email)
            }
        }
    }

    companion object {
        const val KEY_NAME = "NAME"
        const val KEY_EMAIL = "EMAIL"
        const val KEY_IMAGE_URL = "IMAGE_URL"

        fun newInstance(context: Context, name: String?, email: String?, imageUrl: String?) =
            Intent(context, HomeActivity::class.java).apply {
                putExtra(KEY_NAME, name)
                putExtra(KEY_EMAIL, email)
                putExtra(KEY_IMAGE_URL, imageUrl)
            }
    }
}