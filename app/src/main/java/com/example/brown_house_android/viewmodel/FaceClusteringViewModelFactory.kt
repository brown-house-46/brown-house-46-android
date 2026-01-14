package com.example.brown_house_android.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.brown_house_android.face.FaceDetector
import com.example.brown_house_android.face.FaceEmbedder
import com.example.brown_house_android.orchestrator.FaceClusteringOrchestrator

/**
 * FaceClusteringViewModel을 생성하는 Factory
 *
 * 의존성 주입을 처리합니다.
 */
class FaceClusteringViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FaceClusteringViewModel::class.java)) {
            val faceDetector = FaceDetector()
            val faceEmbedder = if (FaceEmbedder.isModelAvailable(context)) {
                FaceEmbedder(context)
            } else null

            val orchestrator = FaceClusteringOrchestrator(
                faceDetector = faceDetector,
                faceEmbedder = faceEmbedder
            )

            @Suppress("UNCHECKED_CAST")
            return FaceClusteringViewModel(orchestrator) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
