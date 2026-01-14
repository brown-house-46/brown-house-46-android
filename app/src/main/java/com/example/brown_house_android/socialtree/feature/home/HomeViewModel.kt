package com.example.brown_house_android.socialtree.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.brown_house_android.socialtree.core.model.NodeStatus
import com.example.brown_house_android.socialtree.core.model.NodeType
import com.example.brown_house_android.socialtree.core.model.PersonData
import com.example.brown_house_android.socialtree.core.model.PersonRelationship
import com.example.brown_house_android.socialtree.core.model.PhotoMetadata
import com.example.brown_house_android.socialtree.core.model.SocialNode
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel for Home Screen.
 */
class HomeViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadNodes()
    }

    fun selectNode(nodeId: String) {
        val node = _uiState.value.nodes.firstOrNull { it.id == nodeId }
        _uiState.update { it.copy(selectedNode = node) }
    }

    fun dismissSelectedNode() {
        _uiState.update { it.copy(selectedNode = null) }
    }

    fun retry() {
        loadNodes()
    }

    private fun loadNodes() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            delay(400)

            // TODO: Replace with real data source
            val sampleNodes = listOf(
                SocialNode(
                    id = "node-me",
                    name = "Me",
                    type = NodeType.PERSON,
                    status = NodeStatus.ACTIVE,
                    description = "나"
                ),
                SocialNode(
                    id = "node-1",
                    name = "공유방: 브라운하우스",
                    type = NodeType.ROOM,
                    status = NodeStatus.ACTIVE,
                    description = "가족 공유 공간"
                ),
                SocialNode(
                    id = "node-2",
                    name = "지민",
                    type = NodeType.PERSON,
                    status = NodeStatus.ACTIVE,
                    description = "가족 구성원",
                    personData = PersonData(
                        photoMetadataList = emptyList(),
                        relationship = PersonRelationship.FRIEND,
                        age = 26,
                        memo = "함께 자란 소중한 친구"
                    )
                ),
                SocialNode(
                    id = "node-3",
                    name = "기념일 모음",
                    type = NodeType.MEMORY,
                    status = NodeStatus.INACTIVE,
                    description = "사진과 기록"
                ),
                SocialNode(
                    id = "node-5",
                    name = "박민수",
                    type = NodeType.PERSON,
                    status = NodeStatus.ACTIVE,
                    description = "형",
                    personData = PersonData(
                        profileImageUrl = "android.resource://com.example.brown_house_android/drawable/ic_launcher_foreground",
                        photoMetadataList = emptyList(),
                        relationship = PersonRelationship.SIBLING,
                        birthYear = 1995,
                        phoneNumber = "010-3333-4444",
                        memo = "직장인, 게임 좋아함"
                    )
                ),
                SocialNode(
                    id = "node-6",
                    name = "이수진",
                    type = NodeType.PERSON,
                    status = NodeStatus.ACTIVE,
                    description = "친구",
                    personData = PersonData(
                        profileImageUrl = "android.resource://com.example.brown_house_android/drawable/ic_launcher_foreground",
                        photoMetadataList = listOf(
                            PhotoMetadata(
                                id = "photo-1",
                                uri = "android.resource://com.example.brown_house_android/drawable/ic_launcher_background",
                                takenAt = System.currentTimeMillis() - 86400000L * 30,
                                displayName = "여행사진.jpg"
                            ),
                            PhotoMetadata(
                                id = "photo-2",
                                uri = "android.resource://com.example.brown_house_android/drawable/ic_launcher_foreground",
                                takenAt = System.currentTimeMillis() - 86400000L * 15,
                                displayName = "카페.jpg"
                            )
                        ),
                        relationship = PersonRelationship.FRIEND,
                        age = 27,
                        phoneNumber = "010-5555-6666",
                        memo = "대학 동기, 여행 좋아함"
                    )
                )
            )

            _uiState.update {
                it.copy(
                    isLoading = false,
                    nodes = sampleNodes
                )
            }
        }
    }
}
