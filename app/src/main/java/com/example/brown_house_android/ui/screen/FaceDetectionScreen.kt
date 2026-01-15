package com.example.brown_house_android.ui.screen

import android.Manifest
import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.brown_house_android.data.repository.ExportRepository
import com.example.brown_house_android.model.FaceClusteringUiState
import com.example.brown_house_android.util.FileShareUtil
import com.example.brown_house_android.util.PermissionUtil
import com.example.brown_house_android.viewmodel.FaceClusteringViewModel

@Composable
fun FaceDetectionScreen(
    viewModel: FaceClusteringViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val exportRepository = remember { ExportRepository(context) }

    // ViewModel 상태 구독
    val uiState by viewModel.uiState.collectAsState()

    // UI 전용 상태 (ViewModel과 무관)
    var selectedImagesCount by remember { mutableIntStateOf(0) }
    var exportFilePath by remember { mutableStateOf<String?>(null) }
    var hasPermission by remember {
        mutableStateOf(PermissionUtil.checkStoragePermission(context))
    }

    // Photo Picker 런처
    val pickMultipleMedia = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(maxItems = 20)
    ) { uris: List<Uri> ->
        if (uris.isNotEmpty()) {
            selectedImagesCount = uris.size
            viewModel.processImages(context, uris)
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

        Button(
            onClick = {
                if (hasPermission || Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    launchPhotoPicker(pickMultipleMedia)
                } else {
                    permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                }
            },
            enabled = uiState !is FaceClusteringUiState.Processing
        ) {
            Text(
                when (uiState) {
                    is FaceClusteringUiState.Processing -> "처리 중..."
                    else -> "갤러리에서 사진 선택"
                }
            )
        }

        if (selectedImagesCount > 0) {
            Text("선택된 사진: ${selectedImagesCount}장")
        }

        // 상태별 UI 렌더링
        when (val state = uiState) {
            is FaceClusteringUiState.Idle -> {
                // 초기 상태 - 아무것도 표시하지 않음
            }

            is FaceClusteringUiState.Processing -> {
                CircularProgressIndicator()
                Text(
                    text = state.message,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "${state.current}/${state.total}",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            is FaceClusteringUiState.Success -> {
                val summary = state.summary

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
                            exportFilePath = exportRepository.exportAsCSV(summary)
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("CSV 내보내기")
                    }
                    Button(
                        onClick = {
                            exportFilePath = exportRepository.exportAsJSON(summary)
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
                            FileShareUtil.shareFile(context, path)
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

            is FaceClusteringUiState.Error -> {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "오류 발생",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.error
                        )
                        Text(
                            text = state.message,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Button(
                            onClick = { viewModel.reset() }
                        ) {
                            Text("다시 시도")
                        }
                    }
                }
            }
        }
    }
}

private fun launchPhotoPicker(
    launcher: androidx.activity.result.ActivityResultLauncher<PickVisualMediaRequest>
) {
    launcher.launch(
        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
    )
}
