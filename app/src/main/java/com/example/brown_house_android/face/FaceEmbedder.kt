package com.example.brown_house_android.face

import android.content.Context
import android.graphics.Bitmap
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.common.FileUtil
import java.nio.ByteBuffer

/**
 * MobileFaceNet을 사용한 얼굴 임베딩 생성기
 *
 * 요구사항:
 * - assets/MobileFaceNet.tflite 파일 필요
 * - 입력: 배치 크기 2, 112x112x3 RGB 이미지 (정규화: -1 ~ 1)
 * - 출력: 192차원 벡터 (배치당 2개)
 */
class FaceEmbedder(context: Context) {

    private var interpreter: Interpreter? = null

    init {
        try {
            // TFLite 모델 로드
            val modelBuffer = FileUtil.loadMappedFile(context, MODEL_PATH)
            val options = Interpreter.Options().apply {
                setNumThreads(NUM_THREADS)
            }
            interpreter = Interpreter(modelBuffer, options)

            // 모델의 입력/출력 정보 출력 (디버깅용)
            interpreter?.let { interp ->
                val inputTensor = interp.getInputTensor(0)
                val outputTensor = interp.getOutputTensor(0)
                println("=== MobileFaceNet 모델 정보 ===")
                println("입력 shape: ${inputTensor.shape().contentToString()}")
                println("입력 타입: ${inputTensor.dataType()}")
                println("입력 bytes: ${inputTensor.numBytes()}")
                println("출력 shape: ${outputTensor.shape().contentToString()}")
                println("출력 타입: ${outputTensor.dataType()}")
                println("출력 bytes: ${outputTensor.numBytes()}")
                println("============================")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            throw RuntimeException("MobileFaceNet 모델 로드 실패: ${e.message}\n" +
                    "assets/MobileFaceNet.tflite 파일이 있는지 확인하세요.")
        }
    }

    /**
     * 얼굴 이미지를 192차원 임베딩 벡터로 변환
     *
     * @param faceBitmap 112x112 크기의 얼굴 이미지 (FaceCropper 출력)
     * @return 192차원 정규화된 벡터
     */
    fun getEmbedding(faceBitmap: Bitmap): FloatArray? {
        return try {
            // 1. Bitmap이 112x112인지 확인, 아니면 리사이징
            val resizedBitmap = if (faceBitmap.width != INPUT_SIZE || faceBitmap.height != INPUT_SIZE) {
                Bitmap.createScaledBitmap(faceBitmap, INPUT_SIZE, INPUT_SIZE, true)
            } else {
                faceBitmap
            }

            // 2. 입력 버퍼 준비 (float32: 2 x 112 x 112 x 3)
            // 배치 크기가 2이므로 같은 이미지를 2번 넣음
            val inputBuffer = ByteBuffer.allocateDirect(4 * BATCH_SIZE * INPUT_SIZE * INPUT_SIZE * 3)
            inputBuffer.order(java.nio.ByteOrder.nativeOrder())

            // 3. Bitmap 픽셀을 float로 변환하고 정규화
            val intValues = IntArray(INPUT_SIZE * INPUT_SIZE)
            resizedBitmap.getPixels(intValues, 0, INPUT_SIZE, 0, 0, INPUT_SIZE, INPUT_SIZE)

            // 배치 크기만큼 반복 (같은 이미지를 2번)
            repeat(BATCH_SIZE) {
                for (pixelValue in intValues) {
                    // RGB 채널 추출 및 정규화 (0~255 → -1~1)
                    val r = ((pixelValue shr 16 and 0xFF) / 127.5f - 1.0f)
                    val g = ((pixelValue shr 8 and 0xFF) / 127.5f - 1.0f)
                    val b = ((pixelValue and 0xFF) / 127.5f - 1.0f)

                    inputBuffer.putFloat(r)
                    inputBuffer.putFloat(g)
                    inputBuffer.putFloat(b)
                }
            }

            inputBuffer.rewind()

            // 4. 출력 버퍼 준비 (2, 192)
            val outputBuffer = ByteBuffer.allocateDirect(4 * BATCH_SIZE * EMBEDDING_SIZE)
            outputBuffer.order(java.nio.ByteOrder.nativeOrder())

            // 5. 추론 실행
            interpreter?.run(inputBuffer, outputBuffer)

            // 6. 결과를 FloatArray로 변환 (첫 번째 배치 결과만 사용)
            outputBuffer.rewind()
            val embedding = FloatArray(EMBEDDING_SIZE)
            for (i in 0 until EMBEDDING_SIZE) {
                embedding[i] = outputBuffer.float
            }

            // 7. 임시 Bitmap 해제
            if (resizedBitmap != faceBitmap) {
                resizedBitmap.recycle()
            }

            // 8. L2 정규화
            normalizeL2(embedding)

        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * 여러 얼굴의 임베딩을 한 번에 생성 (배치 처리 최적화)
     *
     * @param faceBitmaps 얼굴 이미지 리스트
     * @return 임베딩 벡터 리스트
     */
    fun getEmbeddings(faceBitmaps: List<Bitmap>): List<FloatArray> {
        if (faceBitmaps.isEmpty()) return emptyList()

        // 단일 얼굴인 경우 기존 함수 사용
        if (faceBitmaps.size == 1) {
            return listOfNotNull(getEmbedding(faceBitmaps[0]))
        }

        val results = mutableListOf<FloatArray>()

        try {
            // 배치 크기(2)만큼 묶어서 처리
            for (i in faceBitmaps.indices step BATCH_SIZE) {
                val batch = mutableListOf<Bitmap>()

                // 첫 번째 이미지
                batch.add(faceBitmaps[i])

                // 두 번째 이미지 (없으면 첫 번째 이미지로 패딩)
                batch.add(
                    if (i + 1 < faceBitmaps.size) faceBitmaps[i + 1]
                    else faceBitmaps[i]
                )

                // 배치 처리
                val batchEmbeddings = processBatch(batch[0], batch[1])

                // 결과 저장
                results.add(batchEmbeddings[0])
                if (i + 1 < faceBitmaps.size) {
                    results.add(batchEmbeddings[1])
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return results
    }

    /**
     * 2개의 얼굴을 배치로 처리
     *
     * @param face1 첫 번째 얼굴
     * @param face2 두 번째 얼굴
     * @return 2개의 임베딩 벡터 배열
     */
    private fun processBatch(face1: Bitmap, face2: Bitmap): Array<FloatArray> {
        // 1. 리사이징
        val resized1 = if (face1.width != INPUT_SIZE || face1.height != INPUT_SIZE) {
            Bitmap.createScaledBitmap(face1, INPUT_SIZE, INPUT_SIZE, true)
        } else face1

        val resized2 = if (face2.width != INPUT_SIZE || face2.height != INPUT_SIZE) {
            Bitmap.createScaledBitmap(face2, INPUT_SIZE, INPUT_SIZE, true)
        } else face2

        // 2. 입력 버퍼 준비 (2 x 112 x 112 x 3)
        val inputBuffer = ByteBuffer.allocateDirect(4 * BATCH_SIZE * INPUT_SIZE * INPUT_SIZE * 3)
        inputBuffer.order(java.nio.ByteOrder.nativeOrder())

        // 3. 두 이미지를 버퍼에 추가
        for (bitmap in listOf(resized1, resized2)) {
            val intValues = IntArray(INPUT_SIZE * INPUT_SIZE)
            bitmap.getPixels(intValues, 0, INPUT_SIZE, 0, 0, INPUT_SIZE, INPUT_SIZE)

            for (pixelValue in intValues) {
                val r = ((pixelValue shr 16 and 0xFF) / 127.5f - 1.0f)
                val g = ((pixelValue shr 8 and 0xFF) / 127.5f - 1.0f)
                val b = ((pixelValue and 0xFF) / 127.5f - 1.0f)

                inputBuffer.putFloat(r)
                inputBuffer.putFloat(g)
                inputBuffer.putFloat(b)
            }
        }

        inputBuffer.rewind()

        // 4. 출력 버퍼 준비 (2, 192)
        val outputBuffer = ByteBuffer.allocateDirect(4 * BATCH_SIZE * EMBEDDING_SIZE)
        outputBuffer.order(java.nio.ByteOrder.nativeOrder())

        // 5. 추론 실행
        interpreter?.run(inputBuffer, outputBuffer)

        // 6. 두 개의 임베딩 추출
        outputBuffer.rewind()
        val embedding1 = FloatArray(EMBEDDING_SIZE)
        val embedding2 = FloatArray(EMBEDDING_SIZE)

        for (i in 0 until EMBEDDING_SIZE) {
            embedding1[i] = outputBuffer.float
        }
        for (i in 0 until EMBEDDING_SIZE) {
            embedding2[i] = outputBuffer.float
        }

        // 7. 임시 Bitmap 해제
        if (resized1 != face1) resized1.recycle()
        if (resized2 != face2) resized2.recycle()

        // 8. L2 정규화
        return arrayOf(
            normalizeL2(embedding1),
            normalizeL2(embedding2)
        )
    }

    /**
     * L2 정규화 (벡터 길이를 1로 만듦)
     * 코사인 유사도 계산 시 내적만으로 계산 가능
     */
    private fun normalizeL2(embedding: FloatArray): FloatArray {
        var sum = 0.0
        for (value in embedding) {
            sum += value * value
        }
        val magnitude = Math.sqrt(sum).toFloat()

        if (magnitude > 0) {
            for (i in embedding.indices) {
                embedding[i] /= magnitude
            }
        }
        return embedding
    }

    /**
     * 두 임베딩 벡터 간의 코사인 유사도 계산
     * L2 정규화된 벡터의 경우 내적과 동일
     *
     * @param embedding1 첫 번째 임베딩
     * @param embedding2 두 번째 임베딩
     * @return 유사도 (-1.0 ~ 1.0, 높을수록 유사)
     */
    fun cosineSimilarity(embedding1: FloatArray, embedding2: FloatArray): Float {
        if (embedding1.size != embedding2.size) {
            throw IllegalArgumentException("임베딩 크기가 일치하지 않습니다")
        }

        var dotProduct = 0.0f
        for (i in embedding1.indices) {
            dotProduct += embedding1[i] * embedding2[i]
        }
        return dotProduct
    }

    /**
     * 유클리드 거리 계산
     *
     * @param embedding1 첫 번째 임베딩
     * @param embedding2 두 번째 임베딩
     * @return 거리 (0에 가까울수록 유사)
     */
    fun euclideanDistance(embedding1: FloatArray, embedding2: FloatArray): Float {
        if (embedding1.size != embedding2.size) {
            throw IllegalArgumentException("임베딩 크기가 일치하지 않습니다")
        }

        var sum = 0.0f
        for (i in embedding1.indices) {
            val diff = embedding1[i] - embedding2[i]
            sum += diff * diff
        }
        return Math.sqrt(sum.toDouble()).toFloat()
    }

    /**
     * 리소스 해제
     */
    fun close() {
        interpreter?.close()
        interpreter = null
    }

    companion object {
        private const val MODEL_PATH = "MobileFaceNet.tflite"
        private const val INPUT_SIZE = 112
        private const val BATCH_SIZE = 2  // 모델의 배치 크기
        private const val EMBEDDING_SIZE = 192  // 실제 출력 차원
        private const val NUM_THREADS = 4

        /**
         * 모델 파일 존재 여부 확인
         */
        fun isModelAvailable(context: Context): Boolean {
            return try {
                context.assets.open(MODEL_PATH).use { true }
            } catch (e: Exception) {
                false
            }
        }
    }

    /**
     * 얼굴 임베딩과 메타데이터를 담는 데이터 클래스
     */
    data class FaceEmbeddingData(
        val embedding: FloatArray,
        val faceIndex: Int,
        val imageIndex: Int
    ) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as FaceEmbeddingData

            if (!embedding.contentEquals(other.embedding)) return false
            if (faceIndex != other.faceIndex) return false
            if (imageIndex != other.imageIndex) return false

            return true
        }

        override fun hashCode(): Int {
            var result = embedding.contentHashCode()
            result = 31 * result + faceIndex
            result = 31 * result + imageIndex
            return result
        }
    }
}
