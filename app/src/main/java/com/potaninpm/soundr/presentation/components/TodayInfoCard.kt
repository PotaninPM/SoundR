package com.potaninpm.soundr.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.potaninpm.soundr.R
import com.potaninpm.soundr.data.local.entities.NotificationReminder
import com.potaninpm.soundr.domain.model.NotificationDayInfo
import com.potaninpm.soundr.domain.model.TrainingInfo
import java.time.LocalDate

@Composable
fun TodayInfoCard(
    showTrainings: Boolean = true,
    onShowTrainingsChange: (Boolean) -> Unit = {},
    trainings: List<TrainingInfo>,
    onStartTrainingClick: () -> Unit = {}
) {
    val date = LocalDate.now()

    val showTrainingsPainter = if (showTrainings) {
        painterResource(R.drawable.visibility_24px)
    } else {
        painterResource(R.drawable.visibility_off_24px)
    }

    OutlinedCard(
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 4.dp)
            .clickable {
                onShowTrainingsChange(!showTrainings)
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(horizontal = 16.dp, vertical = 16.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Column {
                    Text(
                        "Today",
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "${date.month.name} ${date.dayOfMonth}, ${date.year}",
                        color = Color.Gray,
                    )
                }

                Icon(
                    painter = showTrainingsPainter,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .size(27.dp)
                        .clickable {
                            onShowTrainingsChange(!showTrainings)
                        },
                )
            }

            if (trainings.isNotEmpty()) {
                if (showTrainings) {
                    Spacer(modifier = Modifier.padding(4.dp))

                    trainings.forEach { training ->
                        TrainingView(training = training)
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.Gray.copy(alpha = 0.2f))
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Тренировки скрыты",
                            color = Color.Gray,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                Spacer(modifier = Modifier.padding(6.dp))

                TrainButton(
                    text = "Train",
                    onClick = {
                        onStartTrainingClick()
                    }
                )
            } else {

                Spacer(modifier = Modifier.padding(6.dp))

                TrainButton(
                    text = "Train",
                    onClick = {
                        onStartTrainingClick()
                    }
                )

                Spacer(modifier = Modifier.padding(6.dp))

                Text(
                    text = "Сегодня вы не тренировались",
                    color = Color.Gray,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}

@Composable
fun NotificationsInfo(
    showNotifications: Boolean = true,
    reminders: List<NotificationReminder>,
    onConfirmTime: (NotificationReminder) -> Unit,
    onUpdateReminder: (NotificationReminder) -> Unit,
    onDeleteReminder: (NotificationReminder) -> Unit,
    onShowNotificationsChange: (Boolean) -> Unit
) {
    var showNewTimePicker by remember { mutableStateOf(false) }
    var reminderForEdit by remember { mutableStateOf<NotificationReminder?>(null) }

    val showNotificationsPainter = if (showNotifications) {
        painterResource(R.drawable.visibility_24px)
    } else {
        painterResource(R.drawable.visibility_off_24px)
    }

    if (showNewTimePicker) {
        NotificationTimeDialog(
            onConfirm = { time ->
                onConfirmTime(
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

//    reminderForEdit?.let { reminder ->
//        NotificationTimeDialog(
//            onConfirm = { time ->
//                viewModel.updateReminder(
//                    reminder.copy(hour = time.hour, minute = time.minute)
//                )
//                reminderForEdit = null
//            },
//            onDismiss = { reminderForEdit = null }
//        )
//    }

    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
            .padding(bottom = 12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        onClick = {
            onShowNotificationsChange(!showNotifications)
        },
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(horizontal = 16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Уведомления о тренировках",
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.titleMedium
                )

                Icon(
                    painter = showNotificationsPainter,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .size(27.dp)
                        .clickable {
                            onShowNotificationsChange(!showNotifications)
                        }
                )
            }

            if (showNotifications) {
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
                    Column(
                        modifier = Modifier
                            .padding(vertical = 4.dp)
                    ) {
                        reminders.forEach { reminder ->
                            NotificationItem(
                                reminder = reminder,
                                onUpdateReminder = { newReminder ->
                                    onUpdateReminder(newReminder)
                                },
                                onDeleteReminder = { reminderToDelete ->
                                    onDeleteReminder(reminderToDelete)
                                },
                                onCardClick = {

                                }
                            )
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
    }
}

@Composable
fun NotificationItem(
    reminder: NotificationReminder,
    onUpdateReminder: (NotificationReminder) -> Unit = {},
    onDeleteReminder: (NotificationReminder) -> Unit = {},
    onCardClick: (NotificationReminder) -> Unit = {}
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        onClick = {
            onCardClick(reminder)
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "${reminder.hour}:${reminder.minute}",
                style = MaterialTheme.typography.headlineLarge,
                color = if (reminder.enabled) MaterialTheme.colorScheme.onSurface else Color.Gray,
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                NotificationsDays(
                    listOf(
                        NotificationDayInfo(1, true),
                        NotificationDayInfo(2, false),
                        NotificationDayInfo(3, false),
                        NotificationDayInfo(4, true),
                        NotificationDayInfo(5, true),
                        NotificationDayInfo(6, false),
                        NotificationDayInfo(7, true)
                    )
                )

                Spacer(modifier = Modifier.width(16.dp))

                Switch(
                    checked = reminder.enabled,
                    onCheckedChange = { isChecked ->
                        onUpdateReminder(reminder.copy(enabled = isChecked))
                    },
                    colors = SwitchDefaults.colors(
                        uncheckedThumbColor = Color.Gray
                    )
                )
            }

           /* IconButton(
                onClick = {
                    onDeleteReminder(reminder)
                }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.edit_24px),
                    contentDescription = "Удалить уведомление",
                    tint = MaterialTheme.colorScheme.error
                )
            }*/
        }
    }
}

@Composable
fun NotificationsDays(
    days: List<NotificationDayInfo>
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        days.forEach { day ->
            DayStatusText(day.dayNumber, day.isActive)
        }
    }
}

@Composable
fun DayStatusText(
    day: Int,
    isActive: Boolean
) {
    val daysList = listOf("M", "T", "W", "T", "M", "S", "S")

    val textColor: Color = if (isActive) {
        MaterialTheme.colorScheme.primary
    } else {
        Color.Gray
    }

    val textWeight = if (isActive) {
        FontWeight.Bold
    } else {
        FontWeight.Normal
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextCircle(isActive)

        Text(
            text = daysList[day - 1],
            color = textColor,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = textWeight,
        )
    }
}

@Composable
fun TextCircle(
    isActive: Boolean
) {
    val color = if (isActive) {
        MaterialTheme.colorScheme.primary
    } else {
        Color.Transparent
    }

    Box(
        modifier = Modifier
            .clip(CircleShape)
            .background(color)
            .size(5.dp)
    )
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

