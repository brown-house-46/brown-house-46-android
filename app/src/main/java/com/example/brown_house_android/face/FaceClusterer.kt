package com.example.brown_house_android.face

import android.graphics.Bitmap
import android.graphics.Rect

/**
 * 얼굴 클러스터링 - 동일 인물 그룹화
 *
 * 알고리즘:
 * 1. 192차원 얼굴 임베딩을 사용
 * 2. 코사인 유사도 기반 임계값 비교
 * 3. 간단한 그리디 클러스터링
 * 4. 동일 인물로 판단되면 같은 그룹에 추가
 */
class FaceClusterer(
    private val similarityThreshold: Float = DEFAULT_THRESHOLD
) {

    /**
     * 얼굴 데이터 (임베딩 + 메타데이터)
     */
    data class FaceData(
        val embedding: FloatArray,
        val bitmap: Bitmap,
        val rect: Rect,
        val imageIndex: Int,
        val faceIndex: Int
    ) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as FaceData

            if (!embedding.contentEquals(other.embedding)) return false
            if (imageIndex != other.imageIndex) return false
            if (faceIndex != other.faceIndex) return false

            return true
        }

        override fun hashCode(): Int {
            var result = embedding.contentHashCode()
            result = 31 * result + imageIndex
            result = 31 * result + faceIndex
            return result
        }
    }

    /**
     * 클러스터 (사람)
     */
    data class Cluster(
        val id: Int,
        val faces: MutableList<FaceData> = mutableListOf()
    ) {
        /**
         * 클러스터의 평균 임베딩 계산
         */
        fun getAverageEmbedding(): FloatArray {
            if (faces.isEmpty()) return FloatArray(EMBEDDING_SIZE)

            val avgEmbedding = FloatArray(EMBEDDING_SIZE)
            for (face in faces) {
                for (i in face.embedding.indices) {
                    avgEmbedding[i] += face.embedding[i]
                }
            }

            for (i in avgEmbedding.indices) {
                avgEmbedding[i] /= faces.size
            }

            // L2 정규화
            var sum = 0.0
            for (value in avgEmbedding) {
                sum += value * value
            }
            val magnitude = Math.sqrt(sum).toFloat()

            if (magnitude > 0) {
                for (i in avgEmbedding.indices) {
                    avgEmbedding[i] /= magnitude
                }
            }

            return avgEmbedding
        }
    }

    /**
     * 얼굴들을 사람별로 클러스터링
     *
     * @param faceDataList 얼굴 데이터 리스트
     * @return 클러스터 리스트 (Person 0, Person 1, ...)
     */
    fun cluster(faceDataList: List<FaceData>): List<Cluster> {
        if (faceDataList.isEmpty()) return emptyList()

        val clusters = mutableListOf<Cluster>()
        var nextClusterId = 0

        for (faceData in faceDataList) {
            // 기존 클러스터와 비교
            var assignedCluster: Cluster? = null
            var maxSimilarity = -1.0f

            for (cluster in clusters) {
                val avgEmbedding = cluster.getAverageEmbedding()
                val similarity = cosineSimilarity(faceData.embedding, avgEmbedding)

                if (similarity > maxSimilarity && similarity >= similarityThreshold) {
                    maxSimilarity = similarity
                    assignedCluster = cluster
                }
            }

            if (assignedCluster != null) {
                // 기존 클러스터에 추가
                assignedCluster.faces.add(faceData)
            } else {
                // 새 클러스터 생성
                val newCluster = Cluster(id = nextClusterId++)
                newCluster.faces.add(faceData)
                clusters.add(newCluster)
            }
        }

        // 얼굴 개수가 많은 순으로 정렬
        return clusters.sortedByDescending { it.faces.size }
    }

    /**
     * 두 임베딩 간의 코사인 유사도 계산
     */
    private fun cosineSimilarity(embedding1: FloatArray, embedding2: FloatArray): Float {
        if (embedding1.size != embedding2.size) return 0.0f

        var dotProduct = 0.0f
        for (i in embedding1.indices) {
            dotProduct += embedding1[i] * embedding2[i]
        }
        return dotProduct
    }

    /**
     * 클러스터링 결과 요약
     */
    data class ClusteringSummary(
        val totalFaces: Int,
        val totalPeople: Int,
        val clusters: List<ClusterInfo>
    )

    /**
     * 개별 클러스터 정보
     */
    data class ClusterInfo(
        val personId: Int,
        val faceCount: Int,
        val imageIndices: List<Int>,
        val representativeFace: Bitmap
    )

    /**
     * 클러스터링 결과를 요약 형태로 변환
     */
    fun summarize(clusters: List<Cluster>): ClusteringSummary {
        val clusterInfos = clusters.mapIndexed { index, cluster ->
            ClusterInfo(
                personId = index,
                faceCount = cluster.faces.size,
                imageIndices = cluster.faces.map { it.imageIndex }.distinct().sorted(),
                representativeFace = cluster.faces.first().bitmap
            )
        }

        return ClusteringSummary(
            totalFaces = clusters.sumOf { it.faces.size },
            totalPeople = clusters.size,
            clusters = clusterInfos
        )
    }

    companion object {
        /**
         * 임베딩 벡터 차원 (MobileFaceNet 출력)
         */
        const val EMBEDDING_SIZE = 192

        /**
         * 기본 임계값: 0.6
         * - 0.5 ~ 0.7: 일반적인 얼굴 인식 임계값
         * - 높을수록 엄격 (같은 사람만 그룹화)
         * - 낮을수록 관대 (다른 사람도 그룹화될 수 있음)
         */
        const val DEFAULT_THRESHOLD = 0.6f

        /**
         * 엄격한 임계값 (false positive 최소화)
         */
        const val STRICT_THRESHOLD = 0.7f

        /**
         * 관대한 임계값 (false negative 최소화)
         */
        const val LENIENT_THRESHOLD = 0.5f
    }
}
