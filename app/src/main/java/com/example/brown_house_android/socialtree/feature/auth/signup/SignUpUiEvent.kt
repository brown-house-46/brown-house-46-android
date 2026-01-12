package com.example.brown_house_android.socialtree.feature.auth.signup

/**
 * UI Events for SignUp Screen
 */
sealed interface SignUpUiEvent {
    data class NameChanged(val name: String) : SignUpUiEvent
    data class EmailChanged(val email: String) : SignUpUiEvent
    data class PasswordChanged(val password: String) : SignUpUiEvent
    data class PasswordConfirmChanged(val passwordConfirm: String) : SignUpUiEvent
    object SignUpClicked : SignUpUiEvent
    object LoginClicked : SignUpUiEvent
}
