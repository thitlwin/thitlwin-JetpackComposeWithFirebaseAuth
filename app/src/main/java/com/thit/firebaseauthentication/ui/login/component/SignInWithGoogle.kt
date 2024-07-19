package com.thit.firebaseauthentication.ui.login.component

import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.thit.firebaseauthentication.ui.login.AuthViewModel
import com.thit.firebaseauthentication.ui.login.getGoogleIdOption
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


const val TAG = "GoogleAuth"

@Composable
fun SignInWithGoogle(authViewModel: AuthViewModel) {
    SignInWithGoogleButton()
}

@Composable
private fun SignInWithGoogleButton() {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    val startAddAccountIntentLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) {
            // Once the account has been added, do sign in again.
            doGoogleSignIn(coroutineScope, context, null)
        }

    OutlinedButton(
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
        onClick = {
            doGoogleSignIn(
                coroutineScope,
                context,
                startAddAccountIntentLauncher
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
    ) {
        Icon(
            imageVector = Icons.Filled.Mail,
            contentDescription = "Gmail"
        )
        Text(
            text = "Sign in with Google",
            style = MaterialTheme.typography.headlineSmall.copy(fontSize = 14.sp),
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

private fun doGoogleSignIn(
    coroutineScope: CoroutineScope,
    context: Context,
    startAddAccountIntentLauncher: ManagedActivityResultLauncher<Intent, ActivityResult>?,
) {

    val credentialManager = CredentialManager.create(context)

    val googleSignRequest: GetCredentialRequest = GetCredentialRequest.Builder()
        .addCredentialOption(getGoogleIdOption(context))
        .build()

    coroutineScope.launch {
        try {
            val result = credentialManager.getCredential(
                request = googleSignRequest,
                context = context,
            )
            handleSignIn(result, context)
        } catch (e: NoCredentialException) {
            e.printStackTrace()
            // if there is no credential, request to add google account
            startAddAccountIntentLauncher?.launch(getAddGoogleAccountIntent())
        } catch (e: GetCredentialException) {
            e.printStackTrace()
        }
    }
}

suspend fun handleSignIn(result: GetCredentialResponse, context: Context) {
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
                    val googleCredentials =
                        GoogleAuthProvider.getCredential(googleIdToken, null)
                    val user =
                        Firebase.auth.signInWithCredential(googleCredentials).await().user

                    user?.run {
                        Toast.makeText(
                            context,
                            "You're signed in! as $displayName",
                            Toast.LENGTH_LONG
                        ).show()
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

fun getAddGoogleAccountIntent(): Intent {
    val intent = Intent(Settings.ACTION_ADD_ACCOUNT)
    intent.putExtra(Settings.EXTRA_ACCOUNT_TYPES, arrayOf("com.google"))
    return intent
}

@Preview
@Composable
fun SignupMethodContentScreenPreview() {
    MaterialTheme {
        SignInWithGoogleButton()
    }
}