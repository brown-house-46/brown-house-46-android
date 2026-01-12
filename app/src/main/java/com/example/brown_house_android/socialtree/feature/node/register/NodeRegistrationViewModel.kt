package com.example.brown_house_android.socialtree.feature.node.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.brown_house_android.socialtree.core.model.BirthDateMode
import com.example.brown_house_android.socialtree.core.model.NodeType
import com.example.brown_house_android.socialtree.core.model.PersonRelationship
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
 * ViewModel for Node Registration Screen.
 */
class NodeRegistrationViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(NodeRegistrationUiState())
    val uiState: StateFlow<NodeRegistrationUiState> = _uiState.asStateFlow()

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        loadParentOptions()
    }

    fun setParentId(parentId: String?) {
        _uiState.update { it.copy(parentId = parentId) }
    }

    fun onNameChanged(value: String) {
        _uiState.update {
            it.copy(
                name = value,
                isSubmitEnabled = validate(value, it.nodeType, it.relationship)
            )
        }
    }

    fun onDescriptionChanged(value: String) {
        _uiState.update { it.copy(description = value) }
    }

    fun onNodeTypeSelected(type: NodeType) {
        _uiState.update {
            it.copy(
                nodeType = type,
                isSubmitEnabled = validate(it.name, type, it.relationship)
            )
        }
    }

    fun onParentSelected(parentId: String?) {
        _uiState.update { it.copy(parentId = parentId) }
    }

    fun onTagInputChanged(value: String) {
        _uiState.update { it.copy(tagInput = value) }
    }

    fun onAddTag() {
        val tag = _uiState.value.tagInput.trim()
        if (tag.isNotEmpty() && !_uiState.value.tags.contains(tag)) {
            _uiState.update { state ->
                state.copy(
                    tags = state.tags + tag,
                    tagInput = ""
                )
            }
        }
    }

    fun onRemoveTag(tag: String) {
        _uiState.update { state ->
            state.copy(tags = state.tags.filterNot { it == tag })
        }
    }

    // Person-specific functions
    fun onProfilePhotoSelected(uri: String) {
        _uiState.update { it.copy(profileImageUrl = uri) }
    }

    fun onRelationshipSelected(relationship: PersonRelationship) {
        _uiState.update {
            it.copy(
                relationship = relationship,
                isSubmitEnabled = validate(it.name, it.nodeType, relationship)
            )
        }
    }

    fun onBirthDateModeChanged(mode: BirthDateMode) {
        _uiState.update { it.copy(birthDateMode = mode) }
    }

    fun onAgeChanged(value: String) {
        _uiState.update { it.copy(age = value) }
    }

    fun onBirthYearChanged(value: String) {
        _uiState.update { it.copy(birthYear = value) }
    }

    fun onPhoneNumberChanged(value: String) {
        _uiState.update { it.copy(phoneNumber = value) }
    }

    fun onMemoChanged(value: String) {
        _uiState.update { it.copy(memo = value) }
    }

    fun onSubmit() {
        if (!_uiState.value.isSubmitEnabled) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            delay(600)
            _uiState.update { it.copy(isLoading = false) }
            _uiEvent.send(UiEvent.Navigate(SocialTreeRoutes.Home.route))
        }
    }

    fun onCancel() {
        viewModelScope.launch {
            _uiEvent.send(UiEvent.NavigateBack)
        }
    }

    private fun validate(
        name: String,
        type: NodeType,
        relationship: PersonRelationship
    ): Boolean {
        // For PERSON nodes: require name and relationship
        if (type == NodeType.PERSON) {
            return name.isNotBlank() && relationship.label.isNotBlank()
        }
        // For other node types: just require name
        return name.isNotBlank()
    }

    private fun loadParentOptions() {
        _uiState.update {
            it.copy(
                parentOptions = listOf(
                    ParentOption("root", "루트 (없음)"),
                    ParentOption("node-1", "공유방: 브라운하우스"),
                    ParentOption("node-2", "지민")
                )
            )
        }
    }
}
