package com.example.brown_house_android.socialtree.feature.node.detail

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Message
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.brown_house_android.socialtree.core.designsystem.foundation.BrownCard
import com.example.brown_house_android.socialtree.core.designsystem.foundation.BrownIconButton
import com.example.brown_house_android.socialtree.core.designsystem.foundation.BrownInfoCard
import com.example.brown_house_android.socialtree.core.designsystem.theme.SocialTreeTheme
import com.example.brown_house_android.socialtree.core.designsystem.tokens.BrownColor
import com.example.brown_house_android.socialtree.core.designsystem.tokens.BrownSpacing
import com.example.brown_house_android.socialtree.core.model.BirthDate
import com.example.brown_house_android.socialtree.core.model.NodeStatus
import com.example.brown_house_android.socialtree.core.model.NodeType
import com.example.brown_house_android.socialtree.core.model.PersonData
import com.example.brown_house_android.socialtree.core.model.PersonRelationship
import com.example.brown_house_android.socialtree.core.model.SocialNode

/**
 * Node Detail Screen
 * Redesigned to match HTML document design for person nodes
 */
@Composable
fun NodeDetailScreen(
    nodeId: String,
    viewModel: NodeDetailViewModel = viewModel(),
    onNavigateBack: () -> Unit = {},
    onEditNode: (String) -> Unit = {},
    onAddChildNode: (String) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(nodeId) {
        if (uiState.node == null || uiState.node!!.id != nodeId) {
            viewModel.loadNode(nodeId)
        }
    }

    NodeDetailContent(
        uiState = uiState,
        onNavigateBack = onNavigateBack,
        onEditNode = { onEditNode(nodeId) },
        onDeleteNode = viewModel::onDeleteNode,
        onCallPhone = viewModel::onCallPhone,
        onSendSMS = viewModel::onSendSMS
    )
}

@Composable
private fun NodeDetailContent(
    uiState: NodeDetailUiState,
    onNavigateBack: () -> Unit,
    onEditNode: () -> Unit,
    onDeleteNode: () -> Unit,
    onCallPhone: () -> Unit,
    onSendSMS: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BrownColor.DetailBgLight)
    ) {
        when {
            uiState.isLoading -> {
                LoadingState()
            }
            uiState.errorMessage != null -> {
                ErrorState(
                    message = uiState.errorMessage,
                    onNavigateBack = onNavigateBack
                )
            }
            uiState.node != null -> {
                val node = uiState.node
                if (node.type == NodeType.PERSON && node.personData != null) {
                    // Person-centric UI
                    PersonDetailView(
                        node = node,
                        personData = node.personData,
                        yearsTogetherText = uiState.yearsTogetherText,
                        onNavigateBack = onNavigateBack,
                        onEditNode = onEditNode,
                        onDeleteNode = onDeleteNode,
                        onCallPhone = onCallPhone,
                        onSendSMS = onSendSMS
                    )
                } else {
                    // Generic node UI (placeholder)
                    GenericNodeView(
                        node = node,
                        onNavigateBack = onNavigateBack,
                        onEditNode = onEditNode
                    )
                }
            }
        }
    }
}

@Composable
private fun PersonDetailView(
    node: SocialNode,
    personData: PersonData,
    yearsTogetherText: String?,
    onNavigateBack: () -> Unit,
    onEditNode: () -> Unit,
    onDeleteNode: () -> Unit,
    onCallPhone: () -> Unit,
    onSendSMS: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Header
        DetailHeader(
            onNavigateBack = onNavigateBack,
            onEdit = onEditNode
        )

        // Scrollable content
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(BrownSpacing.space4),
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = BrownSpacing.space6)
                .padding(top = BrownSpacing.space2, bottom = BrownSpacing.space8)
        ) {
            Spacer(modifier = Modifier.height(BrownSpacing.space6))

            // Profile Section
            PersonProfileSection(
                name = node.name,
                relationship = personData.relationship.label,
                imageUrl = personData.profileImageUrl,
                yearsTogetherText = yearsTogetherText
            )

            Spacer(modifier = Modifier.height(BrownSpacing.space4))

            // Birthday Card
            if (personData.birthDate != null) {
                BrownInfoCard(
                    icon = Icons.Default.Cake,
                    title = "생년월일"
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text(
                            text = personData.birthDate.solarDate,
                            style = MaterialTheme.typography.bodyMedium,
                            color = BrownColor.DetailTextMain,
                            fontWeight = FontWeight.Medium
                        )
                        if (personData.birthDate.lunarDate != null) {
                            Text(
                                text = personData.birthDate.lunarDate,
                                style = MaterialTheme.typography.labelSmall,
                                color = BrownColor.DetailTextSub
                            )
                        }
                    }
                }
            }

            // Contact Card
            if (personData.phoneNumber != null) {
                BrownInfoCard(
                    icon = Icons.Default.Call,
                    title = "연락처",
                    action = {
                        BrownIconButton(
                            icon = Icons.Default.Message,
                            contentDescription = "SMS",
                            onClick = onSendSMS
                        )
                    }
                ) {
                    Text(
                        text = personData.phoneNumber,
                        style = MaterialTheme.typography.bodyMedium,
                        color = BrownColor.DetailTextMain,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Memo Card
            if (personData.memo != null) {
                BrownInfoCard(
                    icon = Icons.Default.Edit,
                    title = "메모"
                ) {
                    Text(
                        text = personData.memo,
                        style = MaterialTheme.typography.bodyMedium,
                        color = BrownColor.DetailTextMain,
                        lineHeight = MaterialTheme.typography.bodyMedium.lineHeight.times(1.5f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(BrownSpacing.space8))

            // Delete Button
            DeleteTreeButton(onClick = onDeleteNode)
        }
    }
}

@Composable
private fun DetailHeader(
    onNavigateBack: () -> Unit,
    onEdit: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(BrownColor.DetailBgLight.copy(alpha = 0.9f))
            .padding(horizontal = BrownSpacing.space4, vertical = BrownSpacing.space3)
    ) {
        // Back button
        BrownIconButton(
            icon = Icons.Default.ArrowBack,
            contentDescription = "뒤로가기",
            onClick = onNavigateBack,
            modifier = Modifier.align(Alignment.CenterStart)
        )

        // Edit button
        BrownIconButton(
            icon = Icons.Default.Edit,
            contentDescription = "편집",
            onClick = onEdit,
            modifier = Modifier.align(Alignment.CenterEnd)
        )
    }
}

@Composable
private fun PersonProfileSection(
    name: String,
    relationship: String,
    imageUrl: String?,
    yearsTogetherText: String?
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(BrownSpacing.space8),
        modifier = Modifier.padding(vertical = BrownSpacing.space6)
    ) {
        Box(
            contentAlignment = Alignment.BottomCenter
        ) {
            // Large profile photo
            Box(
                modifier = Modifier
                    .size(136.dp)
                    .shadow(
                        elevation = 8.dp,
                        shape = CircleShape,
                        ambientColor = BrownColor.DetailPrimary.copy(alpha = 0.06f)
                    )
                    .border(
                        width = 4.dp,
                        color = Color.White,
                        shape = CircleShape
                    )
                    .clip(CircleShape)
                    .background(BrownColor.InputAccent)
            ) {
                if (imageUrl != null) {
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = "Profile photo",
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            // Relationship badge
            Box(
                modifier = Modifier
                    .offset(y = 12.dp)
                    .shadow(
                        elevation = 2.dp,
                        shape = CircleShape
                    )
                    .background(
                        color = Color.White,
                        shape = CircleShape
                    )
                    .border(
                        width = 1.dp,
                        color = BrownColor.DetailBorder,
                        shape = CircleShape
                    )
                    .padding(horizontal = BrownSpacing.space4, vertical = 6.dp)
            ) {
                Text(
                    text = relationship,
                    style = MaterialTheme.typography.labelSmall,
                    color = BrownColor.DetailTextSub,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Name and years together
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(BrownSpacing.space1),
            modifier = Modifier.padding(top = BrownSpacing.space4)
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.headlineMedium,
                color = BrownColor.DetailTextMain,
                fontWeight = FontWeight.Bold
            )

            if (yearsTogetherText != null) {
                Text(
                    text = yearsTogetherText,
                    style = MaterialTheme.typography.bodySmall,
                    color = BrownColor.DetailTextSub,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun DeleteTreeButton(
    onClick: () -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .background(Color.Transparent)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(BrownSpacing.space2),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = null,
                tint = BrownColor.DetailTextSub,
                modifier = Modifier.size(18.dp)
            )
            Text(
                text = "이 나무 삭제하기",
                style = MaterialTheme.typography.bodySmall,
                color = BrownColor.DetailTextSub,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun GenericNodeView(
    node: SocialNode,
    onNavigateBack: () -> Unit,
    onEditNode: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(BrownSpacing.space4)
    ) {
        DetailHeader(
            onNavigateBack = onNavigateBack,
            onEdit = onEditNode
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(BrownSpacing.space4)
        ) {
            Text(
                text = node.name,
                style = MaterialTheme.typography.headlineMedium,
                color = BrownColor.DetailTextMain
            )
            if (node.description != null) {
                Spacer(modifier = Modifier.height(BrownSpacing.space4))
                Text(
                    text = node.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = BrownColor.DetailTextSub
                )
            }
        }
    }
}

@Composable
private fun LoadingState() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        CircularProgressIndicator(color = BrownColor.DetailPrimary)
    }
}

@Composable
private fun ErrorState(
    message: String,
    onNavigateBack: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(BrownSpacing.space4)
    ) {
        BrownCard(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(BrownSpacing.space3),
                modifier = Modifier.padding(BrownSpacing.space6)
            ) {
                Text(
                    text = "오류",
                    style = MaterialTheme.typography.titleMedium,
                    color = BrownColor.Error,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = BrownColor.DetailTextSub,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(BrownSpacing.space2))
                Text(
                    text = "돌아가기",
                    style = MaterialTheme.typography.bodyMedium,
                    color = BrownColor.DetailPrimary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable(onClick = onNavigateBack)
                )
            }
        }
    }
}

// Previews
@Preview(showBackground = true, backgroundColor = 0xFFF5F4F2)
@Preview(
    showBackground = true,
    backgroundColor = 0xFFF5F4F2,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun PersonDetailScreenPreview() {
    SocialTreeTheme {
        val sampleNode = SocialNode(
            id = "sample-1",
            name = "김서연",
            type = NodeType.PERSON,
            status = NodeStatus.ACTIVE,
            personData = PersonData(
                relationship = PersonRelationship.PARENT,
                birthDate = BirthDate(
                    solarDate = "1965년 4월 5일",
                    lunarDate = "음력 3월 5일"
                ),
                phoneNumber = "010-1234-5678",
                memo = "꽃을 좋아하시고 요리를 잘하신다. 특히 봄에는 프리지아를 가장 좋아하심. 주말마다 등산을 즐기시는 편."
            )
        )

        PersonDetailView(
            node = sampleNode,
            personData = sampleNode.personData!!,
            yearsTogetherText = "함께한 지 28년",
            onNavigateBack = {},
            onEditNode = {},
            onDeleteNode = {},
            onCallPhone = {},
            onSendSMS = {}
        )
    }
}
