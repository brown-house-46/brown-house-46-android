package com.example.brown_house_android.socialtree.core.designsystem.foundation

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.brown_house_android.socialtree.core.designsystem.theme.SocialTreeTheme
import com.example.brown_house_android.socialtree.core.designsystem.tokens.BrownColor
import com.example.brown_house_android.socialtree.core.designsystem.tokens.BrownShape
import com.example.brown_house_android.socialtree.core.designsystem.tokens.BrownSpacing

/**
 * BrownButton - Custom button following HTML wireframe specs
 *
 * Specs from HTML:
 * - Height: h-14 (56dp) or h-16 (64dp)
 * - Border radius: rounded-2xl (24dp)
 * - Padding: px-5 to px-6
 * - Shadow: shadow-soft
 * - States: default, hover, disabled, loading
 */

enum class BrownButtonSize {
    MEDIUM,  // h-14 (56dp)
    LARGE    // h-16 (64dp)
}

enum class BrownButtonVariant {
    PRIMARY,    // Dark background, light text
    SECONDARY,  // Light background, dark text
    OUTLINE,    // Transparent with border
    TEXT        // No background, just text
}

@Composable
fun BrownButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    size: BrownButtonSize = BrownButtonSize.MEDIUM,
    variant: BrownButtonVariant = BrownButtonVariant.PRIMARY,
    contentPadding: PaddingValues = PaddingValues(
        horizontal = BrownSpacing.buttonPadding,
        vertical = BrownSpacing.space3
    ),
    content: @Composable RowScope.() -> Unit
) {
    val height = when (size) {
        BrownButtonSize.MEDIUM -> 56.dp
        BrownButtonSize.LARGE -> 64.dp
    }

    when (variant) {
        BrownButtonVariant.PRIMARY -> {
            Button(
                onClick = onClick,
                modifier = modifier.height(height),
                enabled = enabled && !isLoading,
                shape = RoundedCornerShape(BrownShape.button),
                colors = ButtonDefaults.buttonColors(
                    containerColor = BrownColor.Primary,
                    contentColor = BrownColor.TextMainDark,
                    disabledContainerColor = BrownColor.Primary.copy(alpha = 0.3f),
                    disabledContentColor = BrownColor.TextMainDark.copy(alpha = 0.5f)
                ),
                contentPadding = contentPadding,
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 2.dp,
                    pressedElevation = 4.dp,
                    disabledElevation = 0.dp
                )
            ) {
                ButtonContent(isLoading, content)
            }
        }

        BrownButtonVariant.SECONDARY -> {
            Button(
                onClick = onClick,
                modifier = modifier.height(height),
                enabled = enabled && !isLoading,
                shape = RoundedCornerShape(BrownShape.button),
                colors = ButtonDefaults.buttonColors(
                    containerColor = BrownColor.SurfaceLight,
                    contentColor = BrownColor.TextMainLight,
                    disabledContainerColor = BrownColor.SurfaceLight.copy(alpha = 0.5f),
                    disabledContentColor = BrownColor.TextSubLight
                ),
                contentPadding = contentPadding,
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 1.dp,
                    pressedElevation = 2.dp,
                    disabledElevation = 0.dp
                )
            ) {
                ButtonContent(isLoading, content)
            }
        }

        BrownButtonVariant.OUTLINE -> {
            OutlinedButton(
                onClick = onClick,
                modifier = modifier.height(height),
                enabled = enabled && !isLoading,
                shape = RoundedCornerShape(BrownShape.button),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color.Transparent,
                    contentColor = BrownColor.Primary
                ),
                border = ButtonDefaults.outlinedButtonBorder.copy(width = 1.5.dp),
                contentPadding = contentPadding
            ) {
                ButtonContent(isLoading, content)
            }
        }

        BrownButtonVariant.TEXT -> {
            TextButton(
                onClick = onClick,
                modifier = modifier.height(height),
                enabled = enabled && !isLoading,
                shape = RoundedCornerShape(BrownShape.button),
                colors = ButtonDefaults.textButtonColors(
                    contentColor = BrownColor.Primary
                ),
                contentPadding = contentPadding
            ) {
                ButtonContent(isLoading, content)
            }
        }
    }
}

@Composable
private fun RowScope.ButtonContent(
    isLoading: Boolean,
    content: @Composable RowScope.() -> Unit
) {
    if (isLoading) {
        CircularProgressIndicator(
            modifier = Modifier.size(24.dp),
            color = LocalContentColor.current,
            strokeWidth = 2.dp
        )
    } else {
        content()
    }
}

// Convenience functions
@Composable
fun BrownPrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false
) {
    BrownButton(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        enabled = enabled,
        isLoading = isLoading,
        size = BrownButtonSize.LARGE,
        variant = BrownButtonVariant.PRIMARY
    ) {
        Text(
            text = text,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun BrownSecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false
) {
    BrownButton(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        enabled = enabled,
        isLoading = isLoading,
        size = BrownButtonSize.LARGE,
        variant = BrownButtonVariant.SECONDARY
    ) {
        Text(
            text = text,
            fontWeight = FontWeight.SemiBold
        )
    }
}

// Previews
@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun BrownButtonPreview() {
    SocialTreeTheme {
        Row(
            modifier = Modifier.padding(BrownSpacing.space4),
            horizontalArrangement = Arrangement.spacedBy(BrownSpacing.space3),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BrownPrimaryButton(
                text = "로그인",
                onClick = {},
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Preview(name = "Button Variants", showBackground = true)
@Composable
private fun BrownButtonVariantsPreview() {
    SocialTreeTheme {
        Row(
            modifier = Modifier.padding(BrownSpacing.space5),
            horizontalArrangement = Arrangement.spacedBy(BrownSpacing.space3),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Primary
            BrownButton(
                onClick = {},
                variant = BrownButtonVariant.PRIMARY,
                modifier = Modifier.weight(1f)
            ) {
                Text("Primary")
            }

            // Secondary
            BrownButton(
                onClick = {},
                variant = BrownButtonVariant.SECONDARY,
                modifier = Modifier.weight(1f)
            ) {
                Text("Secondary")
            }
        }
    }
}

@Preview(name = "Button States", showBackground = true)
@Composable
private fun BrownButtonStatesPreview() {
    SocialTreeTheme {
        Row(
            modifier = Modifier.padding(BrownSpacing.space5),
            horizontalArrangement = Arrangement.spacedBy(BrownSpacing.space3),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Enabled
            BrownButton(
                onClick = {},
                modifier = Modifier.weight(1f)
            ) {
                Text("Enabled")
            }

            // Loading
            BrownButton(
                onClick = {},
                isLoading = true,
                modifier = Modifier.weight(1f)
            ) {
                Text("Loading")
            }

            // Disabled
            BrownButton(
                onClick = {},
                enabled = false,
                modifier = Modifier.weight(1f)
            ) {
                Text("Disabled")
            }
        }
    }
}
