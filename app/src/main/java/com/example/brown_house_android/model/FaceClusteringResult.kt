package com.example.brown_house_android.model

import com.example.brown_house_android.face.FaceClusterer

/**
 * Orchestrator가 emit하는 얼굴 처리 결과
 */
sealed class FaceClusteringResult {
    /**
     * 진행 상황
     */
    data class Progress(
        val currentImage: Int,
        val totalImages: Int,
        val message: String
    ) : FaceClusteringResult()

    /**
     * 이미지 처리 완료
     */
    data class ImageProcessed(
        val imageIndex: Int,
        val faceCount: Int
    ) : FaceClusteringResult()

    /**
     * 전체 처리 성공
     */
    data class Success(
        val summary: FaceClusterer.ClusteringSummary,
        val detectionResults: List<String>
    ) : FaceClusteringResult()

    /**
     * 처리 중 오류 발생
     */
    data class Error(
        val imageIndex: Int?,
        val message: String,
        val throwable: Throwable
    ) : FaceClusteringResult()
}
