package com.potaninpm.soundr.presentation.screens

import android.net.Uri
import android.widget.VideoView
import androidx.annotation.RawRes
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollable
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.potaninpm.soundr.domain.model.ExerciseInfo
import com.potaninpm.soundr.domain.model.TrainingInfo
import com.potaninpm.soundr.presentation.components.SuccessfullyTraining
import com.potaninpm.soundr.presentation.components.UpperStatsPart
import com.potaninpm.soundr.presentation.navigation.RootNavDestinations
import com.potaninpm.soundr.presentation.viewModel.TrainingViewModel

@Composable
fun TrainingScreen(
    navController: NavController,
    viewModel: TrainingViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val completedTraining by viewModel.completedTrainingInfo.collectAsState()

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
                    completedTraining = completedTraining,
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
    completedTraining: TrainingInfo,
    onResetTrainingClick: () -> Unit,
    onBackHomeClick: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SuccessfullyTraining()

        Spacer(modifier = Modifier.height(16.dp))

        TrainingInfoStatCard(
            points = 10,
            totalTime = completedTraining.duration / 1000 / 60,
            totalExercises = completedTraining.madeExercisesId.size,
            onResetTrainingClick = {
                onResetTrainingClick()
            },
            onBackHomeClick = {
                onBackHomeClick()
            }
        )

    }
}

@Composable
fun TrainingInfoStatCard(
    points: Int,
    totalTime: Long,
    totalExercises: Int,
    onResetTrainingClick: () -> Unit,
    onBackHomeClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxSize(),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(16.dp)
        ) {
            UpperStatsPart(
                header = totalTime.toString(),
                description = "Exercise time"
            )

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(12.dp))

            UpperStatsPart(
                header = totalExercises.toString(),
                description = "Total exercises"
            )

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(12.dp))

            UpperStatsPart(
                header = "+$points \uD83C\uDFC6",
                description = "Points reward"
            )

            Spacer(modifier = Modifier.height(12.dp))

            Spacer(modifier = Modifier.weight(1f))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    onClick = {
                        onResetTrainingClick()
                    }
                ) {
                    Text("Start Again")
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    onClick = {
                        onBackHomeClick()
                    }
                ) {
                    Text("Return Home")
                }
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

    val videoView = remember(videoId) {
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
        modifier = modifier,
        update = { view ->
            if (view.tag != videoId) {
                view.stopPlayback()
                val videoUri = Uri.parse("android.resource://${context.packageName}/$videoId")
                view.setVideoURI(videoUri)
                view.start()
                view.tag = videoId
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TrainingScreenContent(
    exercise: ExerciseInfo?,
    onNextClick: () -> Unit,
    onPrevClick: () -> Unit,
    onSkipClick: () -> Unit,
    exerciseIndex: Int,
    totalExercises: Int
) {
    val context = LocalContext.current

    if (exercise == null) return

    val sheetState = rememberStandardBottomSheetState(
        initialValue = SheetValue.Expanded,
        confirmValueChange = {
            false
        }
    )

    val scaffoldState = rememberBottomSheetScaffoldState(bottomSheetState = sheetState)

    val expandedVideoHeight = 450.dp
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
        sheetPeekHeight = 80.dp,
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        sheetContent = {
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
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "Exercise ${exerciseIndex + 1} of $totalExercises",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
                Spacer(modifier = Modifier.height(16.dp))

                TrainingVideo(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(videoHeight)
                        .clip(RoundedCornerShape(16.dp)),
                    videoId = context.resources.getIdentifier(exercise.videoId, "raw", context.packageName)
                )
            }
        }
    )
}

