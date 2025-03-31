package com.potaninpm.soundr

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.potaninpm.soundr.presentation.navigation.RootNavigation
import com.potaninpm.soundr.presentation.theme.SoundRTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SoundRTheme {
                RootNavigation()
            }
        }
    }
}