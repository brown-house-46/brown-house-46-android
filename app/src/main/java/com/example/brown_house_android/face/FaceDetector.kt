package com.example.brown_house_android.face

import android.graphics.Bitmap
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import kotlinx.coroutines.tasks.await

class FaceDetector {

    private val options = FaceDetectorOptions.Builder()
        .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
        .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_NONE)
        .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_NONE)
        .setMinFaceSize(0.15f) // 최소 얼굴 크기 (이미지의 15%)
        .build()

    private val detector = FaceDetection.getClient(options)

    /**
     * Bitmap에서 얼굴 개수 검출
     * @param bitmap 분석할 이미지
     * @return 검출된 얼굴 개수
     */
    suspend fun detectFaces(bitmap: Bitmap): Int {
        return try {
            val inputImage = InputImage.fromBitmap(bitmap, 0)
            val faces = detector.process(inputImage).await()
            faces.size
        } catch (e: Exception) {
            e.printStackTrace()
            0
        }
    }

    /**
     * Bitmap에서 얼굴 검출 (상세 정보 포함)
     * @param bitmap 분석할 이미지
     * @return 검출된 Face 객체 리스트
     */
    suspend fun detectFacesWithDetails(bitmap: Bitmap): List<Face> {
        return try {
            val inputImage = InputImage.fromBitmap(bitmap, 0)
            detector.process(inputImage).await()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    /**
     * 리소스 해제
     */
    fun close() {
        detector.close()
    }
}
