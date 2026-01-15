package com.example.brown_house_android.socialtree.feature.auth.login

/**
 * UI State for Login Screen
 */
data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isEmailError: Boolean = false,
    val isPasswordError: Boolean = false,
    val emailErrorMessage: String? = null,
    val passwordErrorMessage: String? = null,
    val isLoading: Boolean = false,
    val isLoginEnabled: Boolean = false
)
