package com.example.brown_house_android.socialtree.feature.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.brown_house_android.socialtree.core.navigation.SocialTreeRoutes
import com.example.brown_house_android.socialtree.core.ui.UiEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel for Login Screen
 */
class LoginViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: LoginUiEvent) {
        when (event) {
            is LoginUiEvent.EmailChanged -> {
                _uiState.update { currentState ->
                    currentState.copy(
                        email = event.email,
                        isEmailError = false,
                        emailErrorMessage = null,
                        isLoginEnabled = validateLoginEnabled(event.email, currentState.password)
                    )
                }
            }

            is LoginUiEvent.PasswordChanged -> {
                _uiState.update { currentState ->
                    currentState.copy(
                        password = event.password,
                        isPasswordError = false,
                        passwordErrorMessage = null,
                        isLoginEnabled = validateLoginEnabled(currentState.email, event.password)
                    )
                }
            }

            is LoginUiEvent.LoginClicked -> {
                performLogin()
            }

            is LoginUiEvent.SignUpClicked -> {
                viewModelScope.launch {
                    _uiEvent.send(UiEvent.Navigate(SocialTreeRoutes.SignUp.route))
                }
            }

            is LoginUiEvent.TogglePasswordVisibility -> {
                // Password visibility handled by BrownPasswordField internally
            }
        }
    }

    private fun validateLoginEnabled(email: String, password: String): Boolean {
        return email.isNotBlank() &&
                password.isNotBlank() &&
                email.contains("@") &&
                password.length >= 6
    }

    private fun validateEmail(email: String): Boolean {
        return email.contains("@") && email.contains(".")
    }

    private fun performLogin() {
        val currentState = _uiState.value

        // Validate email
        if (!validateEmail(currentState.email)) {
            _uiState.update {
                it.copy(
                    isEmailError = true,
                    emailErrorMessage = "올바른 이메일 형식이 아닙니다"
                )
            }
            return
        }

        // Validate password
        if (currentState.password.length < 6) {
            _uiState.update {
                it.copy(
                    isPasswordError = true,
                    passwordErrorMessage = "비밀번호는 최소 6자 이상이어야 합니다"
                )
            }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            // Simulate login API call
            delay(1500)

            // For now, just navigate to home (no actual authentication)
            _uiEvent.send(UiEvent.Navigate(SocialTreeRoutes.Home.route))

            _uiState.update { it.copy(isLoading = false) }
        }
    }
}
