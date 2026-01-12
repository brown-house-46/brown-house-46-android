package com.example.brown_house_android.socialtree.core.designsystem.foundation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.brown_house_android.socialtree.core.designsystem.tokens.BrownColor
import com.example.brown_house_android.socialtree.core.designsystem.tokens.BrownSpacing

/**
 * Profile photo uploader component
 * Based on HTML design: 144dp circular photo with camera icon overlay
 */
@Composable
fun BrownProfilePhotoUploader(
    imageUrl: String?,
    onPhotoClick: () -> Unit,
    modifier: Modifier = Modifier,
    label: String = "사진 업로드"
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Box(
            contentAlignment = Alignment.BottomEnd,
            modifier = Modifier
                .size(144.dp)
                .clickable(onClick = onPhotoClick)
        ) {
            // Main photo circle
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .shadow(
                        elevation = 8.dp,
                        shape = CircleShape,
                        ambientColor = BrownColor.Primary.copy(alpha = 0.06f),
                        spotColor = BrownColor.Primary.copy(alpha = 0.06f)
                    )
                    .border(
                        width = 6.dp,
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
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            // Camera icon button overlay
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(56.dp)
                    .shadow(
                        elevation = 4.dp,
                        shape = CircleShape
                    )
                    .border(
                        width = 5.dp,
                        color = BrownColor.InputBgLight,
                        shape = CircleShape
                    )
                    .clip(CircleShape)
                    .background(BrownColor.InputPrimary)
            ) {
                Icon(
                    imageVector = Icons.Default.CameraAlt,
                    contentDescription = "Upload photo",
                    tint = Color.White,
                    modifier = Modifier.size(22.dp)
                )
            }
        }

        // Label text
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = BrownColor.InputTextSub,
            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
            modifier = Modifier.padding(top = BrownSpacing.space4)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F2EB)
@Composable
private fun BrownProfilePhotoUploaderPreview() {
    MaterialTheme {
        Box(
            modifier = Modifier.padding(BrownSpacing.space6),
            contentAlignment = Alignment.Center
        ) {
            BrownProfilePhotoUploader(
                imageUrl = null,
                onPhotoClick = {}
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F2EB)
@Composable
private fun BrownProfilePhotoUploaderWithImagePreview() {
    MaterialTheme {
        Box(
            modifier = Modifier.padding(BrownSpacing.space6),
            contentAlignment = Alignment.Center
        ) {
            BrownProfilePhotoUploader(
                imageUrl = "https://via.placeholder.com/144",
                onPhotoClick = {}
            )
        }
    }
}
