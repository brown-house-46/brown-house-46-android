package com.example.brown_house_android.model

import com.example.brown_house_android.face.FaceClusterer

/**
 * UI에서 관찰하는 얼굴 클러스터링 상태
 */
sealed class FaceClusteringUiState {
    /**
     * 초기 상태 (아무것도 처리하지 않음)
     */
    object Idle : FaceClusteringUiState()

    /**
     * 처리 중
     */
    data class Processing(
        val current: Int,
        val total: Int,
        val message: String
    ) : FaceClusteringUiState()

    /**
     * 처리 완료
     */
    data class Success(
        val summary: FaceClusterer.ClusteringSummary,
        val detectionResults: List<String>
    ) : FaceClusteringUiState()

    /**
     * 오류 발생
     */
    data class Error(val message: String) : FaceClusteringUiState()
}
