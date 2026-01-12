package com.example.brown_house_android.socialtree.core.designsystem.tokens

import androidx.compose.ui.unit.dp

/**
 * Design Tokens - Spacing Scale
 * Based on Tailwind spacing (4px base unit) from HTML wireframes
 *
 * Usage examples from wireframes:
 * - p-3 (padding: 12px) → BrownSpacing.space3
 * - gap-4 (gap: 16px) → BrownSpacing.space4
 * - px-5 (horizontal padding: 20px) → BrownSpacing.space5
 */
object BrownSpacing {
    val space0 = 0.dp      // No spacing
    val space1 = 4.dp      // p-1, gap-1
    val space2 = 8.dp      // p-2, gap-2
    val space3 = 12.dp     // p-3, gap-3 (common in wireframes)
    val space4 = 16.dp     // p-4, gap-4 (very common)
    val space5 = 20.dp     // p-5, gap-5 (screen padding)
    val space6 = 24.dp     // p-6, gap-6
    val space8 = 32.dp     // p-8
    val space10 = 40.dp    // p-10
    val space12 = 48.dp    // p-12
    val space16 = 64.dp    // p-16

    // Common patterns from HTML wireframes
    val screenPadding = space5         // px-5 (most common screen padding)
    val screenPaddingLarge = space6    // px-6 (some screens)
    val cardPadding = space4           // p-4 (card internal padding)
    val sectionSpacing = space6        // gap-6 between major sections
    val itemSpacing = space3           // gap-3 between list items
    val buttonPadding = space6         // px-6 button horizontal padding
}
