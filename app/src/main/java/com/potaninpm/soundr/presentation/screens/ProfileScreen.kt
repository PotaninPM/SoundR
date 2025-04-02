package com.potaninpm.soundr.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun ProfileScreen() {
    ProfileScreenContent()
}

@Composable
private fun ProfileScreenContent() {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp)
    ) {

    }
}

@Preview
@Composable
private fun ProfileScreenDarkPreview() {
    MaterialTheme(
        colorScheme = darkColorScheme()
    ) {
        ProfileScreenContent()
    }
}

@Preview
@Composable
private fun ProfileScreenLightPreview() {
    MaterialTheme(
        colorScheme = lightColorScheme()
    ) {
        ProfileScreenContent()
    }
}

