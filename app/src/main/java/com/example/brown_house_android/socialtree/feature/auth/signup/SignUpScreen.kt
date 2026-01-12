package com.example.brown_house_android.socialtree.feature.auth.signup

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
import androidx.compose.material.icons.filled.Person
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
 * SignUp Screen
 * 회원가입 페이지 UI (Issue #2)
 */
@Composable
fun SignUpScreen(
    viewModel: SignUpViewModel = viewModel(),
    onNavigateBack: () -> Unit = {},
    onSignUpSuccess: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    // Handle one-time events
    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.Navigate -> {
                    if (event.route.contains("home")) {
                        onSignUpSuccess()
                    }
                }
                is UiEvent.NavigateBack -> {
                    onNavigateBack()
                }
                else -> {}
            }
        }
    }

    SignUpContent(
        uiState = uiState,
        onEvent = viewModel::onEvent
    )
}

@Composable
private fun SignUpContent(
    uiState: SignUpUiState,
    onEvent: (SignUpUiEvent) -> Unit
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
            // Title
            Text(
                text = "회원가입",
                style = MaterialTheme.typography.displayLarge,
                fontWeight = FontWeight.Bold,
                color = BrownColor.Primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Social Tree와 함께 시작하세요",
                style = MaterialTheme.typography.bodyMedium,
                color = BrownColor.TextSubLight
            )

            Spacer(modifier = Modifier.height(BrownSpacing.space8))

            // Name Field
            BrownTextField(
                value = uiState.name,
                onValueChange = { onEvent(SignUpUiEvent.NameChanged(it)) },
                label = "이름",
                placeholder = "이름을 입력하세요",
                leadingIcon = Icons.Default.Person,
                isError = uiState.isNameError,
                errorMessage = uiState.nameErrorMessage
            )

            Spacer(modifier = Modifier.height(BrownSpacing.space4))

            // Email Field
            BrownTextField(
                value = uiState.email,
                onValueChange = { onEvent(SignUpUiEvent.EmailChanged(it)) },
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
                onValueChange = { onEvent(SignUpUiEvent.PasswordChanged(it)) },
                label = "비밀번호",
                placeholder = "비밀번호를 입력하세요 (최소 6자)",
                isError = uiState.isPasswordError,
                errorMessage = uiState.passwordErrorMessage
            )

            Spacer(modifier = Modifier.height(BrownSpacing.space4))

            // Password Confirm Field
            BrownPasswordField(
                value = uiState.passwordConfirm,
                onValueChange = { onEvent(SignUpUiEvent.PasswordConfirmChanged(it)) },
                label = "비밀번호 확인",
                placeholder = "비밀번호를 다시 입력하세요",
                isError = uiState.isPasswordConfirmError,
                errorMessage = uiState.passwordConfirmErrorMessage
            )

            Spacer(modifier = Modifier.height(BrownSpacing.space8))

            // SignUp Button
            BrownPrimaryButton(
                text = "회원가입",
                onClick = { onEvent(SignUpUiEvent.SignUpClicked) },
                enabled = uiState.isSignUpEnabled,
                isLoading = uiState.isLoading,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(BrownSpacing.space4))

            // Login Link
            TextButton(
                onClick = { onEvent(SignUpUiEvent.LoginClicked) }
            ) {
                Text(
                    text = "이미 계정이 있으신가요? 로그인",
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
private fun SignUpScreenPreview() {
    SocialTreeTheme {
        SignUpContent(
            uiState = SignUpUiState(
                name = "",
                email = "",
                password = "",
                passwordConfirm = "",
                isSignUpEnabled = false
            ),
            onEvent = {}
        )
    }
}

@Preview(name = "SignUp Screen - Filled", showBackground = true)
@Composable
private fun SignUpScreenFilledPreview() {
    SocialTreeTheme {
        SignUpContent(
            uiState = SignUpUiState(
                name = "홍길동",
                email = "hong@example.com",
                password = "password123",
                passwordConfirm = "password123",
                isSignUpEnabled = true
            ),
            onEvent = {}
        )
    }
}

@Preview(name = "SignUp Screen - Error", showBackground = true)
@Composable
private fun SignUpScreenErrorPreview() {
    SocialTreeTheme {
        SignUpContent(
            uiState = SignUpUiState(
                name = "",
                email = "invalid@",
                password = "123",
                passwordConfirm = "456",
                isNameError = true,
                nameErrorMessage = "이름을 입력해주세요",
                isEmailError = true,
                emailErrorMessage = "올바른 이메일 형식이 아닙니다",
                isPasswordError = true,
                passwordErrorMessage = "비밀번호는 최소 6자 이상이어야 합니다",
                isPasswordConfirmError = true,
                passwordConfirmErrorMessage = "비밀번호가 일치하지 않습니다",
                isSignUpEnabled = false
            ),
            onEvent = {}
        )
    }
}
