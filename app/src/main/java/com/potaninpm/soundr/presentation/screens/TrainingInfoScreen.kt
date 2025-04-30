package com.potaninpm.soundr.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.potaninpm.soundr.data.local.entities.CompletedTraining
import com.potaninpm.soundr.domain.model.TrainingInfo
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrainingInfoScreen(
    navController: NavController,
    completedTraining: TrainingInfo,
    onBackClick: () -> Unit = {}
) {
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
        TrainingInfoScreenContent(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp),
            completedTraining = completedTraining
        )
    }
}

@Composable
private fun TrainingInfoScreenContent(
    modifier: Modifier,
    completedTraining: TrainingInfo
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            TopInfoBar(
                trainingNumber = completedTraining.id.toInt()
            )
        }

        item {
            CompletedExerciseInfo(
                training = completedTraining
            )
        }
    }
}

@Composable
fun TopInfoBar(
    trainingNumber: Int
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Всего тренировок",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = trainingNumber.toString(),
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun CompletedExerciseInfo(
    training: TrainingInfo
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Тренировка #${training.id}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            Text(
                text = "Дата: ${formatDate(training.date)}",
                style = MaterialTheme.typography.bodyMedium
            )
            
            Text(
                text = "Длительность: ${training.duration} мин",
                style = MaterialTheme.typography.bodyMedium
            )
            
            Text(
                text = "Упражнений выполнено: ${training.madeExercisesId.size}",
                style = MaterialTheme.typography.bodyMedium
            )
            
          /*  Text(
                text = "Точность: ${String.format("%.1f", training.)}%",
                style = MaterialTheme.typography.bodyMedium
            )*/
        }
    }
}

private fun formatDate(date: LocalDate): String {
    val formatter = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
    return formatter.format(date)
}

