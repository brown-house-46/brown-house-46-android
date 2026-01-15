package com.example.brown_house_android.socialtree.feature.auth.login

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.brown_house_android.socialtree.core.designsystem.foundation.BrownPasswordField
import com.example.brown_house_android.socialtree.core.designsystem.foundation.BrownPrimaryButton
import com.example.brown_house_android.socialtree.core.designsystem.foundation.BrownTextField
import com.example.brown_house_android.socialtree.core.designsystem.theme.SocialTreeTheme
import com.example.brown_house_android.socialtree.core.designsystem.tokens.BrownColor
import com.example.brown_house_android.socialtree.core.designsystem.tokens.BrownSpacing
import com.example.brown_house_android.socialtree.core.ui.UiEvent

/**
 * Login Screen
 * 로그인 페이지 UI (Issue #1)
 */
@Composable
fun LoginScreen(
    viewModel: LoginViewModel = viewModel(),
    onNavigateToSignUp: () -> Unit = {},
    onLoginSuccess: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    // Handle one-time events
    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.Navigate -> {
                    if (event.route.contains("home")) {
                        onLoginSuccess()
                    } else if (event.route.contains("signup")) {
                        onNavigateToSignUp()
                    }
                }
                else -> {}
            }
        }
    }

    LoginContent(
        uiState = uiState,
        onEvent = viewModel::onEvent
    )
}

@Composable
private fun LoginContent(
    uiState: LoginUiState,
    onEvent: (LoginUiEvent) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BrownColor.BackgroundLight)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(BrownSpacing.screenPadding)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo/Title Section
            Text(
                text = "Social Tree",
                style = MaterialTheme.typography.displayLarge,
                fontWeight = FontWeight.Bold,
                color = BrownColor.Primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "당신의 관계를 시각화하세요",
                style = MaterialTheme.typography.bodyMedium,
                color = BrownColor.TextSubLight
            )

            Spacer(modifier = Modifier.height(BrownSpacing.space12))

            // Email Field
            BrownTextField(
                value = uiState.email,
                onValueChange = { onEvent(LoginUiEvent.EmailChanged(it)) },
                label = "이메일",
                placeholder = "이메일을 입력하세요",
                leadingIcon = Icons.Default.Email,
                isError = uiState.isEmailError,
                errorMessage = uiState.emailErrorMessage,
                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                    keyboardType = KeyboardType.Email
                )
            )

            Spacer(modifier = Modifier.height(BrownSpacing.space4))

            // Password Field
            BrownPasswordField(
                value = uiState.password,
                onValueChange = { onEvent(LoginUiEvent.PasswordChanged(it)) },
                label = "비밀번호",
                isError = uiState.isPasswordError,
                errorMessage = uiState.passwordErrorMessage
            )

            Spacer(modifier = Modifier.height(BrownSpacing.space8))

            // Login Button
            BrownPrimaryButton(
                text = "로그인",
                onClick = { onEvent(LoginUiEvent.LoginClicked) },
                enabled = uiState.isLoginEnabled,
                isLoading = uiState.isLoading,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(BrownSpacing.space4))

            // Sign Up Link
            TextButton(
                onClick = { onEvent(LoginUiEvent.SignUpClicked) }
            ) {
                Text(
                    text = "계정이 없으신가요? 회원가입",
                    style = MaterialTheme.typography.bodyMedium,
                    color = BrownColor.Primary
                )
            }
        }
    }
}

// Previews
@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun LoginScreenPreview() {
    SocialTreeTheme {
        LoginContent(
            uiState = LoginUiState(
                email = "",
                password = "",
                isLoginEnabled = false
            ),
            onEvent = {}
        )
    }
}

@Preview(name = "Login Screen - Filled", showBackground = true)
@Composable
private fun LoginScreenFilledPreview() {
    SocialTreeTheme {
        LoginContent(
            uiState = LoginUiState(
                email = "user@example.com",
                password = "password123",
                isLoginEnabled = true
            ),
            onEvent = {}
        )
    }
}

@Preview(name = "Login Screen - Error", showBackground = true)
@Composable
private fun LoginScreenErrorPreview() {
    SocialTreeTheme {
        LoginContent(
            uiState = LoginUiState(
                email = "invalid@",
                password = "123",
                isEmailError = true,
                emailErrorMessage = "올바른 이메일 형식이 아닙니다",
                isPasswordError = true,
                passwordErrorMessage = "비밀번호는 최소 6자 이상이어야 합니다",
                isLoginEnabled = false
            ),
            onEvent = {}
        )
    }
}
