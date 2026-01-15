package com.example.brown_house_android.socialtree.feature.home

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountTree
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FormatListBulleted
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.brown_house_android.socialtree.core.designsystem.foundation.BrownAvatar
import com.example.brown_house_android.socialtree.core.designsystem.foundation.BrownAvatarSize
import com.example.brown_house_android.socialtree.core.designsystem.foundation.BrownButton
import com.example.brown_house_android.socialtree.core.designsystem.foundation.BrownButtonSize
import com.example.brown_house_android.socialtree.core.designsystem.foundation.BrownButtonVariant
import com.example.brown_house_android.socialtree.core.designsystem.foundation.BrownCard
import com.example.brown_house_android.socialtree.core.designsystem.foundation.BrownFloatingActionButton
import com.example.brown_house_android.socialtree.core.designsystem.foundation.BrownIconButton
import com.example.brown_house_android.socialtree.core.designsystem.foundation.BrownSecondaryButton
import com.example.brown_house_android.socialtree.core.designsystem.theme.SocialTreeTheme
import com.example.brown_house_android.socialtree.core.designsystem.tokens.BrownColor
import com.example.brown_house_android.socialtree.core.designsystem.tokens.BrownShape
import com.example.brown_house_android.socialtree.core.designsystem.tokens.BrownSpacing
import com.example.brown_house_android.socialtree.core.model.SocialNode
import kotlin.math.roundToInt

/**
 * Home Screen - Social Tree overview.
 */
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel(),
    onNodeClick: (String) -> Unit = {},
    onAddNode: () -> Unit = {},
    onOpenProfile: () -> Unit = {},
    onOpenSettings: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    HomeContent(
        uiState = uiState,
        onRetry = viewModel::retry,
        onNodeSelected = viewModel::selectNode,
        onDismissSelected = viewModel::dismissSelectedNode,
        onNodeClick = onNodeClick,
        onAddNode = onAddNode,
        onOpenProfile = onOpenProfile,
        onOpenSettings = onOpenSettings
    )
}

@Composable
private fun HomeContent(
    uiState: HomeUiState,
    onRetry: () -> Unit,
    onNodeSelected: (String) -> Unit,
    onDismissSelected: () -> Unit,
    onNodeClick: (String) -> Unit,
    onAddNode: () -> Unit,
    onOpenProfile: () -> Unit,
    onOpenSettings: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BrownColor.BackgroundLight)
    ) {
        TreeCanvas(onNodeSelected = onNodeSelected)

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .padding(horizontal = BrownSpacing.screenPadding)
        ) {
            Spacer(modifier = Modifier.height(BrownSpacing.space6))
            HomeTopBar()
            Spacer(modifier = Modifier.height(BrownSpacing.space3))
            FilterChipsRow()
        }

        when {
            uiState.isLoading -> {
                LoadingState()
            }

            uiState.errorMessage != null -> {
                ErrorState(
                    message = uiState.errorMessage,
                    onRetry = onRetry
                )
            }

            else -> {
                val isVisible = uiState.selectedNode != null
                AnimatedVisibility(
                    visible = isVisible,
                    enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                    exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable(onClick = onDismissSelected)
                            .background(BrownColor.TextMainLight.copy(alpha = 0.08f))
                    )
                }

                AnimatedVisibility(
                    visible = isVisible,
                    enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                    exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
                    modifier = Modifier.align(Alignment.BottomCenter)
                ) {
                    BottomDetailSheet(
                        node = uiState.selectedNode,
                        onDismiss = onDismissSelected,
                        onView = { uiState.selectedNode?.let { onNodeClick(it.id) } },
                        onEdit = onAddNode,
                        modifier = Modifier
                            .padding(horizontal = BrownSpacing.space4, vertical = BrownSpacing.space10)
                    )
                }
            }
        }

        BrownFloatingActionButton(
            icon = Icons.Default.Add,
            contentDescription = "노드 추가",
            onClick = onAddNode,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = BrownSpacing.space5, bottom = BrownSpacing.space16)
        )

        BottomNavBar(
            onOpenSettings = onOpenSettings,
            onOpenProfile = onOpenProfile,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
private fun HomeTopBar() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "My Social Tree",
            style = MaterialTheme.typography.headlineLarge,
            color = BrownColor.Primary
        )
        BrownIconButton(
            icon = Icons.Default.Search,
            contentDescription = "검색",
            onClick = {}
        )
    }
}

@Composable
private fun FilterChipsRow() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(BrownSpacing.space2)
    ) {
        FilterChip(
            text = "All",
            isSelected = true,
            icon = Icons.Default.GridView
        )
        FilterChip(
            text = "Family",
            isSelected = false,
            icon = Icons.Default.Favorite
        )
        FilterChip(
            text = "Friends",
            isSelected = false,
            icon = Icons.Default.Group
        )
        FilterChip(
            text = "Coworkers",
            isSelected = false,
            icon = Icons.Default.Work
        )
    }
}

@Composable
private fun FilterChip(
    text: String,
    isSelected: Boolean,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    val background = if (isSelected) BrownColor.Primary else BrownColor.SurfaceLight
    val content = if (isSelected) BrownColor.TextMainDark else BrownColor.TextSubLight

    Row(
        modifier = Modifier
            .background(background, BrownShape.chipShape)
            .padding(horizontal = BrownSpacing.space4, vertical = BrownSpacing.space2),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(BrownSpacing.space1)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = content,
            modifier = Modifier.size(16.dp)
        )
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            color = content
        )
    }
}

private const val MIN_SCALE = 0.5f
private const val MAX_SCALE = 3.0f

@Composable
private fun TreeCanvas(onNodeSelected: (String) -> Unit) {
    var scale by remember { mutableFloatStateOf(1f) }
    var offsetX by remember { mutableFloatStateOf(0f) }
    var offsetY by remember { mutableFloatStateOf(0f) }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .clipToBounds()
            .pointerInput(Unit) {
                detectTransformGestures { centroid, pan, zoom, _ ->
                    val newScale = (scale * zoom).coerceIn(MIN_SCALE, MAX_SCALE)
                    val scaleChange = newScale / scale
                    offsetX = (offsetX - centroid.x) * scaleChange + centroid.x + pan.x
                    offsetY = (offsetY - centroid.y) * scaleChange + centroid.y + pan.y
                    scale = newScale
                }
            }
            .pointerInput(Unit) {
                detectTapGestures(
                    onDoubleTap = { tapOffset ->
                        if (scale > 1.5f) {
                            scale = 1f
                            offsetX = 0f
                            offsetY = 0f
                        } else {
                            scale = 2f
                            offsetX = (1 - scale) * tapOffset.x
                            offsetY = (1 - scale) * tapOffset.y
                        }
                    }
                )
            }
    ) {
        val width = maxWidth
        val height = maxHeight

        val mePosition = DpOffset(width * 0.5f, height * 0.52f)
        val momPosition = DpOffset(width * 0.33f, height * 0.36f)
        val dadPosition = DpOffset(width * 0.68f, height * 0.36f)
        val grandmaPosition = DpOffset(width * 0.18f, height * 0.22f)
        val alexPosition = DpOffset(width * 0.78f, height * 0.55f)
        val sarahPosition = DpOffset(width * 0.55f, height * 0.72f)

        Box(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                    translationX = offsetX
                    translationY = offsetY
                }
        ) {
            DotGridBackground()

            Canvas(modifier = Modifier.fillMaxSize()) {
                val stroke = (BrownSpacing.space1 / 4)
                drawLine(
                    color = BrownColor.BorderLight,
                    start = mePosition.toPxOffset(this),
                    end = momPosition.toPxOffset(this),
                    strokeWidth = stroke.toPx(),
                    cap = StrokeCap.Round
                )
                drawLine(
                    color = BrownColor.BorderLight,
                    start = mePosition.toPxOffset(this),
                    end = dadPosition.toPxOffset(this),
                    strokeWidth = stroke.toPx(),
                    cap = StrokeCap.Round
                )
                drawLine(
                    color = BrownColor.BorderLight,
                    start = mePosition.toPxOffset(this),
                    end = alexPosition.toPxOffset(this),
                    strokeWidth = stroke.toPx(),
                    cap = StrokeCap.Round
                )
                drawLine(
                    color = BrownColor.BorderLight,
                    start = mePosition.toPxOffset(this),
                    end = sarahPosition.toPxOffset(this),
                    strokeWidth = stroke.toPx(),
                    cap = StrokeCap.Round
                )
                drawLine(
                    color = BrownColor.BorderLight,
                    start = momPosition.toPxOffset(this),
                    end = grandmaPosition.toPxOffset(this),
                    strokeWidth = stroke.toPx(),
                    cap = StrokeCap.Round
                )
            }

            TreePersonNode(
                name = "Grandma",
                size = BrownAvatarSize.SMALL,
                position = grandmaPosition,
                labelStyle = LabelStyle.Subtle
            ) { onNodeSelected("node-1") }

            TreePersonNode(
                name = "Mom",
                size = BrownAvatarSize.MEDIUM,
                position = momPosition,
                labelStyle = LabelStyle.Primary
            ) { onNodeSelected("node-2") }

            TreePersonNode(
                name = "Dad",
                size = BrownAvatarSize.MEDIUM,
                position = dadPosition,
                labelStyle = LabelStyle.Subtle
            ) { onNodeSelected("node-3") }

            TreePersonNode(
                name = "Alex",
                size = BrownAvatarSize.SMALL,
                position = alexPosition,
                labelStyle = LabelStyle.Subtle
            ) { onNodeSelected("node-4") }

            TreePersonNode(
                name = "Sarah",
                size = BrownAvatarSize.SMALL,
                position = sarahPosition,
                labelStyle = LabelStyle.Subtle
            ) { onNodeSelected("node-5") }

            TreeMainNode(
                name = "Me",
                position = mePosition
            ) { onNodeSelected("node-me") }
        }
    }
}

@Composable
private fun DotGridBackground() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val step = (BrownSpacing.space8).toPx()
        val radius = (BrownSpacing.space1 / 4).toPx()
        val color = BrownColor.BorderLight.copy(alpha = 0.25f)

        var y = 0f
        while (y < size.height) {
            var x = 0f
            while (x < size.width) {
                drawCircle(color = color, radius = radius, center = androidx.compose.ui.geometry.Offset(x, y))
                x += step
            }
            y += step
        }
    }
}

private data class DpOffset(val x: Dp, val y: Dp) {
    fun toPxOffset(scope: androidx.compose.ui.graphics.drawscope.DrawScope): androidx.compose.ui.geometry.Offset {
        return androidx.compose.ui.geometry.Offset(x.toPx(scope), y.toPx(scope))
    }

    private fun Dp.toPx(scope: androidx.compose.ui.graphics.drawscope.DrawScope): Float {
        return with(scope) { this@toPx.toPx() }
    }
}

private enum class LabelStyle {
    Primary,
    Subtle
}

@Composable
private fun TreePersonNode(
    name: String,
    size: BrownAvatarSize,
    position: DpOffset,
    labelStyle: LabelStyle,
    onClick: () -> Unit
) {
    val labelBackground = when (labelStyle) {
        LabelStyle.Primary -> BrownColor.Primary
        LabelStyle.Subtle -> BrownColor.SurfaceLight.copy(alpha = 0.7f)
    }
    val labelColor = when (labelStyle) {
        LabelStyle.Primary -> BrownColor.TextMainDark
        LabelStyle.Subtle -> BrownColor.TextSubLight
    }

    Column(
        modifier = Modifier
            .offset(
                x = position.x - (size.size / 2),
                y = position.y - (size.size / 2)
            )
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BrownAvatar(size = size, contentDescription = name)
        Spacer(modifier = Modifier.height(BrownSpacing.space2))
        Box(
            modifier = Modifier
                .background(labelBackground, BrownShape.chipShape)
                .padding(horizontal = BrownSpacing.space3, vertical = BrownSpacing.space1)
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.labelMedium,
                color = labelColor,
                fontWeight = if (labelStyle == LabelStyle.Primary) FontWeight.SemiBold else FontWeight.Medium
            )
        }
    }
}

@Composable
private fun TreeMainNode(
    name: String,
    position: DpOffset,
    onClick: () -> Unit
) {
    val size = BrownAvatarSize.LARGE.size

    Column(
        modifier = Modifier
            .offset(x = position.x - (size / 2), y = position.y - (size / 2))
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(size + BrownSpacing.space4)
                .background(BrownColor.SurfaceLight, BrownShape.fullShape),
            contentAlignment = Alignment.Center
        ) {
            BrownAvatar(size = BrownAvatarSize.LARGE, contentDescription = name)
        }
        Spacer(modifier = Modifier.height(BrownSpacing.space2))
        Text(
            text = name,
            style = MaterialTheme.typography.titleMedium,
            color = BrownColor.TextMainLight,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun BottomDetailSheet(
    node: SocialNode?,
    onDismiss: () -> Unit,
    onView: () -> Unit,
    onEdit: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (node == null) return

    var sheetHeightPx by remember { mutableFloatStateOf(0f) }
    val peekFraction = 0.55f
    val collapsedOffset = if (sheetHeightPx == 0f) 0f else sheetHeightPx * (1f - peekFraction)
    var offsetY by remember { mutableFloatStateOf(collapsedOffset) }

    LaunchedEffect(node.id, sheetHeightPx) {
        offsetY = collapsedOffset
    }

    val draggableState = rememberDraggableState { delta ->
        offsetY = (offsetY + delta).coerceIn(0f, collapsedOffset)
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .offset { androidx.compose.ui.unit.IntOffset(0, offsetY.roundToInt()) }
            .draggable(
                state = draggableState,
                orientation = Orientation.Vertical,
                onDragStopped = {
                    val target = if (offsetY < collapsedOffset / 2f) 0f else collapsedOffset
                    offsetY = target
                }
            )
    ) {
        BrownCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = BrownSpacing.space6),
            shape = BrownShape.extraLarge2Shape
        ) {
            Column(
                modifier = Modifier
                    .padding(BrownSpacing.space5)
                    .fillMaxWidth()
                    .onSizeChanged { sheetHeightPx = it.height.toFloat() },
                verticalArrangement = Arrangement.spacedBy(BrownSpacing.space4)
            ) {
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .width(44.dp)
                        .height(4.dp)
                        .background(BrownColor.BorderLight, BrownShape.fullShape)
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(BrownSpacing.space4)
                ) {
                    BrownAvatar(size = BrownAvatarSize.MEDIUM, contentDescription = node.name)
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = node.name,
                                style = MaterialTheme.typography.titleLarge,
                                color = BrownColor.TextMainLight
                            )
                            Spacer(modifier = Modifier.width(BrownSpacing.space2))
                            Box(
                                modifier = Modifier
                                    .background(BrownColor.SurfaceVariant, BrownShape.chipShape)
                                    .padding(horizontal = BrownSpacing.space2, vertical = BrownSpacing.space1)
                            ) {
                                Text(
                                    text = node.type.label.uppercase(),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = BrownColor.TextSubLight
                                )
                            }
                        }
                        Text(
                            text = "Last added 2 days ago",
                            style = MaterialTheme.typography.bodySmall,
                            color = BrownColor.TextSubLight
                        )
                    }
                    BrownIconButton(
                        icon = Icons.Default.Close,
                        contentDescription = "닫기",
                        onClick = onDismiss
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(BrownSpacing.space3)) {
                    BrownButton(
                        onClick = onView,
                        variant = BrownButtonVariant.SECONDARY,
                        size = BrownButtonSize.MEDIUM,
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Visibility,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(BrownSpacing.space2))
                        Text(text = "View Profile")
                    }
                    BrownButton(
                        onClick = onEdit,
                        variant = BrownButtonVariant.PRIMARY,
                        size = BrownButtonSize.MEDIUM,
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(BrownSpacing.space2))
                        Text(text = "Edit Relation")
                    }
                }
            }
        }
    }
}

@Composable
private fun BottomNavBar(
    onOpenSettings: () -> Unit,
    onOpenProfile: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(BrownColor.SurfaceLight)
            .padding(top = BrownSpacing.space2, bottom = BrownSpacing.space6)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(BrownSpacing.space1 / 4)
                .background(BrownColor.BorderLight)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = BrownSpacing.space3),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BottomNavItem(
                label = "Tree",
                icon = Icons.Default.AccountTree,
                isSelected = true,
                onClick = {}
            )
            BottomNavItem(
                label = "List",
                icon = Icons.Default.FormatListBulleted,
                isSelected = false,
                onClick = onOpenProfile
            )
            BottomNavItem(
                label = "Settings",
                icon = Icons.Default.Settings,
                isSelected = false,
                onClick = onOpenSettings
            )
        }
    }
}

@Composable
private fun BottomNavItem(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val color = if (isSelected) BrownColor.Primary else BrownColor.TextSubLight

    Column(
        modifier = Modifier.clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(BrownSpacing.space1)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = color,
            modifier = Modifier.size(22.dp)
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = color
        )
    }
}

@Composable
private fun LoadingState() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = BrownSpacing.space12),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = BrownColor.Primary)
    }
}

@Composable
private fun ErrorState(
    message: String,
    onRetry: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(BrownSpacing.space6)
    ) {
        BrownCard(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(BrownSpacing.cardPadding),
                verticalArrangement = Arrangement.spacedBy(BrownSpacing.space3)
            ) {
                Text(
                    text = "문제가 발생했습니다",
                    style = MaterialTheme.typography.titleMedium,
                    color = BrownColor.TextMainLight
                )
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = BrownColor.TextSubLight
                )
                BrownSecondaryButton(
                    text = "다시 시도",
                    onClick = onRetry
                )
            }
        }
    }
}

@Preview(name = "Home - Light", showBackground = true)
@Preview(name = "Home - Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun HomeScreenPreview() {
    SocialTreeTheme {
        HomeContent(
            uiState = HomeUiState(),
            onRetry = {},
            onNodeSelected = {},
            onDismissSelected = {},
            onNodeClick = {},
            onAddNode = {},
            onOpenProfile = {},
            onOpenSettings = {}
        )
    }
}
