package com.potaninpm.soundr.presentation.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.potaninpm.soundr.domain.model.TrainingInfo
import com.potaninpm.soundr.presentation.viewModel.TrainingsViewModel
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrainingInfoScreen(
    navController: NavController,
    trainingId: Int,
    trainingsViewModel: TrainingsViewModel = hiltViewModel(),
) {
    val completedTraining = trainingsViewModel.loadTrainingById(trainingId).collectAsState(initial = null).value

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Информация о тренировке",
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Назад",
                        )
                    }
                }
            )
        },
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) { innerPadding ->
        if (completedTraining != null) {
            TrainingInfoScreenContent(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(16.dp),
                completedTraining = completedTraining
            )
        } else {
            Log.i("INFOG", "TrainingInfoScreen: completedTraining is null")
        }
    }
}

@Composable
private fun TrainingInfoScreenContent(
    modifier: Modifier = Modifier,
    completedTraining: TrainingInfo
) {
    val dateFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.getDefault())
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm", Locale.getDefault())

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        TopInfo(completedTraining, dateFormatter, timeFormatter)

        Spacer(Modifier.height(24.dp))

        Text("Упражнения", style = MaterialTheme.typography.titleSmall)

        Spacer(Modifier.height(8.dp))

        OutlinedCard(
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
        ) {
            Column {
                completedTraining.allExercisesId.forEach { exId ->
                    val done = exId in completedTraining.madeExercisesId
                    ExerciseListItem(id = exId, done = done)
                    HorizontalDivider()
                }
            }
        }
    }
}

@Composable
private fun TopInfo(
    completedTraining: TrainingInfo,
    dateFormatter: DateTimeFormatter?,
    timeFormatter: DateTimeFormatter
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = completedTraining.date.format(dateFormatter),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Начало", style = MaterialTheme.typography.labelMedium)
                    Text(
                        completedTraining.timeStart
                            .toLocalTimeString(timeFormatter),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                Column {
                    Text("Конец", style = MaterialTheme.typography.labelMedium)
                    Text(
                        completedTraining.timeEnd
                            .toLocalTimeString(timeFormatter),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                Column {
                    Text("Длительность", style = MaterialTheme.typography.labelMedium)
                    Text(
                        completedTraining.duration
                            .toDurationString(),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}

@Composable
private fun ExerciseListItem(id: Long, done: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = if (done) Icons.Default.CheckCircle else Icons.Default.Info,
            contentDescription = null,
            tint = if (done) MaterialTheme.colorScheme.primary else Color.Gray
        )
        Spacer(Modifier.width(12.dp))
        Text(
            text = "Упражнение ${id + 1}",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

private fun Long.toLocalTimeString(fmt: DateTimeFormatter): String =
    Instant.ofEpochMilli(this)
        .atZone(ZoneId.systemDefault())
        .toLocalTime()
        .format(fmt)

private fun Long.toDurationString(): String {
    val totalSec = this / 1000
    val min = totalSec / 60
    val sec = totalSec % 60
    return "%d:%02d".format(min, sec)
}

