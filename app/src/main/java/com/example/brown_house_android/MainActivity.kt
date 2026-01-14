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

class MainActivity : ComponentActivity() {

    private val viewModel: FaceClusteringViewModel by viewModels {
        FaceClusteringViewModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            Brown_house_androidTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    FaceDetectionScreen(
                        viewModel = viewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}
