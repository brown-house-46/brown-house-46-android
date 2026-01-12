package com.example.brown_house_android.socialtree.feature.node.detail

import com.example.brown_house_android.socialtree.core.model.NodeType
import com.example.brown_house_android.socialtree.core.model.SocialNode

/**
 * UI state for Node Detail screen.
 * Supports both generic node and person-specific views.
 */
data class NodeDetailUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val node: SocialNode? = null,
    val parentNode: SocialNode? = null,
    val childNodes: List<SocialNode> = emptyList(),
    val tags: List<String> = emptyList(),

    // Person-specific computed field
    val yearsTogetherText: String? = null  // e.g., "함께한 지 28년"
) {
    // Helper computed property
    val isPerson: Boolean
        get() = node?.type == NodeType.PERSON && node.personData != null
}
