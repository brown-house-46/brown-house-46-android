package com.example.brown_house_android.socialtree.core.designsystem.tokens

import androidx.compose.ui.graphics.Color

/**
 * Design Tokens - Color Palette
 * Extracted from HTML wireframes with warm earthy tones
 */
object BrownColor {
    // Primary Colors - Warm Earthy Palette
    val Primary = Color(0xFF4A4543)           // Deep warm charcoal
    val PrimaryHover = Color(0xFF363230)
    val PrimaryDark = Color(0xFF44403C)

    // Accent Colors
    val Accent = Color(0xFFECA413)            // Warm gold/amber
    val AccentLight = Color(0xFFE8AB30)
    val AccentSoft = Color(0xFFE8E4D9)
    val AccentOrange = Color(0xFFE64C19)      // Burnt orange (room selection)

    // Background Colors
    val BackgroundLight = Color(0xFFF2F0ED)   // Muted warm beige-grey
    val BackgroundDark = Color(0xFF1C1A19)    // Deep warm dark
    val SurfaceLight = Color(0xFFFFFFFF)
    val SurfaceDark = Color(0xFF2B2826)
    val SurfaceVariant = Color(0xFFF9F8F6)    // Slightly darker than white

    // Text Colors
    val TextMainLight = Color(0xFF2D2826)     // Soft black
    val TextMainDark = Color(0xFFE6E2DF)      // Warm off-white
    val TextSubLight = Color(0xFF8C8684)      // Muted warm grey
    val TextSubDark = Color(0xFF9E9895)
    val TextTertiary = Color(0xFF78716C)      // Even more muted

    // Border Colors
    val BorderLight = Color(0xFFEBE7E4)
    val BorderDark = Color(0xFF3D3836)

    // Input Colors
    val InputBg = Color(0xFFFAF9F8)

    // Semantic Colors
    val Success = Color(0xFF6B8E23)           // Olive green
    val Error = Color(0xFFB85C50)             // Muted red
    val Warning = Color(0xFFD4A574)           // Warm beige
    val Info = Color(0xFF7B8FA3)              // Muted blue

    // Additional Colors from Wireframes
    val Charcoal = Color(0xFF433E33)          // Deep charcoal (node info page)
    val Taupe = Color(0xFF5C554B)             // Taupe brown (login)
    val Beige = Color(0xFFD1C7B1)             // Light beige
    val Golden = Color(0xFFECA413)            // Golden yellow

    // Colors from HTML Document Designs
    // From 노드_정보_입력_페이지 (Input Page)
    val InputPrimary = Color(0xFF433E33)      // Warm Deep Charcoal for actions
    val InputPrimaryForeground = Color(0xFFF5F2EB)  // Light Cream text on primary
    val InputBgLight = Color(0xFFF5F2EB)      // Warm Beige Background
    val InputBgDark = Color(0xFF262522)       // Warm Dark Background
    val InputSurfaceLight = Color(0xFFFFFFFF) // White Surface for inputs
    val InputSurfaceDark = Color(0xFF33312C)  // Dark Warm Surface
    val InputTextMain = Color(0xFF2D2A26)     // Soft Black
    val InputTextSub = Color(0xFF8E8980)      // Warm Grey
    val InputAccent = Color(0xFFE8E4D9)       // Soft Beige Accents
    val InputPlaceholder = Color(0xFFD1D1D1)  // Placeholder gray

    // From 노드_상세정보_페이지 (Detail Page)
    val DetailPrimary = Color(0xFFD1C7B1)     // Primary for detail page
    val DetailBgLight = Color(0xFFF5F4F2)     // Background light
    val DetailBgDark = Color(0xFF262320)      // Background dark
    val DetailSurfaceLight = Color(0xFFFFFFFF)// Surface light
    val DetailSurfaceDark = Color(0xFF2E2B28) // Surface dark
    val DetailTextMain = Color(0xFF44403C)    // Text main
    val DetailTextSub = Color(0xFF78716C)     // Text sub
    val DetailBorder = Color(0xFFE7E5E4)      // Border color
    val DetailIconBg = Color(0xFFF5F4F2)      // Icon circle background (same as bg-light)
}
