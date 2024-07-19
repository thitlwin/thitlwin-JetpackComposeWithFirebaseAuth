package com.thit.firebaseauthentication.ui.login.component

import android.content.Context
import android.content.Intent
import android.provider.Settings
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
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.thit.firebaseauthentication.R
import com.thit.firebaseauthentication.ui.login.AuthUIEvent
import com.thit.firebaseauthentication.ui.login.AuthViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.security.MessageDigest
import java.util.UUID


const val TAG = "GoogleAuth"

@Composable
fun SignInWithGoogle() {
    SignInWithGoogleButton()
}

@Composable
private fun SignInWithGoogleButton() {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val authViewModel = viewModel<AuthViewModel>()

    val startAddAccountIntentLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) {
            // Once the account has been added, do sign in again.
            doGoogleSignIn(authViewModel, coroutineScope, context, null)
        }

    OutlinedButton(
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
        onClick = {
            doGoogleSignIn(
                authViewModel,
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
    authViewModel: AuthViewModel,
    coroutineScope: CoroutineScope,
    context: Context,
    startAddAccountIntentLauncher: ManagedActivityResultLauncher<Intent, ActivityResult>?,
) {

    val credentialManager = CredentialManager.create(context)

    fun getGoogleIdOption(context: Context): GetGoogleIdOption {

        val rawNonce = UUID.randomUUID().toString()
        val bytes = rawNonce.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        val hashedNonce = digest.fold("") { str, it ->
            str + "%02x".format(it)
        }
        return GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false) // true - check if the user has any accounts that have previously been used to sign in to the app
            .setServerClientId(context.getString(R.string.web_client_id))
            .setAutoSelectEnabled(true) // true- Enable automatic sign-in for returning users
            .setNonce(hashedNonce)
            .build()
    }

    val googleSignRequest: GetCredentialRequest = GetCredentialRequest.Builder()
        .addCredentialOption(getGoogleIdOption(context))
        .build()

    coroutineScope.launch {
        try {
            val result = credentialManager.getCredential(
                request = googleSignRequest,
                context = context,
            )
            authViewModel.onEvent(AuthUIEvent.HandleSignInResult(result))
        } catch (e: NoCredentialException) {
            e.printStackTrace()
            // if there is no credential, request to add google account
            startAddAccountIntentLauncher?.launch(getAddGoogleAccountIntent())
        } catch (e: GetCredentialException) {
            e.printStackTrace()
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