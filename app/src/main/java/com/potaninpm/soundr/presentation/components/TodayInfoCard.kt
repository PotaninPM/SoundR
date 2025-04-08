package com.potaninpm.soundr.presentation.components

import android.content.Context
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.potaninpm.soundr.R
import com.potaninpm.soundr.data.local.entities.NotificationReminder
import com.potaninpm.soundr.domain.model.TrainingInfo
import com.potaninpm.soundr.presentation.viewModel.NotificationViewModel
import java.time.LocalDate

@Composable
fun TodayInfoCard(
    trainings: List<TrainingInfo>
) {
    val date = LocalDate.now()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 5.dp
        )
    ) {
        Column {
            TrainingsInfo(
                date = date,
                trainings = trainings
            )

            HorizontalDivider()

            NotificationsInfo(

            )
        }
    }
}

@Composable
fun NotificationsInfo(
    viewModel: NotificationViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    val reminders by viewModel.reminders.collectAsState()

    var showNewTimePicker by remember { mutableStateOf(false) }
    var reminderForEdit by remember { mutableStateOf<NotificationReminder?>(null) }

    if (showNewTimePicker) {
        NotificationTimeDialog(
            onConfirm = { time ->
                viewModel.insertReminder(
                    NotificationReminder(
                        hour = time.hour,
                        minute = time.minute,
                        enabled = true
                    )
                )
                showNewTimePicker = false
            },
            onDismiss = {
                showNewTimePicker = false
            }
        )
    }

    reminderForEdit?.let { reminder ->
        NotificationTimeDialog(
            onConfirm = { time ->
                viewModel.updateReminder(
                    reminder.copy(hour = time.hour, minute = time.minute)
                )
                reminderForEdit = null
            },
            onDismiss = { reminderForEdit = null }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = "Уведомления о тренировках",
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth(),
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.titleMedium
        )

        if (reminders.isEmpty()) {
            Text(
                text = "Включите уведомления, чтобы получать напоминания о тренировках каждый день",
                color = Color.Gray,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(vertical = 24.dp),
                style = MaterialTheme.typography.bodyMedium
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                items(reminders) { reminder ->
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .clickable { reminderForEdit = reminder }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Каждый день в ${reminder.hour}:${reminder.minute}",
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.weight(1f)
                            )
                            Switch(
                                checked = reminder.enabled,
                                onCheckedChange = { isChecked ->
                                    viewModel.updateReminder(reminder.copy(enabled = isChecked))
                                },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = MaterialTheme.colorScheme.primary,
                                    uncheckedThumbColor = Color.Gray
                                )
                            )
                            IconButton(onClick = { viewModel.deleteReminder(reminder) }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.edit_24px),
                                    contentDescription = "Удалить уведомление",
                                    tint = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    }
                }
            }
        }

        TrainButton(
            onClick = { showNewTimePicker = true },
            text = "Добавить уведомления",
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            shape = 12,
            color = MaterialTheme.colorScheme.primary
        )
    }
}


@Composable
fun TrainingsInfo(
    date: LocalDate,
    trainings: List<TrainingInfo>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            "Today",
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "${date.month.name} ${date.dayOfMonth}, ${date.year}",
            color = Color.Gray,
        )

        Spacer(modifier = Modifier.padding(6.dp))

        TrainButton(
            text = "Train",
            onClick = {

            }
        )

        Spacer(modifier = Modifier.padding(6.dp))

        if (trainings.isNotEmpty()) {
            trainings.forEach {
                TrainingView()
                Text(
                    text = "${it.timeStart} - ${it.timeEnd} min",
                    color = Color.Gray,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                )
            }
        } else {
            Text(
                text = "Сегодня вы не тренировались",
                color = Color.Gray,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            )
        }
    }
}


@Preview
@Composable
fun TodayInfoCardScreenDarkPreview() {
    MaterialTheme(
        colorScheme = darkColorScheme()
    ) {
//        TodayInfoCard()
    }
}

@Preview
@Composable
fun TodayInfoCardScreenLightPreview() {
    MaterialTheme(
        colorScheme = lightColorScheme()
    ) {
        //TodayInfoCard()
    }
}

