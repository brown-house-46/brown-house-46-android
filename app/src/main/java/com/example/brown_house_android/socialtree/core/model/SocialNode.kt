package com.example.brown_house_android.socialtree.core.model

/**
 * Basic node model for UI scaffolding.
 */

enum class NodeType(val label: String) {
    PERSON("사람"),
    ROOM("공유방"),
    MEMORY("기록"),
    GROUP("그룹")
}

enum class NodeStatus(val label: String) {
    ACTIVE("활성"),
    INACTIVE("비활성"),
    ARCHIVED("보관")
}

data class SocialNode(
    val id: String,
    val name: String,
    val type: NodeType,
    val status: NodeStatus,
    val description: String? = null,
    val personData: PersonData? = null  // Person-specific data (only for NodeType.PERSON)
)
