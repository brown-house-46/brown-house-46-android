package com.example.brown_house_android.face

import android.graphics.Bitmap
import android.graphics.Rect
import com.google.mlkit.vision.face.Face
import kotlin.math.max
import kotlin.math.min

/**
 * 얼굴 영역 잘라내기 유틸리티
 */
object FaceCropper {

    /**
     * 검출된 얼굴을 정사각형으로 잘라내고 112x112px로 리사이징
     *
     * @param bitmap 원본 이미지
     * @param face ML Kit Face 객체
     * @param targetSize 출력 크기 (기본 112x112)
     * @param padding 얼굴 주변 여백 비율 (기본 0.2 = 20%)
     * @return 잘라낸 얼굴 Bitmap 또는 null
     */
    fun cropFace(
        bitmap: Bitmap,
        face: Face,
        targetSize: Int = 112,
        padding: Float = 0.2f
    ): Bitmap? {
        return try {
            val boundingBox = face.boundingBox

            // 정사각형 영역 계산 (패딩 포함)
            val squareRect = calculateSquareRect(boundingBox, bitmap.width, bitmap.height, padding)

            // 영역 잘라내기
            val croppedBitmap = Bitmap.createBitmap(
                bitmap,
                squareRect.left,
                squareRect.top,
                squareRect.width(),
                squareRect.height()
            )

            // 112x112로 리사이징
            val resizedBitmap = Bitmap.createScaledBitmap(
                croppedBitmap,
                targetSize,
                targetSize,
                true
            )

            // 중간 Bitmap 해제
            if (croppedBitmap != resizedBitmap) {
                croppedBitmap.recycle()
            }

            resizedBitmap
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * 여러 얼굴을 한 번에 잘라내기
     *
     * @param bitmap 원본 이미지
     * @param faces Face 객체 리스트
     * @param targetSize 출력 크기
     * @return 잘라낸 얼굴 Bitmap 리스트
     */
    fun cropFaces(
        bitmap: Bitmap,
        faces: List<Face>,
        targetSize: Int = 112
    ): List<Bitmap> {
        return faces.mapNotNull { face ->
            cropFace(bitmap, face, targetSize)
        }
    }

    /**
     * 바운딩 박스를 정사각형으로 확장하고 패딩 추가
     *
     * @param rect 원본 바운딩 박스
     * @param imageWidth 이미지 너비
     * @param imageHeight 이미지 높이
     * @param padding 패딩 비율
     * @return 정사각형 Rect
     */
    private fun calculateSquareRect(
        rect: Rect,
        imageWidth: Int,
        imageHeight: Int,
        padding: Float
    ): Rect {
        val width = rect.width()
        val height = rect.height()

        // 더 긴 쪽을 기준으로 정사각형 크기 결정
        val size = max(width, height)

        // 패딩 적용
        val sizeWithPadding = (size * (1 + padding)).toInt()

        // 중심점 계산
        val centerX = rect.centerX()
        val centerY = rect.centerY()

        // 정사각형 좌표 계산
        var left = centerX - sizeWithPadding / 2
        var top = centerY - sizeWithPadding / 2
        var right = centerX + sizeWithPadding / 2
        var bottom = centerY + sizeWithPadding / 2

        // 이미지 경계 내로 제한
        if (left < 0) {
            right = min(right - left, imageWidth)
            left = 0
        }
        if (top < 0) {
            bottom = min(bottom - top, imageHeight)
            top = 0
        }
        if (right > imageWidth) {
            left = max(0, left - (right - imageWidth))
            right = imageWidth
        }
        if (bottom > imageHeight) {
            top = max(0, top - (bottom - imageHeight))
            bottom = imageHeight
        }

        // 최종 크기 조정 (정사각형 유지)
        val finalSize = min(right - left, bottom - top)
        right = left + finalSize
        bottom = top + finalSize

        return Rect(left, top, right, bottom)
    }

    /**
     * 얼굴 영역 정보를 담는 데이터 클래스
     */
    data class CroppedFace(
        val bitmap: Bitmap,
        val originalRect: Rect,
        val faceIndex: Int
    )

    /**
     * 얼굴 잘라내기 + 메타데이터 포함
     *
     * @param bitmap 원본 이미지
     * @param faces Face 객체 리스트
     * @param targetSize 출력 크기
     * @return CroppedFace 리스트
     */
    fun cropFacesWithMetadata(
        bitmap: Bitmap,
        faces: List<Face>,
        targetSize: Int = 112
    ): List<CroppedFace> {
        return faces.mapIndexedNotNull { index, face ->
            cropFace(bitmap, face, targetSize)?.let { croppedBitmap ->
                CroppedFace(
                    bitmap = croppedBitmap,
                    originalRect = face.boundingBox,
                    faceIndex = index
                )
            }
        }
    }
}
