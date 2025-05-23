package com.potaninpm.soundr.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.potaninpm.soundr.R
import com.potaninpm.soundr.domain.model.TrainingInfo
import com.potaninpm.soundr.presentation.components.CalendarView
import com.potaninpm.soundr.presentation.components.TrainingView
import com.potaninpm.soundr.presentation.navigation.RootNavDestinations
import com.potaninpm.soundr.presentation.viewModel.TrainingsViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun CalendarScreen(
    viewModel: TrainingsViewModel = hiltViewModel(),
    navController: NavController
) {
    val trainingsByDate by viewModel.dayTrainings.collectAsState()

    CalendarScreenContent(
        trainingsByDate = trainingsByDate,
        onDateSelected = { newDate ->
            viewModel.loadTrainingsByDate(newDate)
        },
        onCompletedTrainingClick = { training ->
            navController.navigate("${RootNavDestinations.TrainingInfo}/${training.id}")
        }
    )
}

@Composable
private fun CalendarScreenContent(
    trainingsByDate: List<TrainingInfo>,
    onDateSelected: (LocalDate) -> Unit = {},
    onCompletedTrainingClick: (TrainingInfo) -> Unit
) {
    var selectedDate by remember { mutableStateOf<LocalDate>(LocalDate.now()) }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .verticalScroll(rememberScrollState())
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
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
                trainings = trainingsByDate,
                onCompletedTrainingClick = { training ->
                    onCompletedTrainingClick(training)
                }
            )
        }
    }
}

@Composable
private fun SelectedDayInfo(
    date: LocalDate,
    trainings: List<TrainingInfo>,
    onCompletedTrainingClick: (TrainingInfo) -> Unit
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxSize(),
    ) {
        Text(
            text = stringResource(R.string.selected_date, date.format(DateTimeFormatter.ofPattern("MMMM d, yyyy"))),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
        )

        if (trainings.isNotEmpty()) {
            trainings.forEach { training ->
                OutlinedCard(
                    onClick = {
                        onCompletedTrainingClick(training)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    shape = RoundedCornerShape(12.dp),
                ) {
                    TrainingView(training)
                }
            }
        } else {
            Text(
                text = stringResource(R.string.no_trainings_for_day),
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Normal,
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 16.dp)
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

