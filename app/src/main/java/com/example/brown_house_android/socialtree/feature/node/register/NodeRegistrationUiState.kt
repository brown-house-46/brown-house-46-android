package com.example.brown_house_android.socialtree.feature.node.register

import com.example.brown_house_android.socialtree.core.model.BirthDateMode
import com.example.brown_house_android.socialtree.core.model.NodeType
import com.example.brown_house_android.socialtree.core.model.PersonRelationship

/**
 * UI state for Node Registration screen.
 * Supports both generic node and person-specific fields.
 */
data class NodeRegistrationUiState(
    // Common fields (all node types)
    val name: String = "",
    val description: String = "",
    val nodeType: NodeType = NodeType.PERSON,
    val parentId: String? = null,
    val parentOptions: List<ParentOption> = emptyList(),
    val tags: List<String> = emptyList(),
    val tagInput: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isSubmitEnabled: Boolean = false,

    // Person-specific fields (only used when nodeType == PERSON)
    val profileImageUrl: String? = null,
    val relationship: PersonRelationship = PersonRelationship.OTHER,
    val birthDateMode: BirthDateMode = BirthDateMode.AGE,
    val age: String = "",
    val birthYear: String = "",
    val phoneNumber: String = "",
    val memo: String = ""
)

data class ParentOption(
    val id: String,
    val name: String
)
