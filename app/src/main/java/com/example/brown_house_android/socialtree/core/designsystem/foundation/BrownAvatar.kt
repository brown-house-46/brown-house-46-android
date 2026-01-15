package com.example.brown_house_android.socialtree.core.designsystem.foundation

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.brown_house_android.socialtree.core.designsystem.theme.SocialTreeTheme
import com.example.brown_house_android.socialtree.core.designsystem.tokens.BrownColor
import com.example.brown_house_android.socialtree.core.designsystem.tokens.BrownSpacing

/**
 * BrownAvatar - Circular avatar component
 *
 * Specs from HTML:
 * - Sizes: 40px (w-10), 56px (w-14), 72px (w-18), 144px (w-36)
 * - Circular with border
 * - Shadow effect
 * - Badge/status indicator support
 */

enum class BrownAvatarSize(val size: Dp) {
    SMALL(40.dp),          // w-10 h-10
    MEDIUM(56.dp),         // w-14 h-14
    LARGE(72.dp),          // w-18 h-18
    EXTRA_LARGE(144.dp)    // w-36 h-36
}

@Composable
fun BrownAvatar(
    modifier: Modifier = Modifier,
    imageUrl: String? = null,
    contentDescription: String? = null,
    size: BrownAvatarSize = BrownAvatarSize.MEDIUM,
    showBorder: Boolean = true,
    borderColor: Color = BrownColor.Primary,
    borderWidth: Dp = 2.dp,
    showShadow: Boolean = true,
    badge: (@Composable () -> Unit)? = null
) {
    Box(modifier = modifier) {
        // Avatar
        Box(
            modifier = Modifier
                .size(size.size)
                .then(
                    if (showShadow) Modifier.shadow(4.dp, CircleShape)
                    else Modifier
                )
                .clip(CircleShape)
                .background(BrownColor.SurfaceLight)
                .then(
                    if (showBorder) Modifier.border(borderWidth, borderColor, CircleShape)
                    else Modifier
                ),
            contentAlignment = Alignment.Center
        ) {
            if (imageUrl != null) {
                // Load image with Coil
                Image(
                    painter = rememberAsyncImagePainter(imageUrl),
                    contentDescription = contentDescription,
                    modifier = Modifier.size(size.size),
                    contentScale = ContentScale.Crop
                )
            } else {
                // Placeholder icon
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = contentDescription,
                    tint = BrownColor.TextSubLight,
                    modifier = Modifier.size(size.size * 0.6f)
                )
            }
        }

        // Badge (e.g., notification dot, status indicator)
        if (badge != null) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = BrownSpacing.space1, end = BrownSpacing.space1)
            ) {
                badge()
            }
        }
    }
}

/**
 * Status badge for online/offline indicator
 */
@Composable
fun StatusBadge(
    isOnline: Boolean = false,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(12.dp)
            .clip(CircleShape)
            .background(if (isOnline) BrownColor.Success else BrownColor.TextSubLight)
            .border(2.dp, BrownColor.SurfaceLight, CircleShape)
    )
}

/**
 * Notification count badge
 */
@Composable
fun NotificationBadge(
    count: Int,
    modifier: Modifier = Modifier
) {
    if (count > 0) {
        Box(
            modifier = modifier
                .size(20.dp)
                .clip(CircleShape)
                .background(BrownColor.Error)
                .border(2.dp, BrownColor.SurfaceLight, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (count > 9) "9+" else count.toString(),
                style = androidx.compose.material3.MaterialTheme.typography.labelSmall,
                color = Color.White
            )
        }
    }
}

// Previews
@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun BrownAvatarPreview() {
    SocialTreeTheme {
        Row(
            modifier = Modifier.padding(BrownSpacing.space5),
            horizontalArrangement = Arrangement.spacedBy(BrownSpacing.space4),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Small
            BrownAvatar(size = BrownAvatarSize.SMALL)

            // Medium (default)
            BrownAvatar(size = BrownAvatarSize.MEDIUM)

            // Large
            BrownAvatar(size = BrownAvatarSize.LARGE)
        }
    }
}

@Preview(name = "Avatar with Status Badge", showBackground = true)
@Composable
private fun BrownAvatarWithStatusPreview() {
    SocialTreeTheme {
        Row(
            modifier = Modifier.padding(BrownSpacing.space5),
            horizontalArrangement = Arrangement.spacedBy(BrownSpacing.space4),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Online
            BrownAvatar(
                size = BrownAvatarSize.MEDIUM,
                badge = { StatusBadge(isOnline = true) }
            )

            // Offline
            BrownAvatar(
                size = BrownAvatarSize.MEDIUM,
                badge = { StatusBadge(isOnline = false) }
            )

            // With notification
            BrownAvatar(
                size = BrownAvatarSize.MEDIUM,
                badge = { NotificationBadge(count = 5) }
            )

            // With notification 10+
            BrownAvatar(
                size = BrownAvatarSize.MEDIUM,
                badge = { NotificationBadge(count = 12) }
            )
        }
    }
}

@Preview(name = "Avatar Sizes", showBackground = true)
@Composable
private fun BrownAvatarSizesPreview() {
    SocialTreeTheme {
        Row(
            modifier = Modifier.padding(BrownSpacing.space5),
            horizontalArrangement = Arrangement.spacedBy(BrownSpacing.space4),
            verticalAlignment = Alignment.Bottom
        ) {
            BrownAvatar(size = BrownAvatarSize.SMALL)
            BrownAvatar(size = BrownAvatarSize.MEDIUM)
            BrownAvatar(size = BrownAvatarSize.LARGE)
            BrownAvatar(size = BrownAvatarSize.EXTRA_LARGE)
        }
    }
}
