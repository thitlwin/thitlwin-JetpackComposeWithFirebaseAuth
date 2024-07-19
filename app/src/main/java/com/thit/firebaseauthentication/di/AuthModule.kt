package com.thit.firebaseauthentication.di

import android.app.Application
import android.content.Context
import androidx.credentials.CredentialManager
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.firebase.auth.FirebaseAuth
import com.thit.firebaseauthentication.R
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth() = FirebaseAuth.getInstance()

//    @Provides
//    @Singleton
//    fun provideCredentialManager(application: Application) = CredentialManager.create(application)

//    @Provides
//    @Singleton
//    fun provideGoogleIdOptions(): GetGoogleIdOption = GetGoogleIdOption.Builder()
//        .setFilterByAuthorizedAccounts(true)
////        .setServerClientId(application.getString(R.string.default_web_client_id))
//        .setServerClientId("780123414032-5c838gb3vaimbjvf8b6t6e6r9gj3jg4b.apps.googleusercontent.com")
//        .setAutoSelectEnabled(true)
//        .setNonce("nonce string to use when generating a Google ID token")
//        .build()

//    val signInWithGoogleOption: GetSignInWithGoogleOption = GetSignInWithGoogleOption.Builder()
//        .setServerClientId(WEB_CLIENT_ID)
//        .setNonce(<nonce string to use when generating a Google ID token>)
//    .build()

    @Provides
    @Singleton
    fun provideSignInWithGoogleOption(application: Application) =
        GetSignInWithGoogleOption
            .Builder("780123414032-5c838gb3vaimbjvf8b6t6e6r9gj3jg4b.apps.googleusercontent.com")
//            .Builder(application.getString(R.string.web_client_id))
            .setNonce("nonce string to use when generating a Google ID token")
            .build()


}