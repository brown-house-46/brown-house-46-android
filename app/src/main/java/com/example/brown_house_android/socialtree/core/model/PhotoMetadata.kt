package com.example.brown_house_android.socialtree.core.model

/**
 * Photo metadata for displaying images in UI
 * Contains minimal information required to render images using Coil
 *
 * Usage:
 * - For mock data: Use android.resource:// URIs pointing to drawable resources
 * - For production: Use content:// URIs from MediaStore
 */
data class PhotoMetadata(
    val id: String,
    val uri: String,
    val takenAt: Long,
    val displayName: String? = null
)
