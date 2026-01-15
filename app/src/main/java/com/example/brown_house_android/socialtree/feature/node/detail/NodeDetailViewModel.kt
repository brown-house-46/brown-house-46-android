package com.example.brown_house_android.socialtree.feature.node.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.brown_house_android.socialtree.core.model.BirthDate
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
import java.time.Year

/**
 * ViewModel for Node Detail Screen.
 */
class NodeDetailViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(NodeDetailUiState())
    val uiState: StateFlow<NodeDetailUiState> = _uiState.asStateFlow()

    fun loadNode(nodeId: String) {
        if (_uiState.value.node?.id == nodeId) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            delay(300)

            val node = sampleNodes().firstOrNull { it.id == nodeId }
            if (node == null) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "노드를 찾을 수 없습니다"
                    )
                }
                return@launch
            }

            _uiState.update {
                it.copy(
                    isLoading = false,
                    node = node,
                    parentNode = sampleNodes().firstOrNull { it.id == "node-1" },
                    childNodes = sampleNodes().filter { it.id != node.id }.take(2),
                    tags = listOf("가족", "공유", "기록"),
                    yearsTogetherText = calculateYearsTogether(node.personData?.birthDate)
                )
            }
        }
    }

    // Person-specific actions
    fun onDeleteNode() {
        // TODO: Implement delete functionality
    }

    fun onCallPhone() {
        // TODO: Implement phone call intent
    }

    fun onSendSMS() {
        // TODO: Implement SMS intent
    }

    private fun calculateYearsTogether(birthDate: BirthDate?): String? {
        if (birthDate == null) return null

        // Extract year from solar date string (e.g., "1965년 4월 5일")
        val yearMatch = """(\d{4})년""".toRegex().find(birthDate.solarDate)
        val birthYear = yearMatch?.groupValues?.get(1)?.toIntOrNull() ?: return null

        val currentYear = Year.now().value
        val years = currentYear - birthYear

        return "함께한 지 ${years}년"
    }

    private fun sampleNodes() = listOf(
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
                birthDate = BirthDate(
                    solarDate = "1998년 10월 13일",
                    lunarDate = "음력 8월 23일"
                ),
                phoneNumber = "010-9876-5432",
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
            id = "node-4",
            name = "김서연",
            type = NodeType.PERSON,
            status = NodeStatus.ACTIVE,
            description = "어머니",
            personData = PersonData(
                profileImageUrl = "android.resource://com.example.brown_house_android/drawable/ic_launcher_foreground",
                photoMetadataList = listOf(
                    PhotoMetadata(
                        id = "photo-1",
                        uri = "android.resource://com.example.brown_house_android/drawable/ic_launcher_background",
                        takenAt = System.currentTimeMillis() - 86400000L * 60,
                        displayName = "어머니_생신.jpg"
                    ),
                    PhotoMetadata(
                        id = "photo-2",
                        uri = "android.resource://com.example.brown_house_android/drawable/ic_launcher_foreground",
                        takenAt = System.currentTimeMillis() - 86400000L * 45,
                        displayName = "가족여행.jpg"
                    ),
                    PhotoMetadata(
                        id = "photo-3",
                        uri = "android.resource://com.example.brown_house_android/drawable/ic_launcher_background",
                        takenAt = System.currentTimeMillis() - 86400000L * 30,
                        displayName = "꽃구경.jpg"
                    )
                ),
                relationship = PersonRelationship.PARENT,
                birthDate = BirthDate(
                    solarDate = "1965년 4월 5일",
                    lunarDate = "음력 3월 5일"
                ),
                phoneNumber = "010-1234-5678",
                memo = "꽃을 좋아하시고 요리를 잘하신다. 특히 봄에는 프리지아를 가장 좋아하심. 주말마다 등산을 즐기시는 편."
            )
        )
    )
}
