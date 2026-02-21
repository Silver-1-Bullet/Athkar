package com.istighfar.counter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.istighfar.counter.ui.screens.HomeScreen
import com.istighfar.counter.ui.theme.AthkarTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AthkarTheme {
                HomeScreen()
            }
        }
    }
}
