package com.example.brown_house_android.socialtree.core.designsystem.foundation

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import com.example.brown_house_android.socialtree.core.designsystem.theme.SocialTreeTheme
import com.example.brown_house_android.socialtree.core.designsystem.tokens.BrownColor
import com.example.brown_house_android.socialtree.core.designsystem.tokens.BrownShape
import com.example.brown_house_android.socialtree.core.designsystem.tokens.BrownSpacing

/**
 * BrownTextField - Custom text field from wireframes
 *
 * Specs:
 * - Height: h-14 or h-16
 * - Border radius: rounded-2xl (24dp)
 * - Padding: px-6
 * - Icon: Material icon on leading/trailing
 * - Background: white/surface
 * - Shadow: shadow-sm with focus effect
 */

@Composable
fun BrownTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String = "",
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
    onTrailingIconClick: (() -> Unit)? = null,
    isError: Boolean = false,
    errorMessage: String? = null,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    singleLine: Boolean = true,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default
) {
    Column(modifier = modifier) {
        // Label
        if (label != null) {
            Text(
                text = label,
                style = androidx.compose.material3.MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold,
                color = BrownColor.TextMainLight,
                modifier = Modifier.padding(start = BrownSpacing.space2)
            )
            Spacer(modifier = Modifier.height(BrownSpacing.space2))
        }

        // Text Field
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(
                    text = placeholder,
                    color = BrownColor.TextSubLight.copy(alpha = 0.4f)
                )
            },
            leadingIcon = if (leadingIcon != null) {
                {
                    Icon(
                        imageVector = leadingIcon,
                        contentDescription = null,
                        tint = BrownColor.TextSubLight
                    )
                }
            } else null,
            trailingIcon = if (trailingIcon != null) {
                {
                    if (onTrailingIconClick != null) {
                        IconButton(onClick = onTrailingIconClick) {
                            Icon(
                                imageVector = trailingIcon,
                                contentDescription = null,
                                tint = BrownColor.TextSubLight
                            )
                        }
                    } else {
                        Icon(
                            imageVector = trailingIcon,
                            contentDescription = null,
                            tint = BrownColor.TextSubLight
                        )
                    }
                }
            } else null,
            isError = isError,
            enabled = enabled,
            readOnly = readOnly,
            singleLine = singleLine,
            maxLines = maxLines,
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            shape = RoundedCornerShape(BrownShape.input),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = BrownColor.SurfaceLight,
                unfocusedContainerColor = BrownColor.SurfaceLight,
                disabledContainerColor = BrownColor.SurfaceLight.copy(alpha = 0.5f),
                focusedBorderColor = BrownColor.Primary.copy(alpha = 0.3f),
                unfocusedBorderColor = Color.Transparent,
                errorBorderColor = BrownColor.Error,
                focusedTextColor = BrownColor.TextMainLight,
                unfocusedTextColor = BrownColor.TextMainLight
            ),
            textStyle = androidx.compose.material3.MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Medium
            )
        )

        // Error message
        if (isError && errorMessage != null) {
            Spacer(modifier = Modifier.height(BrownSpacing.space1))
            Text(
                text = errorMessage,
                style = androidx.compose.material3.MaterialTheme.typography.bodySmall,
                color = BrownColor.Error,
                modifier = Modifier.padding(start = BrownSpacing.space2)
            )
        }
    }
}

/**
 * Password field variant with visibility toggle
 */
@Composable
fun BrownPasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String = "비밀번호를 입력하세요",
    isError: Boolean = false,
    errorMessage: String? = null,
    enabled: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
    keyboardActions: KeyboardActions = KeyboardActions.Default
) {
    var passwordVisible by remember { mutableStateOf(false) }

    BrownTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        label = label,
        placeholder = placeholder,
        trailingIcon = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
        onTrailingIconClick = { passwordVisible = !passwordVisible },
        isError = isError,
        errorMessage = errorMessage,
        enabled = enabled,
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions
    )
}

// Previews
@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun BrownTextFieldPreview() {
    SocialTreeTheme {
        Column(modifier = Modifier.padding(BrownSpacing.space5)) {
            var email by remember { mutableStateOf("") }
            BrownTextField(
                value = email,
                onValueChange = { email = it },
                label = "이메일",
                placeholder = "이메일을 입력하세요",
                leadingIcon = Icons.Default.Email
            )
        }
    }
}

@Preview(name = "Password Field", showBackground = true)
@Composable
private fun BrownPasswordFieldPreview() {
    SocialTreeTheme {
        Column(modifier = Modifier.padding(BrownSpacing.space5)) {
            var password by remember { mutableStateOf("") }
            BrownPasswordField(
                value = password,
                onValueChange = { password = it },
                label = "비밀번호"
            )
        }
    }
}

@Preview(name = "Error State", showBackground = true)
@Composable
private fun BrownTextFieldErrorPreview() {
    SocialTreeTheme {
        Column(modifier = Modifier.padding(BrownSpacing.space5)) {
            BrownTextField(
                value = "invalid@",
                onValueChange = {},
                label = "이메일",
                placeholder = "이메일을 입력하세요",
                leadingIcon = Icons.Default.Email,
                isError = true,
                errorMessage = "올바른 이메일 형식이 아닙니다"
            )
        }
    }
}

@Preview(name = "Filled State", showBackground = true)
@Composable
private fun BrownTextFieldFilledPreview() {
    SocialTreeTheme {
        Column(modifier = Modifier.padding(BrownSpacing.space5)) {
            BrownTextField(
                value = "user@example.com",
                onValueChange = {},
                label = "이메일",
                placeholder = "이메일을 입력하세요",
                leadingIcon = Icons.Default.Email
            )

            Spacer(modifier = Modifier.height(BrownSpacing.space4))

            var password by remember { mutableStateOf("password123") }
            BrownPasswordField(
                value = password,
                onValueChange = { password = it },
                label = "비밀번호"
            )
        }
    }
}
