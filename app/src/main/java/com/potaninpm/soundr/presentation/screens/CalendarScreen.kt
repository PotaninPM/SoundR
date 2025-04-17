package com.potaninpm.soundr.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.potaninpm.soundr.data.local.entities.CompletedTraining
import com.potaninpm.soundr.domain.model.TrainingInfo
import com.potaninpm.soundr.presentation.components.CalendarView
import com.potaninpm.soundr.presentation.viewModel.TrainingsViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun CalendarScreen(
    viewModel: TrainingsViewModel = hiltViewModel(),
) {
    val trainingsByDate by viewModel.dayTrainings.collectAsState()

    CalendarScreenContent(
        trainingsByDate = trainingsByDate,
        onDateSelected = { newDate ->
            viewModel.loadTrainingsByDate(newDate)
        }
    )
}

@Composable
private fun CalendarScreenContent(
    trainingsByDate: List<TrainingInfo>,
    onDateSelected: (LocalDate) -> Unit = {},
) {
    val scrollState = rememberScrollState()
    var selectedDate by remember { mutableStateOf<LocalDate>(LocalDate.now()) }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .scrollable(scrollState, orientation = Orientation.Vertical)
        ) {
            CalendarView(
                onDateSelected = { date ->
                    onDateSelected(date)
                    selectedDate = date
                }
            )

            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
            )

            SelectedDayInfo(
                date = selectedDate,
                trainings = trainingsByDate
            )
        }
    }
}

@Composable
private fun SelectedDayInfo(
    date: LocalDate,
    trainings: List<TrainingInfo>
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = "Выбрано: ${date.format(DateTimeFormatter.ofPattern("MMMM d, yyyy"))}",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
        )

        if (trainings.isNotEmpty()) {

            trainings.forEach { training ->
                Text(
                    text = training.id.toString(),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                )
            }
        } else {
            Text(
                text = "Нет тренировок на этот день",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Normal,
            )
        }
    }
}

/*@Preview
@Composable
private fun CalendarScreenDarkPreview() {
    MaterialTheme(
        colorScheme = darkColorScheme()
    ) {
        CalendarScreenContent()
    }
}

@Preview
@Composable
private fun CalendarScreenLightPreview() {
    MaterialTheme(
        colorScheme = lightColorScheme()
    ) {
        CalendarScreenContent()
    }
}*/

