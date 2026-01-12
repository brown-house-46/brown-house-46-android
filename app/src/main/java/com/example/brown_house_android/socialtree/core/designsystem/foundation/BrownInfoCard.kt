package com.example.brown_house_android.socialtree.core.designsystem.foundation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.brown_house_android.socialtree.core.designsystem.tokens.BrownColor
import com.example.brown_house_android.socialtree.core.designsystem.tokens.BrownSpacing

/**
 * Info card component for displaying person details
 * Based on HTML design: icon circle + title + content area
 * Used in NodeDetailScreen for birthday, contact, memo sections
 */
@Composable
fun BrownInfoCard(
    icon: ImageVector,
    title: String,
    modifier: Modifier = Modifier,
    action: (@Composable () -> Unit)? = null,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(24.dp),
                ambientColor = BrownColor.DetailPrimary.copy(alpha = 0.06f),
                spotColor = BrownColor.DetailPrimary.copy(alpha = 0.06f)
            )
            .background(
                color = BrownColor.DetailSurfaceLight,
                shape = RoundedCornerShape(24.dp)
            )
            .border(
                width = 1.dp,
                color = Color.White,
                shape = RoundedCornerShape(24.dp)
            )
            .padding(BrownSpacing.space5)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(BrownSpacing.space4),
            verticalAlignment = Alignment.Top
        ) {
            // Icon circle
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(BrownColor.DetailIconBg)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = BrownColor.DetailTextSub,
                    modifier = Modifier.size(20.dp)
                )
            }

            // Content area
            Column(
                verticalArrangement = Arrangement.spacedBy(BrownSpacing.space1),
                modifier = Modifier
                    .weight(1f)
                    .padding(top = 2.dp)
            ) {
                // Title
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelSmall,
                    color = BrownColor.DetailTextSub,
                    fontWeight = FontWeight.Bold
                )

                // Content slot
                content()
            }

            // Optional action (e.g., SMS button)
            if (action != null) {
                Box(
                    modifier = Modifier.padding(top = 2.dp)
                ) {
                    action()
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F4F2)
@Composable
private fun BrownInfoCardPreview() {
    MaterialTheme {
        Box(modifier = Modifier.padding(BrownSpacing.space4)) {
            BrownInfoCard(
                icon = Icons.Default.Cake,
                title = "생년월일"
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Text(
                        text = "1965년 4월 5일",
                        style = MaterialTheme.typography.bodyMedium,
                        color = BrownColor.DetailTextMain,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "음력 3월 5일",
                        style = MaterialTheme.typography.labelSmall,
                        color = BrownColor.DetailTextSub
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F4F2)
@Composable
private fun BrownInfoCardWithActionPreview() {
    MaterialTheme {
        Box(modifier = Modifier.padding(BrownSpacing.space4)) {
            BrownInfoCard(
                icon = Icons.Default.Cake,
                title = "연락처",
                action = {
                    BrownIconButton(
                        icon = Icons.Default.Cake,
                        contentDescription = "SMS",
                        onClick = {}
                    )
                }
            ) {
                Text(
                    text = "010-1234-5678",
                    style = MaterialTheme.typography.bodyMedium,
                    color = BrownColor.DetailTextMain,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}
