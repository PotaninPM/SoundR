package com.potaninpm.soundr.presentation.screens

import android.net.Uri
import android.widget.VideoView
import androidx.annotation.RawRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.potaninpm.soundr.R

@Composable
fun TrainingScreen() {

    TrainingScreenContent()
}

@Composable
fun TrainingVideo(
    modifier: Modifier = Modifier,
    @RawRes videoId: Int
) {
    val context = LocalContext.current

    val videoView = remember {
        VideoView(context).apply {
            val videoUri = Uri.parse("android.resource://${context.packageName}/$videoId")
            setVideoURI(videoUri)

            setOnPreparedListener { mediaPlayer ->
                mediaPlayer.isLooping = true
            }

            start()
        }
    }

    DisposableEffect(videoId) {
        onDispose {
            videoView.stopPlayback()
        }
    }

    AndroidView(
        factory = {
            videoView
        },
        modifier = modifier
    )
}

@Composable
private fun TrainingScreenContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .windowInsetsPadding(WindowInsets.systemBars.only(WindowInsetsSides.Vertical)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TrainingVideo(
            modifier = Modifier
                .height(500.dp)
                .clip(RoundedCornerShape(16.dp)),
            videoId = R.raw.video1
        )
    }
}

@Preview
@Composable
private fun TrainingScreenDarkPreview() {
    MaterialTheme(
        colorScheme = darkColorScheme()
    ) {
        TrainingScreenContent()
    }
}

@Preview
@Composable
private fun TrainingScreenLightPreview() {
    MaterialTheme(
        colorScheme = lightColorScheme()
    ) {
        TrainingScreenContent()
    }
}

