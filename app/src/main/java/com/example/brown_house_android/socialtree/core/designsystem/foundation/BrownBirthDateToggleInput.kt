package com.example.brown_house_android.socialtree.core.designsystem.foundation

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.brown_house_android.socialtree.core.designsystem.tokens.BrownColor
import com.example.brown_house_android.socialtree.core.designsystem.tokens.BrownSpacing
import com.example.brown_house_android.socialtree.core.model.BirthDateMode

/**
 * Birth date toggle input component
 * Based on HTML design: Toggle switch (나이/년도) + number input with suffix
 */
@Composable
fun BrownBirthDateToggleInput(
    mode: BirthDateMode,
    value: String,
    onModeChange: (BirthDateMode) -> Unit,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(BrownSpacing.space3),
        modifier = modifier.fillMaxWidth()
    ) {
        // Toggle switch
        BirthDateToggle(
            mode = mode,
            onModeChange = onModeChange,
            modifier = Modifier.width(150.dp)
        )

        // Number input
        BirthDateNumberInput(
            value = value,
            suffix = if (mode == BirthDateMode.AGE) "세" else "년",
            onValueChange = onValueChange,
            placeholder = "0",
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun BirthDateToggle(
    mode: BirthDateMode,
    onModeChange: (BirthDateMode) -> Unit,
    modifier: Modifier = Modifier
) {
    val offsetX by animateDpAsState(
        targetValue = if (mode == BirthDateMode.AGE) 0.dp else 69.dp,
        animationSpec = tween(durationMillis = 200),
        label = "toggle_offset"
    )

    Box(
        modifier = modifier
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
            .padding(6.dp)
    ) {
        // Animated background indicator
        Box(
            modifier = Modifier
                .offset(x = offsetX)
                .width(69.dp)
                .fillMaxHeight()
                .clip(RoundedCornerShape(16.dp))
                .background(BrownColor.InputAccent)
        )

        // Toggle buttons
        Row(
            modifier = Modifier.fillMaxSize()
        ) {
            ToggleButton(
                text = "나이",
                isSelected = mode == BirthDateMode.AGE,
                onClick = { onModeChange(BirthDateMode.AGE) },
                modifier = Modifier.weight(1f)
            )
            ToggleButton(
                text = "년도",
                isSelected = mode == BirthDateMode.YEAR,
                onClick = { onModeChange(BirthDateMode.YEAR) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun ToggleButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxHeight()
            .clickable(onClick = onClick)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = if (isSelected) BrownColor.InputTextMain else BrownColor.InputTextSub,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
        )
    }
}

@Composable
private fun BirthDateNumberInput(
    value: String,
    suffix: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
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
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.fillMaxSize()
        ) {
            BasicTextField(
                value = value,
                onValueChange = { newValue ->
                    // Only allow numbers
                    if (newValue.isEmpty() || newValue.all { it.isDigit() }) {
                        onValueChange(newValue)
                    }
                },
                textStyle = MaterialTheme.typography.titleLarge.copy(
                    color = BrownColor.InputTextMain,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.End
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                cursorBrush = SolidColor(BrownColor.InputPrimary),
                singleLine = true,
                decorationBox = { innerTextField ->
                    if (value.isEmpty()) {
                        Text(
                            text = placeholder,
                            style = MaterialTheme.typography.titleLarge,
                            color = BrownColor.InputPlaceholder,
                            textAlign = TextAlign.End
                        )
                    }
                    innerTextField()
                },
                modifier = Modifier.weight(1f)
            )

            Text(
                text = suffix,
                style = MaterialTheme.typography.bodyMedium,
                color = BrownColor.InputTextSub,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(start = BrownSpacing.space2)
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F2EB)
@Composable
private fun BrownBirthDateToggleInputPreview() {
    MaterialTheme {
        Box(modifier = Modifier.padding(BrownSpacing.space4)) {
            BrownBirthDateToggleInput(
                mode = BirthDateMode.AGE,
                value = "28",
                onModeChange = {},
                onValueChange = {}
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F2EB)
@Composable
private fun BrownBirthDateToggleInputYearPreview() {
    MaterialTheme {
        Box(modifier = Modifier.padding(BrownSpacing.space4)) {
            BrownBirthDateToggleInput(
                mode = BirthDateMode.YEAR,
                value = "1995",
                onModeChange = {},
                onValueChange = {}
            )
        }
    }
}
