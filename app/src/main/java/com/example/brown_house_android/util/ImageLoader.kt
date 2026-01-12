package com.example.brown_house_android.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import androidx.exifinterface.media.ExifInterface
import java.io.IOException

object ImageLoader {

    /**
     * URI를 Bitmap으로 변환
     * @param context Android Context
     * @param uri 이미지 URI
     * @param maxDimension 최대 너비/높이 (기본 1024px)
     * @return Bitmap 또는 null
     */
    fun loadBitmapFromUri(
        context: Context,
        uri: Uri,
        maxDimension: Int = 1024
    ): Bitmap? {
        return try {
            // 1단계: 이미지 크기 측정 (inJustDecodeBounds)
            val options = BitmapFactory.Options().apply {
                inJustDecodeBounds = true
            }
            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                BitmapFactory.decodeStream(inputStream, null, options)
            }

            // 2단계: 샘플링 계산
            options.inSampleSize = calculateInSampleSize(
                options.outWidth,
                options.outHeight,
                maxDimension
            )
            options.inJustDecodeBounds = false

            // 3단계: 실제 디코딩
            val bitmap = context.contentResolver.openInputStream(uri)?.use { inputStream ->
                BitmapFactory.decodeStream(inputStream, null, options)
            }

            // 4단계: EXIF 회전 정보 적용
            bitmap?.let { rotateImageIfRequired(context, it, uri) }

        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    /**
     * 적절한 샘플링 크기 계산
     */
    private fun calculateInSampleSize(
        width: Int,
        height: Int,
        maxDimension: Int
    ): Int {
        var inSampleSize = 1
        if (width > maxDimension || height > maxDimension) {
            val halfWidth = width / 2
            val halfHeight = height / 2
            while ((halfWidth / inSampleSize) >= maxDimension &&
                   (halfHeight / inSampleSize) >= maxDimension) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }

    /**
     * EXIF 정보를 기반으로 이미지 회전
     */
    private fun rotateImageIfRequired(
        context: Context,
        bitmap: Bitmap,
        uri: Uri
    ): Bitmap {
        return context.contentResolver.openInputStream(uri)?.use { inputStream ->
            val exif = ExifInterface(inputStream)
            val orientation = exif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL
            )

            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> rotateBitmap(bitmap, 90f)
                ExifInterface.ORIENTATION_ROTATE_180 -> rotateBitmap(bitmap, 180f)
                ExifInterface.ORIENTATION_ROTATE_270 -> rotateBitmap(bitmap, 270f)
                else -> bitmap
            }
        } ?: bitmap
    }

    private fun rotateBitmap(bitmap: Bitmap, degrees: Float): Bitmap {
        val matrix = Matrix().apply { postRotate(degrees) }
        val rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)

        // 새로운 비트맵이 생성된 경우에만 원본을 해제
        if (rotatedBitmap != bitmap) {
            bitmap.recycle()
        }

        return rotatedBitmap
    }
}
