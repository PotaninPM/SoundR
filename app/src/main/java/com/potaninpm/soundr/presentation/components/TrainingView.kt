package com.potaninpm.soundr.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.potaninpm.soundr.R
import com.potaninpm.soundr.domain.model.TrainingInfo
import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun TrainingView(
    training: TrainingInfo
) {
    val startTime = formatTimestamp(training.timeStart)
    val endTime = formatTimestamp(training.timeEnd)

    val durationMinutes = Duration.ofMillis(training.duration).toMinutes()
    Row(
        modifier = Modifier
            .padding(vertical = 16.dp, horizontal = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CustomProgressBar(
            progress = training.progress / 100f
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = "Тренировка №${training.id}",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            TimeSection(
                startTime = startTime,
                endTime = endTime,
                durationMinutes = durationMinutes
            )

            Spacer(modifier = Modifier.height(8.dp))

            DoneTrainingsInfo(training)
        }
    }

}

@Composable
private fun DoneTrainingsInfo(training: TrainingInfo) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(R.drawable.inventory_24px),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(20.dp)
        )

        Spacer(modifier = Modifier.width(6.dp))

        Text(
            text = "Done: ${training.madeExercisesId.size} / ${training.allExercisesId.size} exercises",
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Composable
private fun TimeSection(
    startTime: String,
    endTime: String,
    durationMinutes: Long
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row {
            Icon(
                painter = painterResource(R.drawable.schedule_24px),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )

            Spacer(modifier = Modifier.width(4.dp))

            Text(
                text = "$startTime - $endTime",
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Text(
            text = "$durationMinutes min",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold
        )
    }
}

private fun formatTimestamp(timestamp: Long): String {
    val dateTime = LocalDateTime.ofInstant(
        Instant.ofEpochMilli(timestamp),
        ZoneId.systemDefault()
    )
    return dateTime.format(DateTimeFormatter.ofPattern("HH:mm"))
}

@Preview
@Composable
fun TrainingViewPreview() {
    MaterialTheme(
        colorScheme = lightColorScheme()
    ) {
        Surface {
            TrainingView(
                training = TrainingInfo(
                    id = 1,
                    date = LocalDate.now(),
                    progress = 75,
                    timeStart = System.currentTimeMillis() - 3600000, // 1 hour ago
                    timeEnd = System.currentTimeMillis(),
                    duration = 3600000,
                    allExercisesId = listOf(1, 2, 3, 4),
                    madeExercisesId = listOf(1, 2, 3)
                )
            )
        }
    }
}

@Preview
@Composable
fun TrainingViewDarkPreview() {
    MaterialTheme(
        colorScheme = darkColorScheme()
    ) {
        Surface {
            TrainingView(
                training = TrainingInfo(
                    id = 1,
                    date = LocalDate.now(),
                    progress = 75,
                    timeStart = System.currentTimeMillis() - 3600000, // 1 hour ago
                    timeEnd = System.currentTimeMillis(),
                    duration = 3600000,
                    allExercisesId = listOf(1, 2, 3, 4),
                    madeExercisesId = listOf(1, 2, 3)
                )
            )
        }
    }
}