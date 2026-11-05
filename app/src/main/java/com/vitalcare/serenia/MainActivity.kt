package com.vitalcare.serenia

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import com.vitalcare.serenia.core.designsystems.theme.SereniaTheme
import com.vitalcare.serenia.main.MainScreen

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SereniaTheme {
                MainScreen()
            }
        }
    }
}
