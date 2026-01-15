package com.example.brown_house_android.socialtree.core.designsystem.tokens

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp

/**
 * Design Tokens - Typography
 * Based on Noto Sans KR + HTML wireframe text sizes
 *
 * To use custom fonts (Noto Sans KR):
 * 1. Add font files to res/font/ directory
 * 2. Uncomment the FontFamily definition below
 * 3. Update displayFontFamily assignment
 */
object BrownTypography {
    // TODO: Add Noto Sans KR font files to res/font/
    // val notoSansKr = FontFamily(
    //     Font(R.font.noto_sans_kr_regular, FontWeight.Normal),
    //     Font(R.font.noto_sans_kr_medium, FontWeight.Medium),
    //     Font(R.font.noto_sans_kr_bold, FontWeight.Bold)
    // )

    // Using system default for now, replace with notoSansKr when fonts are added
    val displayFontFamily = FontFamily.Default
    val bodyFontFamily = FontFamily.Default

    // Text Sizes from HTML wireframes (text-xs to text-2xl)
    object Size {
        val xs = 12.sp        // text-xs
        val sm = 14.sp        // text-sm (text-[13px] rounded to 14sp)
        val base = 16.sp      // text-base
        val lg = 18.sp        // text-lg
        val xl = 20.sp        // text-xl
        val xl2 = 24.sp       // text-2xl
        val xl3 = 26.sp       // text-[26px]
    }

    // Line Height variants
    object LineHeight {
        val relaxed = 30.sp   // 메모, 긴 문단용 (bodyMedium 기준 1.5배)
    }

    /**
     * 메모, 긴 문단 등 읽기 편의를 위해 넓은 줄 간격이 필요한 텍스트용 스타일
     */
    val bodyMediumRelaxed = TextStyle(
        fontFamily = bodyFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = Size.sm,
        lineHeight = LineHeight.relaxed,
        letterSpacing = 0.25.sp
    )

    // Create Material3 Typography
    fun createTypography(): Typography = Typography(
        // Display - Large headlines
        displayLarge = TextStyle(
            fontFamily = displayFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = Size.xl3,
            lineHeight = 32.sp,
            letterSpacing = (-0.015).em
        ),
        displayMedium = TextStyle(
            fontFamily = displayFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = Size.xl2,
            lineHeight = 30.sp,
            letterSpacing = 0.em
        ),

        // Headline - Section headers
        headlineLarge = TextStyle(
            fontFamily = displayFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = Size.xl,
            lineHeight = 28.sp
        ),
        headlineMedium = TextStyle(
            fontFamily = displayFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = Size.lg,
            lineHeight = 24.sp
        ),

        // Title - Card/component titles
        titleLarge = TextStyle(
            fontFamily = displayFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = Size.lg,
            lineHeight = 24.sp
        ),
        titleMedium = TextStyle(
            fontFamily = displayFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = Size.base,
            lineHeight = 22.sp
        ),
        titleSmall = TextStyle(
            fontFamily = displayFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = Size.sm,
            lineHeight = 20.sp
        ),

        // Body - Main content
        bodyLarge = TextStyle(
            fontFamily = bodyFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = Size.base,
            lineHeight = 24.sp,
            letterSpacing = 0.5.sp
        ),
        bodyMedium = TextStyle(
            fontFamily = bodyFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = Size.sm,
            lineHeight = 20.sp,
            letterSpacing = 0.25.sp
        ),
        bodySmall = TextStyle(
            fontFamily = bodyFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = Size.xs,
            lineHeight = 16.sp,
            letterSpacing = 0.4.sp
        ),

        // Label - Buttons, chips, tabs
        labelLarge = TextStyle(
            fontFamily = bodyFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = Size.sm,
            lineHeight = 20.sp,
            letterSpacing = 0.1.sp
        ),
        labelMedium = TextStyle(
            fontFamily = bodyFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = Size.xs,
            lineHeight = 16.sp,
            letterSpacing = 0.5.sp
        ),
        labelSmall = TextStyle(
            fontFamily = bodyFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 11.sp,
            lineHeight = 16.sp,
            letterSpacing = 0.5.sp
        )
    )
}
