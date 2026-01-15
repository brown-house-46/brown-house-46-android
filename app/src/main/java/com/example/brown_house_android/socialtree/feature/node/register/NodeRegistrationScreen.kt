package com.example.brown_house_android.socialtree.feature.node.register

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.brown_house_android.socialtree.core.designsystem.foundation.BrownBirthDateToggleInput
import com.example.brown_house_android.socialtree.core.designsystem.foundation.BrownProfilePhotoUploader
import com.example.brown_house_android.socialtree.core.designsystem.foundation.BrownTextField
import com.example.brown_house_android.socialtree.core.designsystem.theme.SocialTreeTheme
import com.example.brown_house_android.socialtree.core.designsystem.tokens.BrownColor
import com.example.brown_house_android.socialtree.core.designsystem.tokens.BrownSpacing
import com.example.brown_house_android.socialtree.core.model.BirthDateMode
import com.example.brown_house_android.socialtree.core.model.NodeType
import com.example.brown_house_android.socialtree.core.model.PersonRelationship
import com.example.brown_house_android.socialtree.core.ui.UiEvent

/**
 * Node Registration Screen
 * Redesigned to match HTML document design for person nodes
 */
@Composable
fun NodeRegistrationScreen(
    parentId: String? = null,
    viewModel: NodeRegistrationViewModel = viewModel(),
    onNavigateBack: () -> Unit = {},
    onRegistrationSuccess: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(parentId) {
        viewModel.setParentId(parentId)
    }

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.Navigate -> onRegistrationSuccess()
                UiEvent.NavigateBack -> onNavigateBack()
                else -> Unit
            }
        }
    }

    NodeRegistrationContent(
        uiState = uiState,
        onNameChanged = viewModel::onNameChanged,
        onProfilePhotoClick = { /* TODO: Implement photo picker */ },
        onRelationshipSelected = viewModel::onRelationshipSelected,
        onBirthDateModeChanged = viewModel::onBirthDateModeChanged,
        onAgeChanged = viewModel::onAgeChanged,
        onBirthYearChanged = viewModel::onBirthYearChanged,
        onPhoneNumberChanged = viewModel::onPhoneNumberChanged,
        onMemoChanged = viewModel::onMemoChanged,
        onSubmit = viewModel::onSubmit,
        onCancel = viewModel::onCancel
    )
}

@Composable
private fun NodeRegistrationContent(
    uiState: NodeRegistrationUiState,
    onNameChanged: (String) -> Unit,
    onProfilePhotoClick: () -> Unit,
    onRelationshipSelected: (PersonRelationship) -> Unit,
    onBirthDateModeChanged: (BirthDateMode) -> Unit,
    onAgeChanged: (String) -> Unit,
    onBirthYearChanged: (String) -> Unit,
    onPhoneNumberChanged: (String) -> Unit,
    onMemoChanged: (String) -> Unit,
    onSubmit: () -> Unit,
    onCancel: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BrownColor.InputBgLight)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Header
            RegistrationHeader(
                onCancel = onCancel
            )

            // Scrollable content
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(BrownSpacing.space8),
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = BrownSpacing.space5)
                    .padding(bottom = 120.dp) // Space for fixed button
            ) {
                Spacer(modifier = Modifier.height(BrownSpacing.space6))

                // Profile Photo Section
                BrownProfilePhotoUploader(
                    imageUrl = uiState.profileImageUrl,
                    onPhotoClick = onProfilePhotoClick
                )

                Spacer(modifier = Modifier.height(BrownSpacing.space2))

                // Name Input
                NameInputSection(
                    name = uiState.name,
                    onNameChanged = onNameChanged
                )

                // Relationship Selection
                RelationshipSection(
                    selectedRelationship = uiState.relationship,
                    onRelationshipSelected = onRelationshipSelected
                )

                // Birth Date Input
                BirthDateSection(
                    mode = uiState.birthDateMode,
                    age = uiState.age,
                    birthYear = uiState.birthYear,
                    onModeChange = onBirthDateModeChanged,
                    onAgeChange = onAgeChanged,
                    onBirthYearChange = onBirthYearChanged
                )

                // Memo Input
                MemoSection(
                    memo = uiState.memo,
                    onMemoChanged = onMemoChanged
                )
            }
        }

        // Fixed bottom button
        BottomSaveButton(
            isEnabled = uiState.isSubmitEnabled,
            isLoading = uiState.isLoading,
            onSubmit = onSubmit,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
private fun RegistrationHeader(
    onCancel: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(BrownColor.InputBgLight.copy(alpha = 0.95f))
            .padding(horizontal = BrownSpacing.space4, vertical = BrownSpacing.space3)
            .padding(bottom = BrownSpacing.space1)
    ) {
        // Cancel button
        TextButton(
            onClick = onCancel,
            modifier = Modifier.align(Alignment.CenterStart)
        ) {
            Text(
                text = "취소",
                style = MaterialTheme.typography.bodyMedium,
                color = BrownColor.InputTextSub,
                fontWeight = FontWeight.Medium
            )
        }

        // Title
        Text(
            text = "새 노드 추가",
            style = MaterialTheme.typography.titleMedium,
            color = BrownColor.InputTextMain,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Center)
        )

        // Spacer for symmetry
        Box(
            modifier = Modifier
                .width(60.dp)
                .align(Alignment.CenterEnd)
        )
    }
}

@Composable
private fun NameInputSection(
    name: String,
    onNameChanged: (String) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(BrownSpacing.space2),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "이름",
            style = MaterialTheme.typography.bodySmall,
            color = BrownColor.InputTextMain,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = BrownSpacing.space2)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .shadow(
                    elevation = 2.dp,
                    shape = RoundedCornerShape(24.dp),
                    ambientColor = BrownColor.InputPrimary.copy(alpha = 0.03f)
                )
                .background(
                    color = BrownColor.InputSurfaceLight,
                    shape = RoundedCornerShape(24.dp)
                )
                .padding(horizontal = BrownSpacing.space6)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxSize()
            ) {
                BrownTextField(
                    value = name,
                    onValueChange = onNameChanged,
                    placeholder = "이름을 입력하세요",
                    modifier = Modifier.weight(1f)
                )

                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = BrownColor.InputPlaceholder,
                    modifier = Modifier.padding(start = BrownSpacing.space3)
                )
            }
        }
    }
}

@Composable
private fun RelationshipSection(
    selectedRelationship: PersonRelationship,
    onRelationshipSelected: (PersonRelationship) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(BrownSpacing.space3),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = BrownSpacing.space2)
        ) {
            Text(
                text = "관계",
                style = MaterialTheme.typography.bodySmall,
                color = BrownColor.InputTextMain,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "필수 선택",
                style = MaterialTheme.typography.labelSmall,
                color = BrownColor.InputTextSub,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .background(
                        color = BrownColor.InputSurfaceLight.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(6.dp)
                    )
                    .padding(horizontal = BrownSpacing.space2, vertical = 2.dp)
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(BrownSpacing.space2),
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
                .padding(bottom = BrownSpacing.space1)
        ) {
            PersonRelationship.values().forEach { relationship ->
                RelationshipChip(
                    relationship = relationship,
                    isSelected = selectedRelationship == relationship,
                    onClick = { onRelationshipSelected(relationship) }
                )
            }
        }
    }
}

@Composable
private fun RelationshipChip(
    relationship: PersonRelationship,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .clip(CircleShape)
            .clickable(onClick = onClick)
            .background(
                color = if (isSelected) BrownColor.InputPrimary else BrownColor.InputSurfaceLight,
                shape = CircleShape
            )
            .shadow(
                elevation = if (isSelected) 4.dp else 2.dp,
                shape = CircleShape,
                ambientColor = if (isSelected) BrownColor.InputPrimary.copy(alpha = 0.2f) else BrownColor.InputPrimary.copy(alpha = 0.03f)
            )
            .border(
                width = if (isSelected) 0.dp else 1.dp,
                color = if (isSelected) BrownColor.InputPrimary else BrownColor.InputSurfaceDark.copy(alpha = 0.1f),
                shape = CircleShape
            )
            .padding(horizontal = BrownSpacing.space6, vertical = BrownSpacing.space3)
    ) {
        Text(
            text = relationship.label,
            style = MaterialTheme.typography.bodySmall,
            color = if (isSelected) BrownColor.InputPrimaryForeground else BrownColor.InputTextSub,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
        )
    }
}

@Composable
private fun BirthDateSection(
    mode: BirthDateMode,
    age: String,
    birthYear: String,
    onModeChange: (BirthDateMode) -> Unit,
    onAgeChange: (String) -> Unit,
    onBirthYearChange: (String) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(BrownSpacing.space2),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "생년월일 또는 나이",
            style = MaterialTheme.typography.bodySmall,
            color = BrownColor.InputTextMain,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = BrownSpacing.space2)
        )

        BrownBirthDateToggleInput(
            mode = mode,
            value = if (mode == BirthDateMode.AGE) age else birthYear,
            onModeChange = onModeChange,
            onValueChange = { value ->
                if (mode == BirthDateMode.AGE) onAgeChange(value) else onBirthYearChange(value)
            }
        )
    }
}

@Composable
private fun MemoSection(
    memo: String,
    onMemoChanged: (String) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(BrownSpacing.space2),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "메모 (선택)",
            style = MaterialTheme.typography.bodySmall,
            color = BrownColor.InputTextMain,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = BrownSpacing.space2)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(144.dp)
                .shadow(
                    elevation = 2.dp,
                    shape = RoundedCornerShape(24.dp),
                    ambientColor = BrownColor.InputPrimary.copy(alpha = 0.03f)
                )
                .background(
                    color = BrownColor.InputSurfaceLight,
                    shape = RoundedCornerShape(24.dp)
                )
                .padding(BrownSpacing.space6)
        ) {
            BrownTextField(
                value = memo,
                onValueChange = onMemoChanged,
                placeholder = "특이사항이나 기억할 점을 적어주세요.",
                singleLine = false,
                maxLines = 6,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
private fun BottomSaveButton(
    isEnabled: Boolean,
    isLoading: Boolean,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(BrownColor.InputBgLight.copy(alpha = 0.85f))
            .padding(BrownSpacing.space5)
            .padding(bottom = BrownSpacing.space4)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .shadow(
                    elevation = 8.dp,
                    shape = RoundedCornerShape(24.dp),
                    ambientColor = BrownColor.InputPrimary.copy(alpha = 0.1f)
                )
                .clip(RoundedCornerShape(24.dp))
                .background(
                    color = if (isEnabled) BrownColor.InputPrimary else BrownColor.InputTextSub.copy(alpha = 0.3f)
                )
                .clickable(enabled = isEnabled && !isLoading, onClick = onSubmit)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(BrownSpacing.space2),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Save,
                    contentDescription = null,
                    tint = BrownColor.InputPrimaryForeground
                )
                Text(
                    text = if (isLoading) "저장 중..." else "정보 저장하기",
                    style = MaterialTheme.typography.titleMedium,
                    color = BrownColor.InputPrimaryForeground,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

// Previews
@Preview(showBackground = true, backgroundColor = 0xFFF5F2EB)
@Preview(
    showBackground = true,
    backgroundColor = 0xFFF5F2EB,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun NodeRegistrationScreenPreview() {
    SocialTreeTheme {
        NodeRegistrationContent(
            uiState = NodeRegistrationUiState(
                name = "김서연",
                relationship = PersonRelationship.PARENT,
                age = "28",
                memo = "꽃을 좋아하시고..."
            ),
            onNameChanged = {},
            onProfilePhotoClick = {},
            onRelationshipSelected = {},
            onBirthDateModeChanged = {},
            onAgeChanged = {},
            onBirthYearChanged = {},
            onPhoneNumberChanged = {},
            onMemoChanged = {},
            onSubmit = {},
            onCancel = {}
        )
    }
}
