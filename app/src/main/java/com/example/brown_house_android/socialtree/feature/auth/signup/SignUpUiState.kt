package com.example.brown_house_android.socialtree.feature.auth.signup

/**
 * UI State for SignUp Screen
 */
data class SignUpUiState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val passwordConfirm: String = "",
    val isNameError: Boolean = false,
    val isEmailError: Boolean = false,
    val isPasswordError: Boolean = false,
    val isPasswordConfirmError: Boolean = false,
    val nameErrorMessage: String? = null,
    val emailErrorMessage: String? = null,
    val passwordErrorMessage: String? = null,
    val passwordConfirmErrorMessage: String? = null,
    val isLoading: Boolean = false,
    val isSignUpEnabled: Boolean = false
)
