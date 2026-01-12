package com.example.brown_house_android.socialtree.core.designsystem.tokens

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp

/**
 * Design Tokens - Shape/Border Radius
 * From HTML wireframes: rounded-lg, rounded-xl, rounded-2xl, rounded-3xl
 *
 * HTML Tailwind → Android dp conversion:
 * - rounded (0.5rem = 8px) → small
 * - rounded-lg (0.75rem = 12px) → medium
 * - rounded-xl (1rem = 16px) → large
 * - rounded-2xl (1.5rem = 24px) → extraLarge
 * - rounded-3xl (2rem = 32px) → extraLarge2
 * - rounded-full (9999px) → full
 */
object BrownShape {
    // Size values
    val small = 8.dp           // rounded (0.5rem)
    val medium = 12.dp         // rounded-lg (0.75rem)
    val large = 16.dp          // rounded-xl (1rem)
    val extraLarge = 24.dp     // rounded-2xl (1.5rem)
    val extraLarge2 = 32.dp    // rounded-3xl (2rem)
    val extraLarge3 = 48.dp    // rounded-3xl for large modals (3rem)
    val full = 9999.dp         // rounded-full (circular)

    // Shape objects (for Compose components)
    val smallShape = RoundedCornerShape(small)
    val mediumShape = RoundedCornerShape(medium)
    val largeShape = RoundedCornerShape(large)
    val extraLargeShape = RoundedCornerShape(extraLarge)
    val extraLarge2Shape = RoundedCornerShape(extraLarge2)
    val extraLarge3Shape = RoundedCornerShape(extraLarge3)
    val fullShape = RoundedCornerShape(full)

    // Common component shapes from wireframes
    val button = extraLarge           // h-14 rounded-2xl (buttons)
    val input = extraLarge            // h-16 rounded-2xl (text fields)
    val card = large                  // rounded-lg to rounded-2xl (cards)
    val modal = extraLarge3           // rounded-t-3xl (bottom sheets, modals)
    val modalTop = RoundedCornerShape(
        topStart = extraLarge3,
        topEnd = extraLarge3,
        bottomStart = 0.dp,
        bottomEnd = 0.dp
    )
    val avatar = full                 // circular avatars
    val chip = extraLarge             // rounded-2xl (filter chips, tags)
    val chipShape = RoundedCornerShape(chip)
}
