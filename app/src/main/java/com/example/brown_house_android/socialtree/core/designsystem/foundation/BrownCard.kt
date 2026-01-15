package com.example.brown_house_android.socialtree.core.designsystem.foundation

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.brown_house_android.socialtree.core.designsystem.theme.SocialTreeTheme
import com.example.brown_house_android.socialtree.core.designsystem.tokens.BrownColor
import com.example.brown_house_android.socialtree.core.designsystem.tokens.BrownShape
import com.example.brown_house_android.socialtree.core.designsystem.tokens.BrownSpacing

/**
 * BrownCard - Card component with elevation and rounded corners
 *
 * Specs from HTML:
 * - Padding: p-3 to p-4 (12-16dp)
 * - Border radius: rounded-lg to rounded-3xl (16-32dp)
 * - Shadow: soft shadow
 * - Background: white/surface
 */

@Composable
fun BrownCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    shape: RoundedCornerShape = RoundedCornerShape(BrownShape.card),
    content: @Composable () -> Unit
) {
    if (onClick != null) {
        Card(
            onClick = onClick,
            modifier = modifier,
            shape = shape,
            colors = CardDefaults.cardColors(
                containerColor = BrownColor.SurfaceLight,
                contentColor = BrownColor.TextMainLight
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 2.dp,
                pressedElevation = 4.dp
            )
        ) {
            content()
        }
    } else {
        Card(
            modifier = modifier,
            shape = shape,
            colors = CardDefaults.cardColors(
                containerColor = BrownColor.SurfaceLight,
                contentColor = BrownColor.TextMainLight
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 2.dp
            )
        ) {
            content()
        }
    }
}

// Previews
@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun BrownCardPreview() {
    SocialTreeTheme {
        Column(modifier = Modifier.padding(BrownSpacing.space5)) {
            BrownCard(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(BrownSpacing.cardPadding)) {
                    Text(
                        text = "카드 제목",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(BrownSpacing.space2))
                    Text(
                        text = "카드 내용입니다. 이것은 기본 BrownCard 컴포넌트의 예시입니다.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = BrownColor.TextSubLight
                    )
                }
            }

            Spacer(modifier = Modifier.height(BrownSpacing.space4))

            BrownCard(
                modifier = Modifier.fillMaxWidth(),
                onClick = { /* 클릭 */ }
            ) {
                Column(modifier = Modifier.padding(BrownSpacing.cardPadding)) {
                    Text(
                        text = "클릭 가능한 카드",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(BrownSpacing.space2))
                    Text(
                        text = "이 카드는 클릭할 수 있습니다.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = BrownColor.TextSubLight
                    )
                }
            }
        }
    }
}
