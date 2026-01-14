package com.example.brown_house_android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import com.example.brown_house_android.ui.screen.FaceDetectionScreen
import com.example.brown_house_android.ui.theme.Brown_house_androidTheme
import com.example.brown_house_android.viewmodel.FaceClusteringViewModel
import com.example.brown_house_android.viewmodel.FaceClusteringViewModelFactory
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import com.example.brown_house_android.face.FaceClusterer
import com.example.brown_house_android.face.FaceCropper
import com.example.brown_house_android.face.FaceDetector
import com.example.brown_house_android.face.FaceEmbedder
import com.example.brown_house_android.socialtree.core.designsystem.theme.SocialTreeTheme
import com.example.brown_house_android.socialtree.core.navigation.SocialTreeNavHost
import com.example.brown_house_android.util.ImageLoader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            SocialTreeTheme {
                SocialTreeNavHost()
            }
        }
    }
}
