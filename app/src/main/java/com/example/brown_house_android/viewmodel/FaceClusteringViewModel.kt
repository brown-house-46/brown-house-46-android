package com.example.brown_house_android.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.brown_house_android.model.FaceClusteringResult
import com.example.brown_house_android.model.FaceClusteringUiState
import com.example.brown_house_android.orchestrator.FaceClusteringOrchestrator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * 얼굴 클러스터링 UI 상태를 관리하는 ViewModel
 *
 * Orchestrator를 호출하고 결과를 UI 상태로 변환합니다.
 */
class FaceClusteringViewModel(
    private val orchestrator: FaceClusteringOrchestrator
) : ViewModel() {

    private val _uiState = MutableStateFlow<FaceClusteringUiState>(
        FaceClusteringUiState.Idle
    )
    val uiState: StateFlow<FaceClusteringUiState> = _uiState.asStateFlow()

    /**
     * 이미지들을 처리하여 얼굴 클러스터링 수행
     */
    fun processImages(context: Context, imageUris: List<Uri>) {
        viewModelScope.launch {
            try {
                orchestrator.processImages(context, imageUris).collect { result ->
                    _uiState.value = when (result) {
                        is FaceClusteringResult.Progress -> {
                            FaceClusteringUiState.Processing(
                                current = result.currentImage,
                                total = result.totalImages,
                                message = result.message
                            )
                        }
                        is FaceClusteringResult.Success -> {
                            FaceClusteringUiState.Success(
                                summary = result.summary,
                                detectionResults = result.detectionResults
                            )
                        }
                        is FaceClusteringResult.Error -> {
                            Log.e("FaceClustering", "Error at image ${result.imageIndex}", result.throwable)
                            FaceClusteringUiState.Error(result.message)
                        }
                        is FaceClusteringResult.ImageProcessed -> {
                            // ImageProcessed는 로깅만 하고 UI 상태는 유지
                            Log.d("FaceClustering", "Image ${result.imageIndex}: ${result.faceCount} faces")
                            _uiState.value
                        }
                    }
                }
            } catch (e: Exception) {
                _uiState.value = FaceClusteringUiState.Error(
                    "처리 중 오류 발생: ${e.message}"
                )
                Log.e("FaceClustering", "Unexpected error", e)
            }
        }
    }

    /**
     * 상태를 초기화
     */
    fun reset() {
        _uiState.value = FaceClusteringUiState.Idle
    }

    override fun onCleared() {
        super.onCleared()
        orchestrator.close()
    }
}
