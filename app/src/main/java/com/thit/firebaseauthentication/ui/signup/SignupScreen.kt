package com.thit.firebaseauthentication.ui.signup

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.thit.firebaseauthentication.ui.theme.AppTheme
import com.thit.firebaseauthentication.R
import com.thit.firebaseauthentication.ui.login.AuthUIEvent
import com.thit.firebaseauthentication.ui.login.AuthViewModel

@Composable
fun SignupScreen(
    onEvent: (AuthUIEvent) -> Unit,
    onLoginClick: () -> Unit,
    onSignUpSuccess: (email: String) -> Unit
) {
    val authViewModel = viewModel<AuthViewModel>()
    val authUiState = authViewModel.uiState.collectAsState().value

    Scaffold { paddingValues ->

        //TextFields
        var email by remember { mutableStateOf(TextFieldValue("saithitlwin+1@gmail.com")) }
        var password by remember { mutableStateOf(TextFieldValue("abcd1234")) }
        var confirmPassword by remember { mutableStateOf(TextFieldValue("abcd1234")) }
        var hasError by remember { mutableStateOf(authUiState.error != null) }
        var passwordVisualTransformation by remember {
            mutableStateOf<VisualTransformation>(
                PasswordVisualTransformation()
            )
        }
        var confirmPasswordVisualTransformation by remember {
            mutableStateOf<VisualTransformation>(
                PasswordVisualTransformation()
            )
        }
        val passwordInteractionState = remember { MutableInteractionSource() }
        val confirmPasswordInteractionState = remember { MutableInteractionSource() }
        val emailInteractionState = remember { MutableInteractionSource() }
        if (authUiState.alreadySignUp) {
            onSignUpSuccess(email.text)
        }
        if(authUiState.error != null) {
            Toast.makeText(LocalContext.current, authUiState.error.message, Toast.LENGTH_SHORT).show()
        }
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
                    text = "Sign Up",
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
                OutlinedTextField(
                    value = confirmPassword,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Confirm Password",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    },
                    trailingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_remove_red_eye_24),
                            contentDescription = "Eye",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.clickable(onClick = {
                                confirmPasswordVisualTransformation =
                                    if (confirmPasswordVisualTransformation != VisualTransformation.None) {
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
                    label = { Text(text = "Confirm Password") },
                    onValueChange = {
                        confirmPassword = it
                    },
                    interactionSource = confirmPasswordInteractionState,
                    visualTransformation = confirmPasswordVisualTransformation,
                )
            }
            item {
                Button(
                    onClick = {
                        if (invalidInput(email.text, password.text, confirmPassword.text)) {
                            hasError = true
                        } else {
                            hasError = false
                            onEvent(AuthUIEvent.SignUp(email.text, password.text))
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
                        Text(text = "Register")
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
                val primaryColor = MaterialTheme.colorScheme.primary
                val annotatedString = remember {
                    AnnotatedString.Builder("Already have account? Login")
                        .apply {
                            addStyle(style = SpanStyle(color = primaryColor), 22, 27)
                        }
                }
                Text(
                    text = annotatedString.toAnnotatedString(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                        .clickable(onClick = onLoginClick),
                    textAlign = TextAlign.Center
                )
            }

            item { Spacer(modifier = Modifier.height(100.dp)) }
        }
    }
}


fun invalidInput(email: String, password: String, confirmPassword: String): Boolean {
    if (email.isBlank() || password.isBlank() || confirmPassword.isBlank()) return true
    if (confirmPassword != password) return true
    return false
}

@Preview
@Composable
fun SignupScreenPreview() {
    AppTheme {
        SignupScreen(onEvent = {}, onSignUpSuccess = {}, onLoginClick = {})
    }
}