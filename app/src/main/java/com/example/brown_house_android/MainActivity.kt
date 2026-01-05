package com.example.brown_house_android

import android.Manifest
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
import androidx.lifecycle.lifecycleScope
import com.example.brown_house_android.face.FaceDetector
import com.example.brown_house_android.ui.theme.Brown_house_androidTheme
import com.example.brown_house_android.util.ImageLoader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {

    private val faceDetector = FaceDetector()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
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
    }

    @Composable
    fun FaceDetectionScreen(modifier: Modifier = Modifier) {
        // UI 상태 관리
        var selectedImagesCount by remember { mutableIntStateOf(0) }
        var isProcessing by remember { mutableStateOf(false) }
        var results by remember { mutableStateOf<List<String>>(emptyList()) }
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
                processImages(uris) { resultList ->
                    results = resultList
                    isProcessing = false
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
                text = "얼굴 검출 앱",
                style = MaterialTheme.typography.headlineMedium
            )

            Button(
                onClick = {
                    if (hasPermission || Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        // Android 13+ 또는 권한 있음
                        isProcessing = true
                        launchPhotoPicker(pickMultipleMedia)
                    } else {
                        // Android 12 이하 권한 요청
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
            }

            // 결과 표시
            if (results.isNotEmpty()) {
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
                    results.forEach { result ->
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
            // Android 13+ Photo Picker는 권한 불필요
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

    private fun processImages(
        uris: List<Uri>,
        onComplete: (List<String>) -> Unit
    ) {
        lifecycleScope.launch {
            val resultList = mutableListOf<String>()

            withContext(Dispatchers.IO) {
                uris.forEachIndexed { index, uri ->
                    try {
                        // 1. URI → Bitmap 변환
                        val bitmap = ImageLoader.loadBitmapFromUri(
                            context = this@MainActivity,
                            uri = uri
                        )

                        if (bitmap != null) {
                            // 2. 얼굴 검출
                            val faceCount = faceDetector.detectFaces(bitmap)

                            // 3. 로그 출력
                            val message = "사진 ${index + 1}: 얼굴 ${faceCount}개 발견"
                            Log.d("FaceDetection", message)
                            resultList.add(message)

                            // 4. Bitmap 메모리 해제
                            bitmap.recycle()
                        } else {
                            val errorMsg = "사진 ${index + 1}: 로드 실패"
                            Log.e("FaceDetection", errorMsg)
                            resultList.add(errorMsg)
                        }
                    } catch (e: Exception) {
                        val errorMsg = "사진 ${index + 1}: 오류 - ${e.message}"
                        Log.e("FaceDetection", errorMsg, e)
                        resultList.add(errorMsg)
                    }
                }
            }

            // UI 스레드에서 결과 업데이트
            withContext(Dispatchers.Main) {
                onComplete(resultList)
            }
        }
    }
}
