package com.thit.firebaseauthentication.ui.login

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Facebook
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.thit.firebaseauthentication.ui.theme.AppTheme
import com.thit.firebaseauthentication.R
import com.thit.firebaseauthentication.ui.login.component.SignInWithGoogle
import java.security.MessageDigest
import java.util.UUID

@Composable
fun LoginScreen(
    onRegisterClick: () -> Unit,
    onEvent: (AuthUIEvent) -> Unit,
    onLoginSuccess: () -> Unit
) {
    val authViewModel = viewModel<AuthViewModel>()
    val authUiState = authViewModel.uiState.collectAsState().value
    if (authUiState.authState == AuthState.Authenticated || authUiState.authState == AuthState.SignedIn) {
        onLoginSuccess()
    }


    Scaffold { paddingValues ->

        //TextFields
        var email by remember { mutableStateOf(TextFieldValue("koko@gmail.com")) }
        var password by remember { mutableStateOf(TextFieldValue("abcd1234")) }
        var hasError by remember { mutableStateOf(authUiState.error != null) }
        var passwordVisualTransformation by remember {
            mutableStateOf<VisualTransformation>(
                PasswordVisualTransformation()
            )
        }
        val passwordInteractionState = remember { MutableInteractionSource() }
        val emailInteractionState = remember { MutableInteractionSource() }

        LazyColumn(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            item { Spacer(modifier = Modifier.height(20.dp)) }
            item {
                Text(
                    text = "Login",
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.ExtraBold),
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            item {
                OutlinedTextField(
                    value = email,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Email, contentDescription = "Email",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    },
                    maxLines = 1,
                    isError = hasError,
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.primary
                    ),
                    label = { Text(text = "Email address") },
                    onValueChange = {
                        email = it
                    },
                    interactionSource = emailInteractionState,
                )
            }
            item {
                OutlinedTextField(
                    value = password,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Lock, contentDescription = "Password",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    },
                    trailingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_remove_red_eye_24),
                            contentDescription = "Eye",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.clickable(onClick = {
                                passwordVisualTransformation =
                                    if (passwordVisualTransformation != VisualTransformation.None) {
                                        VisualTransformation.None
                                    } else {
                                        PasswordVisualTransformation()
                                    }
                            }
                            )
                        )

                    },
                    colors = OutlinedTextFieldDefaults.colors(unfocusedBorderColor = MaterialTheme.colorScheme.primary),
                    maxLines = 1,
                    isError = hasError,
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    label = { Text(text = "Password") },
                    onValueChange = {
                        password = it
                    },
                    interactionSource = passwordInteractionState,
                    visualTransformation = passwordVisualTransformation,
                )
            }
            item {
                Button(
                    onClick = {
                        if (invalidInput(email.text, password.text)) {
                            hasError = true
                        } else {
                            hasError = false
                            onEvent(AuthUIEvent.SignIn(email.text, password.text))
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                        .height(50.dp)
                        .clip(CircleShape)
                ) {
                    if (authUiState.isLoading) {
                        CircularProgressIndicator(
                            color = Color.White
                        )
                    } else {
                        Text(text = "Log In")
                    }
                }
            }
            item {
                Box(modifier = Modifier.padding(vertical = 16.dp)) {
                    Spacer(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .height(1.dp)
                            .fillMaxWidth()
                            .background(Color.LightGray)
                    )
                    Text(
                        text = "Or use",
                        color = Color.LightGray,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .background(MaterialTheme.colorScheme.background)
                            .padding(horizontal = 16.dp)
                    )
                }
            }

            item {
                SignInWithGoogle()
            }

            item { Spacer(modifier = Modifier.height(8.dp)) }

            item {
                SignInWithFacebook()
            }

            item {
                val primaryColor = MaterialTheme.colorScheme.primary
                val annotatedString = remember {
                    AnnotatedString.Builder("Don't have an account? Register")
                        .apply {
                            addStyle(style = SpanStyle(color = primaryColor), 23, 31)
                        }
                }
                Text(
                    text = annotatedString.toAnnotatedString(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                        .clickable(onClick = onRegisterClick),
                    textAlign = TextAlign.Center
                )
            }

            item { Spacer(modifier = Modifier.height(100.dp)) }
        }
    }
}

@Composable
private fun SignInWithFacebook() {
    OutlinedButton(
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
        onClick = { }, modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
    ) {
        Icon(
            imageVector = Icons.Filled.Facebook,
            contentDescription = "Facebook"
        )
        Text(
            text = "Sign in with Facebook",
            style = MaterialTheme.typography.headlineSmall.copy(fontSize = 14.sp),
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}


fun invalidInput(email: String, password: String) =
    email.isBlank() || password.isBlank()

@Preview
@Composable
fun LoginScreenPreview() {
    AppTheme {
        LoginScreen(onRegisterClick = {}, onEvent = {}) {}
    }
}