package com.example.brown_house_android.socialtree.core.designsystem.foundation

import androidx.compose.foundation.layout.size
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.brown_house_android.socialtree.core.designsystem.tokens.BrownColor
import com.example.brown_house_android.socialtree.core.designsystem.tokens.BrownShape

@Composable
fun BrownIconButton(
    icon: ImageVector,
    contentDescription: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick,
        modifier = modifier.size(40.dp),
        colors = IconButtonDefaults.iconButtonColors(
            contentColor = BrownColor.Primary
        )
    ) {
        Icon(imageVector = icon, contentDescription = contentDescription)
    }
}

@Composable
fun BrownFloatingActionButton(
    icon: ImageVector,
    contentDescription: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    FloatingActionButton(
        onClick = onClick,
        modifier = modifier,
        containerColor = BrownColor.Primary,
        contentColor = BrownColor.TextMainDark,
        shape = BrownShape.fullShape
    ) {
        Icon(imageVector = icon, contentDescription = contentDescription)
    }
}
