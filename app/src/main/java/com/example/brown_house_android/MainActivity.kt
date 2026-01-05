package com.example.brown_house_android

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import com.example.brown_house_android.face.FaceClusterer
import com.example.brown_house_android.face.FaceCropper
import com.example.brown_house_android.face.FaceDetector
import com.example.brown_house_android.face.FaceEmbedder
import com.example.brown_house_android.ui.theme.Brown_house_androidTheme
import com.example.brown_house_android.util.ImageLoader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class MainActivity : ComponentActivity() {

    private val faceDetector = FaceDetector()
    private var faceEmbedder: FaceEmbedder? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // FaceEmbedder 초기화 (모델 파일 확인)
        try {
            if (FaceEmbedder.isModelAvailable(this)) {
                faceEmbedder = FaceEmbedder(this)
                Log.d("FaceEmbedder", "모델 로드 성공")
            } else {
                Log.w("FaceEmbedder", "모델 파일이 없습니다. 클러스터링 기능이 비활성화됩니다.")
            }
        } catch (e: Exception) {
            Log.e("FaceEmbedder", "모델 로드 실패", e)
        }

        setContent {
            Brown_house_androidTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    FaceDetectionScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        faceDetector.close()
        faceEmbedder?.close()
    }

    @Composable
    fun FaceDetectionScreen(modifier: Modifier = Modifier) {
        // UI 상태 관리
        var selectedImagesCount by remember { mutableIntStateOf(0) }
        var isProcessing by remember { mutableStateOf(false) }
        var currentProgress by remember { mutableStateOf("") }
        var clusteringSummary by remember { mutableStateOf<FaceClusterer.ClusteringSummary?>(null) }
        var detectionResults by remember { mutableStateOf<List<String>>(emptyList()) }
        var exportFilePath by remember { mutableStateOf<String?>(null) }
        var hasPermission by remember {
            mutableStateOf(checkStoragePermission())
        }

        // Photo Picker 런처
        val pickMultipleMedia = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickMultipleVisualMedia(maxItems = 20)
        ) { uris: List<Uri> ->
            if (uris.isNotEmpty()) {
                selectedImagesCount = uris.size
                isProcessing = true
                currentProgress = "처리 시작..."
                processImagesWithClustering(uris) { summary, results ->
                    clusteringSummary = summary
                    detectionResults = results
                    isProcessing = false
                    currentProgress = ""
                }
            }
        }

        // 권한 요청 런처 (API 26-32용)
        val permissionLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            hasPermission = isGranted
            if (isGranted) {
                launchPhotoPicker(pickMultipleMedia)
            }
        }

        // UI 렌더링
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "얼굴 검출 및 분류 앱",
                style = MaterialTheme.typography.headlineMedium
            )

            // 모델 상태 표시
            if (faceEmbedder == null) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Text(
                        text = "⚠️ MobileFaceNet 모델 없음\n얼굴 개수만 검출됩니다.",
                        modifier = Modifier.padding(8.dp),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            Button(
                onClick = {
                    if (hasPermission || Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        launchPhotoPicker(pickMultipleMedia)
                    } else {
                        permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                    }
                },
                enabled = !isProcessing
            ) {
                Text(if (isProcessing) "처리 중..." else "갤러리에서 사진 선택")
            }

            if (selectedImagesCount > 0) {
                Text("선택된 사진: ${selectedImagesCount}장")
            }

            if (isProcessing) {
                CircularProgressIndicator()
                if (currentProgress.isNotEmpty()) {
                    Text(
                        text = currentProgress,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            // 클러스터링 결과 요약
            clusteringSummary?.let { summary ->
                HorizontalDivider()
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "분석 완료",
                            style = MaterialTheme.typography.titleLarge
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("총 얼굴: ${summary.totalFaces}개")
                        Text("인물 수: ${summary.totalPeople}명")
                    }
                }

                // 내보내기 버튼
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {
                            exportFilePath = exportResultsAsCSV(summary)
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("CSV 내보내기")
                    }
                    Button(
                        onClick = {
                            exportFilePath = exportResultsAsJSON(summary)
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("JSON 내보내기")
                    }
                }

                // 내보내기 결과 표시
                exportFilePath?.let { path ->
                    Text(
                        text = "저장됨: $path",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Button(
                        onClick = {
                            shareFile(path)
                        }
                    ) {
                        Text("파일 공유")
                    }
                }

                // 클러스터별 상세 정보
                Text(
                    text = "인물별 상세",
                    style = MaterialTheme.typography.titleMedium
                )
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                ) {
                    summary.clusters.forEach { cluster ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = "Person ${cluster.personId}",
                                    style = MaterialTheme.typography.titleSmall
                                )
                                Text("얼굴 수: ${cluster.faceCount}개")
                                Text("사진 번호: ${cluster.imageIndices.joinToString(", ")}")
                            }
                        }
                    }
                }
            }

            // 검출 결과 (간단한 로그)
            if (detectionResults.isNotEmpty() && clusteringSummary == null) {
                HorizontalDivider()
                Text(
                    text = "검출 결과",
                    style = MaterialTheme.typography.titleMedium
                )
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                ) {
                    detectionResults.forEach { result ->
                        Text(
                            text = result,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }
                }
            }
        }
    }

    private fun checkStoragePermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            true
        } else {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun launchPhotoPicker(
        launcher: androidx.activity.result.ActivityResultLauncher<PickVisualMediaRequest>
    ) {
        launcher.launch(
            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
        )
    }

    private fun processImagesWithClustering(
        uris: List<Uri>,
        onComplete: (FaceClusterer.ClusteringSummary?, List<String>) -> Unit
    ) {
        lifecycleScope.launch {
            val resultList = mutableListOf<String>()
            val allFaceData = mutableListOf<FaceClusterer.FaceData>()

            withContext(Dispatchers.IO) {
                uris.forEachIndexed { imageIndex, uri ->
                    try {
                        // 진행률 업데이트
                        withContext(Dispatchers.Main) {
                            // UI 업데이트는 여기서
                        }

                        // 1. URI → Bitmap 변환
                        val bitmap = ImageLoader.loadBitmapFromUri(
                            context = this@MainActivity,
                            uri = uri
                        )

                        if (bitmap != null) {
                            // 2. 얼굴 검출 (상세 정보 포함)
                            val faces = faceDetector.detectFacesWithDetails(bitmap)
                            val faceCount = faces.size

                            val message = "사진 ${imageIndex + 1}: 얼굴 ${faceCount}개 발견"
                            Log.d("FaceDetection", message)
                            resultList.add(message)

                            // 3. FaceEmbedder가 있으면 임베딩 생성
                            if (faceEmbedder != null && faces.isNotEmpty()) {
                                faces.forEachIndexed { faceIndex, face ->
                                    // 얼굴 잘라내기
                                    val croppedFace = FaceCropper.cropFace(bitmap, face)

                                    if (croppedFace != null) {
                                        // 임베딩 생성
                                        val embedding = faceEmbedder?.getEmbedding(croppedFace)

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
                        resultList.add(errorMsg)
                    }
                }

                // 4. 클러스터링
                var summary: FaceClusterer.ClusteringSummary? = null
                if (allFaceData.isNotEmpty()) {
                    val clusterer = FaceClusterer()
                    val clusters = clusterer.cluster(allFaceData)
                    summary = clusterer.summarize(clusters)

                    Log.d("FaceClustering", "총 ${summary.totalFaces}개 얼굴, ${summary.totalPeople}명 인물")
                }

                // UI 스레드에서 결과 업데이트
                withContext(Dispatchers.Main) {
                    onComplete(summary, resultList)
                }
            }
        }
    }
}
