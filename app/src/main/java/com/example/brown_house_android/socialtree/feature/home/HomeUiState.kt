package com.example.brown_house_android.socialtree.feature.home

import com.example.brown_house_android.socialtree.core.model.SocialNode

/**
 * UI state for Home screen.
 */
data class HomeUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val nodes: List<SocialNode> = emptyList(),
    val selectedNode: SocialNode? = null
)
