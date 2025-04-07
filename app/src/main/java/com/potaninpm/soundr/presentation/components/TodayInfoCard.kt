package com.potaninpm.soundr.presentation.components

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.potaninpm.soundr.domain.model.TrainingInfo
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

            NotificationsInfo()
        }
    }
}

@Composable
fun NotificationsInfo() {
    val context = LocalContext.current
    val sharedPrefs = context.getSharedPreferences("soundr", Context.MODE_PRIVATE)

    val notificationTimeInMinutes = sharedPrefs.getInt("notification_in", -1)

    val hours = notificationTimeInMinutes / 60
    val minutes = notificationTimeInMinutes % 60

    var timePicker by remember { mutableStateOf(false) }

    val onChangeClick = {
        sharedPrefs.edit().putInt("notification_in", -1).apply()
    }

    val onConfigureClick = {
        timePicker = true
    }

    if (timePicker) {
        NotificationTimeDialog(
            onConfirm = { time ->
                sharedPrefs.edit().putInt("notification_in", time.hour * 60 + time.minute).apply()
            },
            onDismiss = {
                timePicker = false
            }
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
        )


        if (notificationTimeInMinutes == -1) {
            Text(
                text = "Включите уведомления чтобы получать напоминания о тренировках каждый день",
                color = Color.Gray,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally),
                fontSize = MaterialTheme.typography.bodyMedium.fontSize
            )

            TrainButton(
                onClick = onConfigureClick,
                text = "Настроить уведомления",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                shape = 12,
                color = MaterialTheme.colorScheme.primary
            )
        } else {
            Row(
                modifier = Modifier
                    .padding(vertical = 16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .height(50.dp)
                        .weight(1f)
                        .drawBehind {
                            val strokeWidth = 2.dp.toPx()
                            val dashWidth = 10.dp.toPx()
                            val gapWidth = 6.dp.toPx()

                            drawRoundRect(
                                color = Color.Gray,
                                size = size,
                                cornerRadius = CornerRadius(8.dp.toPx()),
                                style = Stroke(
                                    width = strokeWidth,
                                    pathEffect = PathEffect.dashPathEffect(
                                        floatArrayOf(
                                            dashWidth,
                                            gapWidth
                                        ), 0f
                                    )
                                )
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Каждый день в $hours:$minutes часов",
                        fontWeight = FontWeight.Normal,
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.Gray
                    )
                }

                /*Spacer(modifier = Modifier.width(16.dp))

                Icon(
                    painter = painterResource(R.drawable.edit_24px),
                    contentDescription = null,
                    modifier = Modifier
                        .clickable { onChangeClick() }
                        .size(24.dp)
                        .align(Alignment.CenterVertically)
                )*/
            }
        }
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

