package com.example.brown_house_android.socialtree.core.ui

/**
 * Base sealed interface for UI states
 * Can be used for loading states across the app
 */
sealed interface UiState<out T> {
    object Idle : UiState<Nothing>
    object Loading : UiState<Nothing>
    data class Success<T>(val data: T) : UiState<T>
    data class Error(val message: String, val throwable: Throwable? = null) : UiState<Nothing>
}
