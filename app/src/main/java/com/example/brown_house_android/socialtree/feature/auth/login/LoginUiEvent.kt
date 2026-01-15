package com.example.brown_house_android.socialtree.feature.auth.login

/**
 * UI Events for Login Screen
 */
sealed interface LoginUiEvent {
    data class EmailChanged(val email: String) : LoginUiEvent
    data class PasswordChanged(val password: String) : LoginUiEvent
    object LoginClicked : LoginUiEvent
    object SignUpClicked : LoginUiEvent
    object TogglePasswordVisibility : LoginUiEvent
}
