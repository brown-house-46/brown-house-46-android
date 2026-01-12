package com.example.brown_house_android.socialtree.core.ui

/**
 * Base sealed interface for one-time UI events
 * Used for navigation, snackbar messages, etc.
 */
sealed interface UiEvent {
    data class ShowSnackbar(val message: String) : UiEvent
    data class Navigate(val route: String) : UiEvent
    object NavigateBack : UiEvent
}
