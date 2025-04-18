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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
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
    navController: NavController,
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
                ErrorWithTraining(
                    onGoBackClick = {
                        navController.navigateUp()
                    },
                    error = uiState.error.toString()
                )
            }
            uiState.isCompleted -> {
                TrainingCompleted(
                    onResetTrainingClick = {
                        viewModel.resetTraining()
                    },
                    onBackHomeClick = {
                        navController.navigate(RootNavDestinations.Home)
                    }
                )
            }
            else -> {
                TrainingScreenContent(
                    exercise = uiState.currentExercise,
                    onNextClick = { viewModel.nextExercise() },
                    onPrevClick = { viewModel.prevExercise() },
                    onSkipClick = { viewModel.skipExercise() },
                    exerciseIndex = uiState.exercises.indexOfFirst { it.id == uiState.currentExercise?.id },
                    totalExercises = uiState.exercises.size,
                    videoResId = R.raw.video1
                )
            }
        }
    }
}

@Composable
fun ErrorWithTraining(
    error: String,
    onGoBackClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Error: $error",
            color = Color.Red
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                onGoBackClick()
            }
        ) {
            Text("Go Back")
        }
    }
}

@Composable
fun TrainingCompleted(
    onResetTrainingClick: () -> Unit,
    onBackHomeClick: () -> Unit
) {
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

        Button(
            onClick = {
                onResetTrainingClick()
            }
        ) {
            Text("Start Again")
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton(
            onClick = {
                onBackHomeClick()
            }
        ) {
            Text("Return Home")
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TrainingScreenContent(
    videoResId: Int,
    exercise: ExerciseInfo?,
    onNextClick: () -> Unit,
    onPrevClick: () -> Unit,
    onSkipClick: () -> Unit,
    exerciseIndex: Int,
    totalExercises: Int
) {
    if (exercise == null) return

    // Состояние для BottomSheetScaffold
    val sheetState = rememberStandardBottomSheetState(
        initialValue = SheetValue.Expanded // свёрнуто по умолчанию
    )
    val scaffoldState = rememberBottomSheetScaffoldState(bottomSheetState = sheetState)

    // Определяем динамическую высоту видео в зависимости от прогресса bottom sheet:
    // Если лист свёрнут (Collapsed) – видео занимает больше места,
    // Если развёрнут (Expanded) – видео занимает меньше места.
    // Здесь просто выбираем два фиксированных значения для примера.
    val expandedVideoHeight = 300.dp
    val collapsedVideoHeight = 450.dp

    val videoHeight by remember {
        derivedStateOf {
            if (sheetState.currentValue == SheetValue.Expanded) {
                expandedVideoHeight
            } else {
                collapsedVideoHeight
            }
        }
    }

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetPeekHeight = 80.dp,  // минимальная высота нижнего листа (например, оставляет кнопки видимыми)
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        sheetContent = {
            // Здесь располагаем информацию, которая показывается в BottomSheet при развёрнутом состоянии.
            // Если лист свёрнут, можно показывать только подсказку или кнопки.
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                if (sheetState.currentValue == SheetValue.Expanded) {
                    Text(
                        text = exercise.name,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = exercise.description,
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .windowInsetsPadding(WindowInsets.systemBars.only(WindowInsetsSides.Vertical)),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        OutlinedButton(
                            onClick = onPrevClick,
                            enabled = exerciseIndex > 0,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Back")
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = onNextClick,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Next")
                        }
                    }
                } else {
                    // При свёрнутом состоянии отображаем подсказку
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .windowInsetsPadding(WindowInsets.systemBars.only(WindowInsetsSides.Vertical)),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        OutlinedButton(
                            onClick = onPrevClick,
                            enabled = exerciseIndex > 0,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Back")
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = onNextClick,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Next")
                        }
                    }
                }
            }
        },
        content = { innerPadding ->
            // Основной контент – видео и кнопки управления
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Информационный заголовок (опционально)
                Text(
                    text = "Exercise ${exerciseIndex + 1} of $totalExercises",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
                Spacer(modifier = Modifier.height(16.dp))
                // Видео. Его высота меняется в зависимости от состояния BottomSheet.
                TrainingVideo(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(videoHeight)
                        .clip(RoundedCornerShape(16.dp)),
                    videoId = videoResId
                )
                Spacer(modifier = Modifier.height(16.dp))
                // Кнопки управления. Они всегда видны под видео.
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    OutlinedButton(
                        onClick = onPrevClick,
                        enabled = exerciseIndex > 0,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Back")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = onNextClick,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Next")
                    }
                }
            }
        }
    )
}

/*
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
*/

