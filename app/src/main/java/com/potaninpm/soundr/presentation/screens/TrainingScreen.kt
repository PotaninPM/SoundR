package com.potaninpm.soundr.presentation.screens

import android.net.Uri
import android.widget.VideoView
import androidx.annotation.RawRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.potaninpm.soundr.R
import com.potaninpm.soundr.domain.model.ExerciseInfo
import com.potaninpm.soundr.presentation.navigation.RootNavDestinations
import com.potaninpm.soundr.presentation.viewModel.TrainingViewModel

@Composable
fun TrainingScreen(
    navController: NavController? = null,
    viewModel: TrainingViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .windowInsetsPadding(WindowInsets.systemBars.only(WindowInsetsSides.Vertical))
    ) {
        when {
            uiState.isLoading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            uiState.error != null -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Error: ${uiState.error}",
                        color = Color.Red
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { navController?.navigateUp() }) {
                        Text("Go Back")
                    }
                }
            }
            uiState.isCompleted -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Training Complete!",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { viewModel.resetTraining() }) {
                        Text("Start Again")
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedButton(onClick = { navController?.navigate(RootNavDestinations.Home) }) {
                        Text("Return Home")
                    }
                }
            }
            else -> {
                // Show exercise content
                TrainingScreenContent(
                    exercise = uiState.currentExercise,
                    onNextClick = { viewModel.nextExercise() },
                    onPrevClick = { viewModel.prevExercise() },
                    onSkipClick = { viewModel.skipExercise() },
                    exerciseIndex = uiState.exercises.indexOfFirst { it.id == uiState.currentExercise?.id },
                    totalExercises = uiState.exercises.size
                )
            }
        }
    }
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
private fun TrainingScreenContent(
    exercise: ExerciseInfo?,
    onNextClick: () -> Unit,
    onPrevClick: () -> Unit,
    onSkipClick: () -> Unit,
    exerciseIndex: Int,
    totalExercises: Int
) {
    if (exercise == null) return

    val videoResId = when (exercise.videoId) {
        "video1" -> R.raw.video1
        else -> R.raw.video1
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Exercise ${exerciseIndex + 1} of $totalExercises",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Video
        TrainingVideo(
            modifier = Modifier
                .height(250.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp)),
            videoId = videoResId
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Exercise details
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = exercise.name,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "Repeat ${exercise.timesToDo} times",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = exercise.description,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Start
                )
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Navigation buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = onPrevClick,
                enabled = exerciseIndex > 0,
                modifier = Modifier.weight(1f)
            ) {
                Text("Back")
            }
            
            Spacer(modifier = Modifier.padding(4.dp))
            
            OutlinedButton(
                onClick = onSkipClick,
                modifier = Modifier.weight(1f)
            ) {
                Text("Skip")
            }
            
            Spacer(modifier = Modifier.padding(4.dp))
            
            Button(
                onClick = onNextClick,
                modifier = Modifier.weight(1f)
            ) {
                Text("Next")
            }
        }
    }
}

@Preview
@Composable
private fun TrainingScreenDarkPreview() {
    MaterialTheme(
        colorScheme = darkColorScheme()
    ) {
        TrainingScreenContent(
            exercise = ExerciseInfo(
                id = 1,
                name = "Приседания",
                description = "Приседания – базовое упражнение...",
                videoId = "video1",
                timesToDo = 3
            ),
            onNextClick = {},
            onPrevClick = {},
            onSkipClick = {},
            exerciseIndex = 0,
            totalExercises = 3
        )
    }
}

@Preview
@Composable
private fun TrainingScreenLightPreview() {
    MaterialTheme(
        colorScheme = lightColorScheme()
    ) {
        TrainingScreenContent(
            exercise = ExerciseInfo(
                id = 1,
                name = "Приседания",
                description = "Приседания – базовое упражнение...",
                videoId = "video1",
                timesToDo = 3
            ),
            onNextClick = {},
            onPrevClick = {},
            onSkipClick = {},
            exerciseIndex = 0,
            totalExercises = 3
        )
    }
}

