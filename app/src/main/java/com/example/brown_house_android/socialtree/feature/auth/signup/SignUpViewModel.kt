package com.example.brown_house_android.socialtree.feature.auth.signup

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
 * ViewModel for SignUp Screen
 */
class SignUpViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(SignUpUiState())
    val uiState: StateFlow<SignUpUiState> = _uiState.asStateFlow()

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: SignUpUiEvent) {
        when (event) {
            is SignUpUiEvent.NameChanged -> {
                _uiState.update { currentState ->
                    currentState.copy(
                        name = event.name,
                        isNameError = false,
                        nameErrorMessage = null,
                        isSignUpEnabled = validateSignUpEnabled(
                            event.name,
                            currentState.email,
                            currentState.password,
                            currentState.passwordConfirm
                        )
                    )
                }
            }

            is SignUpUiEvent.EmailChanged -> {
                _uiState.update { currentState ->
                    currentState.copy(
                        email = event.email,
                        isEmailError = false,
                        emailErrorMessage = null,
                        isSignUpEnabled = validateSignUpEnabled(
                            currentState.name,
                            event.email,
                            currentState.password,
                            currentState.passwordConfirm
                        )
                    )
                }
            }

            is SignUpUiEvent.PasswordChanged -> {
                _uiState.update { currentState ->
                    currentState.copy(
                        password = event.password,
                        isPasswordError = false,
                        passwordErrorMessage = null,
                        isSignUpEnabled = validateSignUpEnabled(
                            currentState.name,
                            currentState.email,
                            event.password,
                            currentState.passwordConfirm
                        )
                    )
                }
            }

            is SignUpUiEvent.PasswordConfirmChanged -> {
                _uiState.update { currentState ->
                    currentState.copy(
                        passwordConfirm = event.passwordConfirm,
                        isPasswordConfirmError = false,
                        passwordConfirmErrorMessage = null,
                        isSignUpEnabled = validateSignUpEnabled(
                            currentState.name,
                            currentState.email,
                            currentState.password,
                            event.passwordConfirm
                        )
                    )
                }
            }

            is SignUpUiEvent.SignUpClicked -> {
                performSignUp()
            }

            is SignUpUiEvent.LoginClicked -> {
                viewModelScope.launch {
                    _uiEvent.send(UiEvent.NavigateBack)
                }
            }
        }
    }

    private fun validateSignUpEnabled(
        name: String,
        email: String,
        password: String,
        passwordConfirm: String
    ): Boolean {
        return name.isNotBlank() &&
                email.isNotBlank() &&
                email.contains("@") &&
                password.length >= 6 &&
                passwordConfirm.length >= 6 &&
                password == passwordConfirm
    }

    private fun validateEmail(email: String): Boolean {
        return email.contains("@") && email.contains(".")
    }

    private fun performSignUp() {
        val currentState = _uiState.value

        // Validate name
        if (currentState.name.isBlank()) {
            _uiState.update {
                it.copy(
                    isNameError = true,
                    nameErrorMessage = "이름을 입력해주세요"
                )
            }
            return
        }

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

        // Validate password confirmation
        if (currentState.password != currentState.passwordConfirm) {
            _uiState.update {
                it.copy(
                    isPasswordConfirmError = true,
                    passwordConfirmErrorMessage = "비밀번호가 일치하지 않습니다"
                )
            }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            // Simulate sign up API call
            delay(1500)

            // For now, just navigate to home (no actual authentication)
            _uiEvent.send(UiEvent.Navigate(SocialTreeRoutes.Home.route))

            _uiState.update { it.copy(isLoading = false) }
        }
    }
}
