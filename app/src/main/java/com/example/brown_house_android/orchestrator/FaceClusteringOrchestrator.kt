package com.example.brown_house_android.orchestrator

import android.content.Context
import android.net.Uri
import android.util.Log
import com.example.brown_house_android.face.FaceClusterer
import com.example.brown_house_android.face.FaceCropper
import com.example.brown_house_android.face.FaceDetector
import com.example.brown_house_android.face.FaceEmbedder
import com.example.brown_house_android.model.FaceClusteringResult
import com.example.brown_house_android.util.ImageLoader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

/**
 * 얼굴 검출 → 크롭 → 임베딩 → 클러스터링 파이프라인을 조정하는 Orchestrator
 *
 * MainActivity에서 분리하여 책임을 분리하고 테스트 가능성을 향상시킵니다.
 */
class FaceClusteringOrchestrator(
    private val faceDetector: FaceDetector,
    private val faceEmbedder: FaceEmbedder?,
    private val imageLoader: ImageLoader = ImageLoader,
    private val faceCropper: FaceCropper = FaceCropper,
    private val faceClusterer: FaceClusterer = FaceClusterer()
) {
    /**
     * 여러 이미지의 얼굴을 검출하고 클러스터링
     *
     * @param context Android Context (ImageLoader에 필요)
     * @param imageUris 처리할 이미지 URI 리스트
     * @return Flow<FaceClusteringResult> - 진행 상황 및 최종 결과
     */
    suspend fun processImages(
        context: Context,
        imageUris: List<Uri>
    ): Flow<FaceClusteringResult> = flow {
        val resultList = mutableListOf<String>()
        val allFaceData = mutableListOf<FaceClusterer.FaceData>()

        imageUris.forEachIndexed { imageIndex, uri ->
            try {
                // 진행 상황 emit
                emit(FaceClusteringResult.Progress(
                    currentImage = imageIndex + 1,
                    totalImages = imageUris.size,
                    message = "사진 ${imageIndex + 1}/${imageUris.size} 처리 중..."
                ))

                // 1. URI → Bitmap 변환
                val bitmap = imageLoader.loadBitmapFromUri(
                    context = context,
                    uri = uri
                )

                if (bitmap != null) {
                    // 2. 얼굴 검출 (상세 정보 포함)
                    val faces = faceDetector.detectFacesWithDetails(bitmap)
                    val faceCount = faces.size

                    val message = "사진 ${imageIndex + 1}: 얼굴 ${faceCount}개 발견"
                    Log.d("FaceDetection", message)
                    resultList.add(message)

                    emit(FaceClusteringResult.ImageProcessed(
                        imageIndex = imageIndex + 1,
                        faceCount = faceCount
                    ))

                    // 3. FaceEmbedder가 있으면 임베딩 생성
                    if (faceEmbedder != null && faces.isNotEmpty()) {
                        faces.forEachIndexed { faceIndex, face ->
                            // 얼굴 잘라내기
                            val croppedFace = faceCropper.cropFace(bitmap, face)

                            if (croppedFace != null) {
                                // 임베딩 생성
                                val embedding = faceEmbedder.getEmbedding(croppedFace)

                                if (embedding != null) {
                                    allFaceData.add(
                                        FaceClusterer.FaceData(
                                            embedding = embedding,
                                            bitmap = croppedFace,
                                            rect = face.boundingBox,
                                            imageIndex = imageIndex + 1,
                                            faceIndex = faceIndex
                                        )
                                    )
                                }
                            }
                        }
                    }

                    bitmap.recycle()
                } else {
                    val errorMsg = "사진 ${imageIndex + 1}: 로드 실패"
                    Log.e("FaceDetection", errorMsg)
                    resultList.add(errorMsg)
                }
            } catch (e: Exception) {
                val errorMsg = "사진 ${imageIndex + 1}: 오류 - ${e.message}"
                Log.e("FaceDetection", errorMsg, e)

                emit(FaceClusteringResult.Error(
                    imageIndex = imageIndex + 1,
                    message = errorMsg,
                    throwable = e
                ))
            }
        }

        // 4. 클러스터링
        var summary: FaceClusterer.ClusteringSummary? = null
        if (allFaceData.isNotEmpty()) {
            val clusters = faceClusterer.cluster(allFaceData)
            summary = faceClusterer.summarize(clusters)

            Log.d("FaceClustering", "총 ${summary.totalFaces}개 얼굴, ${summary.totalPeople}명 인물")

            // 메모리 최적화: 대표 얼굴을 제외한 나머지 비트맵 해제
            val representativeBitmaps = summary.clusters.map { it.representativeFace }.toSet()
            allFaceData.forEach { faceData ->
                if (faceData.bitmap !in representativeBitmaps) {
                    faceData.bitmap.recycle()
                }
            }
        }

        // 최종 결과 emit
        emit(FaceClusteringResult.Success(
            summary = summary ?: FaceClusterer.ClusteringSummary(
                totalFaces = 0,
                totalPeople = 0,
                clusters = emptyList()
            ),
            detectionResults = resultList
        ))
    }.flowOn(Dispatchers.IO)

    /**
     * 리소스 해제
     */
    fun close() {
        faceDetector.close()
        faceEmbedder?.close()
    }
}
