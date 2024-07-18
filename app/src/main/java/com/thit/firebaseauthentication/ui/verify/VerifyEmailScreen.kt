package com.thit.firebaseauthentication.ui.verify

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.thit.firebaseauthentication.R

@Composable
fun VerifyEmailScreen(
    email: String,
) {
    RegisterVerification(
        email = email,
        onResendClick = { },
    )
}

@Composable
fun RegisterVerification(
    email: String,
    modifier: Modifier = Modifier,
    onResendClick: () -> Unit,
) {
    Scaffold { paddingValues ->
        LazyColumn(
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            item {
                Text(
                    text = "Verification",
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.ExtraBold),
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
            item {

                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.Top),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Box(
                        modifier = Modifier
                    ) {
                        Box(
                            modifier = Modifier
                                .requiredSize(size = 122.dp)
                                .clip(shape = CircleShape)
                                .background(color = MaterialTheme.colorScheme.primary)
                        )
                        Box(
                            modifier = Modifier
                                .align(alignment = Alignment.TopStart)
                                .offset(
                                    x = 15.dp,
                                    y = 15.dp
                                )
                                .requiredSize(size = 92.dp)
                                .clip(shape = CircleShape)
                                .background(color = MaterialTheme.colorScheme.primary)
                        )
                        Image(
                            painter = painterResource(id = R.drawable.ic_baseline_email),
                            contentDescription = "ic:baseline-email",
                            colorFilter = ColorFilter.tint(Color.White),
                            modifier = Modifier
                                .align(alignment = Alignment.TopStart)
                                .offset(
                                    x = 43.dp,
                                    y = 43.dp
                                )
                                .requiredSize(size = 36.dp)
                        )
                    }
                    Text(
                        modifier = Modifier.padding(top = 30.dp),
                        text = "Verification Link",
                        color = Color(0xff1e1e1e),
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    )
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "We have sent the verification link to",
                            color = Color(0xffdbdbdb),
                            style = TextStyle(
                                fontSize = MaterialTheme.typography.bodySmall.fontSize,
                                fontWeight = FontWeight.Medium
                            )
                        )
                        Text(
                            text = email,
                            color = Color(0xff1e1e1e),
                            style = TextStyle(
                                fontSize = MaterialTheme.typography.bodySmall.fontSize,
                            )
                        )
                    }
                }
            }

            item {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.Start)
                ) {
                    Text(
                        text = "Didn’t receive the email?",
                        color = Color(0xffdbdbdb),
                        style = TextStyle(
                            fontSize = MaterialTheme.typography.bodySmall.fontSize,
                        )
                    )
                    Text(
                        text = "Resend",
                        color = MaterialTheme.colorScheme.primary,
                        style = TextStyle(
                            fontSize = MaterialTheme.typography.bodySmall.fontSize,
                        ),
                        modifier = Modifier.clickable {
                            onResendClick()
                        }
                    )
                }
            }
        }
    }
}


@Preview(widthDp = 360, heightDp = 800)
@Composable
private fun RegisterVerificationPreview() {
    RegisterVerification(email = "email", Modifier, {})
}