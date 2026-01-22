package com.example.brown_house_android.socialtree.feature.home.components

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountTree
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Diversity1
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.brown_house_android.socialtree.core.designsystem.foundation.BrownAvatar
import com.example.brown_house_android.socialtree.core.designsystem.foundation.BrownAvatarSize
import com.example.brown_house_android.socialtree.core.designsystem.foundation.BrownIconButton
import com.example.brown_house_android.socialtree.core.designsystem.theme.SocialTreeTheme
import com.example.brown_house_android.socialtree.core.designsystem.tokens.BrownColor
import com.example.brown_house_android.socialtree.core.designsystem.tokens.BrownShape
import com.example.brown_house_android.socialtree.core.designsystem.tokens.BrownSpacing
import kotlinx.coroutines.launch

private const val TAG = "SideMenuDrawer"

/**
 * SideMenuDrawer - Modal navigation drawer component
 *
 * Specs:
 * - Width: 85% of screen width
 * - Rounded right corner: 32dp
 * - Scrim overlay with 40% opacity
 */
@Composable
fun SideMenuDrawer(
    drawerState: DrawerState,
    onCloseDrawer: () -> Unit,
    onViewProfile: () -> Unit,
    onFriendsClick: () -> Unit,
    onFamilyGroupsClick: () -> Unit,
    onMyTreeClick: () -> Unit,
    onNotificationsClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onHelpSupportClick: () -> Unit,
    onLogoutClick: () -> Unit,
    content: @Composable () -> Unit
) {
    val configuration = LocalConfiguration.current
    val drawerWidth = (configuration.screenWidthDp * 0.85f).dp

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = true,
        scrimColor = BrownColor.TextMainLight.copy(alpha = 0.4f),
        drawerContent = {
            Box(
                modifier = Modifier
                    .width(drawerWidth)
                    .fillMaxHeight()
                    .background(
                        color = BrownColor.BackgroundLight,
                        shape = RoundedCornerShape(
                            topStart = 0.dp,
                            topEnd = BrownShape.extraLarge2,
                            bottomStart = 0.dp,
                            bottomEnd = BrownShape.extraLarge2
                        )
                    )
            ) {
                Column(
                    modifier = Modifier.fillMaxHeight()
                ) {
                    // Header
                    SideMenuHeader(
                        onClose = onCloseDrawer,
                        onViewProfile = onViewProfile
                    )

                    // Divider
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(BrownColor.BorderLight)
                    )

                    // Scrollable menu content
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .verticalScroll(rememberScrollState())
                            .padding(BrownSpacing.space4)
                    ) {
                        // SOCIAL section
                        SideMenuSection(title = "SOCIAL")
                        SideMenuItem(
                            icon = Icons.Default.Group,
                            label = "Friends (친구)",
                            isSelected = true,
                            badge = { CountBadge(count = 12) },
                            onClick = onFriendsClick
                        )
                        SideMenuItem(
                            icon = Icons.Default.Diversity1,
                            label = "Family Groups",
                            onClick = onFamilyGroupsClick
                        )
                        SideMenuItem(
                            icon = Icons.Default.AccountTree,
                            label = "My Tree",
                            onClick = onMyTreeClick
                        )

                        Spacer(modifier = Modifier.height(BrownSpacing.space4))

                        // GENERAL section
                        SideMenuSection(title = "GENERAL")
                        SideMenuItem(
                            icon = Icons.Default.Notifications,
                            label = "Notifications",
                            badge = { NotificationDot() },
                            onClick = onNotificationsClick
                        )
                        SideMenuItem(
                            icon = Icons.Default.Settings,
                            label = "Settings",
                            onClick = onSettingsClick
                        )
                        SideMenuItem(
                            icon = Icons.Default.Help,
                            label = "Help & Support",
                            onClick = onHelpSupportClick
                        )
                    }

                    // Footer
                    SideMenuFooter(
                        onLogout = onLogoutClick
                    )
                }
            }
        },
        content = content
    )
}

/**
 * SideMenuHeader - User profile section at the top of the drawer
 *
 * Specs:
 * - Background: SurfaceVariant
 * - Padding: top=56dp, bottom=32dp, horizontal=24dp
 * - Profile photo with edit badge
 * - User name (22sp, Bold)
 * - "View Profile >" link
 * - Close button (top right)
 */
@Composable
private fun SideMenuHeader(
    onClose: () -> Unit,
    onViewProfile: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(BrownColor.SurfaceVariant)
            .padding(
                top = 56.dp,
                bottom = 32.dp,
                start = 24.dp,
                end = 24.dp
            )
    ) {
        // Close button
        BrownIconButton(
            icon = Icons.Default.Close,
            contentDescription = "Close menu",
            onClick = onClose,
            modifier = Modifier.align(Alignment.TopEnd)
        )

        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(BrownSpacing.space4)
        ) {
            // Profile photo with edit badge
            Box {
                BrownAvatar(
                    size = BrownAvatarSize.LARGE,
                    contentDescription = "Profile photo",
                    showBorder = true,
                    borderColor = BrownColor.BorderLight,
                    borderWidth = 2.dp
                )
                // Edit badge
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .size(28.dp)
                        .background(BrownColor.Primary, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit profile",
                        tint = BrownColor.TextMainDark,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }

            // User info
            Column(
                verticalArrangement = Arrangement.spacedBy(BrownSpacing.space1)
            ) {
                Text(
                    text = "Kim Min-jun",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = BrownColor.TextMainLight
                )
                Row(
                    modifier = Modifier.clickable(onClick = onViewProfile),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(BrownSpacing.space1)
                ) {
                    Text(
                        text = "View Profile",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        color = BrownColor.TextSubLight
                    )
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = null,
                        tint = BrownColor.TextSubLight,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }
    }
}

/**
 * SideMenuSection - Section title component
 *
 * Specs:
 * - Uppercase text, 12sp, Bold
 * - Color: TextSubLight
 * - Top margin: 16dp, Bottom margin: 8dp
 */
@Composable
private fun SideMenuSection(
    title: String
) {
    Text(
        text = title,
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        color = BrownColor.TextSubLight,
        modifier = Modifier.padding(
            top = BrownSpacing.space4,
            bottom = BrownSpacing.space2,
            start = BrownSpacing.space4
        )
    )
}

/**
 * SideMenuItem - Individual menu item
 *
 * Specs:
 * - Height: 56dp
 * - Icon + Text + Optional Badge
 * - Normal state: TextSubLight
 * - Selected state: Primary background with alpha, Primary border and text
 * - Border radius: 12dp (medium)
 */
@Composable
private fun SideMenuItem(
    icon: ImageVector,
    label: String,
    isSelected: Boolean = false,
    badge: (@Composable () -> Unit)? = null,
    onClick: () -> Unit
) {
    val backgroundColor = if (isSelected) {
        BrownColor.Primary.copy(alpha = 0.1f)
    } else {
        androidx.compose.ui.graphics.Color.Transparent
    }

    val borderColor = if (isSelected) {
        BrownColor.Primary.copy(alpha = 0.2f)
    } else {
        androidx.compose.ui.graphics.Color.Transparent
    }

    val contentColor = if (isSelected) {
        BrownColor.Primary
    } else {
        BrownColor.TextSubLight
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(RoundedCornerShape(BrownShape.medium))
            .background(backgroundColor)
            .border(1.dp, borderColor, RoundedCornerShape(BrownShape.medium))
            .clickable(onClick = {
                Log.d(TAG, "Menu item clicked: $label")
                onClick()
            })
            .padding(horizontal = BrownSpacing.space4),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(BrownSpacing.space4)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = contentColor,
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
            color = if (isSelected) BrownColor.TextMainLight else BrownColor.TextMainLight.copy(alpha = 0.7f),
            modifier = Modifier.weight(1f)
        )
        if (badge != null) {
            badge()
        }
    }
}

/**
 * CountBadge - Number badge component
 *
 * Specs:
 * - Background: Primary
 * - Text: White, 10sp, Bold
 * - Min width: 20dp, height: 20dp
 * - Horizontal padding: 6dp
 */
@Composable
fun CountBadge(
    count: Int
) {
    Box(
        modifier = Modifier
            .height(20.dp)
            .background(BrownColor.Primary, CircleShape)
            .padding(horizontal = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = count.toString(),
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = androidx.compose.ui.graphics.Color.White
        )
    }
}

/**
 * NotificationDot - Notification indicator dot
 *
 * Specs:
 * - Size: 8dp
 * - Color: Error (red)
 * - Positioned at icon's top right
 */
@Composable
fun NotificationDot() {
    Box(
        modifier = Modifier
            .size(8.dp)
            .background(BrownColor.Error, CircleShape)
    )
}

/**
 * SideMenuFooter - Footer section with logout button and version
 *
 * Specs:
 * - Top border: BorderLight
 * - Log Out button: SurfaceVariant background, 48dp height, full width
 * - Version text: 10sp, TextSubLight, center aligned
 * - Bottom padding: 40dp (safe area)
 */
@Composable
private fun SideMenuFooter(
    onLogout: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(BrownColor.BackgroundLight)
            .padding(
                top = BrownSpacing.space6,
                bottom = 40.dp,
                start = BrownSpacing.space6,
                end = BrownSpacing.space6
            )
    ) {
        // Top border
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(BrownColor.BorderLight)
        )

        Spacer(modifier = Modifier.height(BrownSpacing.space6))

        // Log Out button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .clip(RoundedCornerShape(BrownShape.medium))
                .background(BrownColor.SurfaceVariant)
                .clickable(onClick = {
                    Log.d(TAG, "Logout clicked")
                    onLogout()
                })
                .padding(horizontal = BrownSpacing.space4),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Logout,
                contentDescription = null,
                tint = BrownColor.TextMainLight.copy(alpha = 0.6f),
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(BrownSpacing.space2))
            Text(
                text = "Log Out",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = BrownColor.TextMainLight.copy(alpha = 0.6f)
            )
        }

        Spacer(modifier = Modifier.height(BrownSpacing.space4))

        // Version text
        Text(
            text = "Social Tree v2.4.0",
            fontSize = 10.sp,
            color = BrownColor.TextSubLight,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}

// Previews
@Preview(name = "Side Menu - Light", showBackground = true)
@Preview(name = "Side Menu - Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SideMenuDrawerPreview() {
    SocialTreeTheme {
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Open)
        val scope = rememberCoroutineScope()

        SideMenuDrawer(
            drawerState = drawerState,
            onCloseDrawer = { scope.launch { drawerState.close() } },
            onViewProfile = {},
            onFriendsClick = {},
            onFamilyGroupsClick = {},
            onMyTreeClick = {},
            onNotificationsClick = {},
            onSettingsClick = {},
            onHelpSupportClick = {},
            onLogoutClick = {}
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BrownColor.BackgroundLight)
            )
        }
    }
}

@Preview(name = "Menu Item - Normal", showBackground = true)
@Composable
private fun SideMenuItemPreview() {
    SocialTreeTheme {
        Column(
            modifier = Modifier
                .width(300.dp)
                .padding(BrownSpacing.space4),
            verticalArrangement = Arrangement.spacedBy(BrownSpacing.space2)
        ) {
            SideMenuItem(
                icon = Icons.Default.Group,
                label = "Friends (친구)",
                isSelected = false,
                badge = { CountBadge(count = 12) },
                onClick = {}
            )
            SideMenuItem(
                icon = Icons.Default.Group,
                label = "Friends (친구)",
                isSelected = true,
                badge = { CountBadge(count = 12) },
                onClick = {}
            )
            SideMenuItem(
                icon = Icons.Default.Notifications,
                label = "Notifications",
                badge = { NotificationDot() },
                onClick = {}
            )
        }
    }
}
