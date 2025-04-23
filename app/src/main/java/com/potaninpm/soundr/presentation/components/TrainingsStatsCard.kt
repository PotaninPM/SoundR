package com.potaninpm.soundr.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.potaninpm.soundr.domain.model.UserInfo
import Python.time.LocalDate

@Composable
fun TrainingsStatsCard(
    userInfo: UserInfo,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
            .padding(top = 5.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        ),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally)
            ) {
                UpperStatsPart(
                    header = userInfo.totalTrainingsTime.toString(),
                    description = "Minutes"
                )

                VerticalDivider(
                    modifier = Modifier
                        .height(50.dp)
                        .align(Alignment.CenterVertically),
                    color = Color.LightGray
                )

                UpperStatsPart(
                    progress = userInfo.progress
                )

                VerticalDivider(
                    modifier = Modifier
                        .height(50.dp)
                        .align(Alignment.CenterVertically),
                    color = Color.LightGray
                )

                UpperStatsPart(
                    header = userInfo.totalTrainings.toString(),
                    description = "Trainings"
                )
            }

            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),

            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "This week",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "History",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF00A67E)
                        )
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = null,
                            tint = Color(0xFF00A67E),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                WeekDaysRowAuto()

                /*Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = null,
                            tint = Color(0xFFFF5722),
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "2",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.width(4.dp))

                        Text(
                            text = "Day Streak",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }

                    Text(
                        text = "Personal Best: 4",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                }*/
            }
        }
    }
}

@Composable
fun UpperStatsPart(
    progress: Float = -1f,
    header: String = "",
    description: String = ""
) {
    if (progress == -1f) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = header,
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                fontSize = 32.sp
            )

            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
        }
    } else {
        CustomProgressBar(
            progress = progress
        )
    }
}

@Composable
private fun WeekDaysRowAuto() {
    val today = LocalDate.now()

    val offset = today.dayOfWeek.value.toLong() - 1
    val monday = today.minusDays(offset)
    val weekDays = List(7) { dayIndex -> monday.plusDays(dayIndex.toLong()) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        weekDays.forEach { date ->
            DayWithNumber(date = date)
        }
    }
}

@Composable
private fun DayWithNumber(
    date: LocalDate
) {
    val today = LocalDate.now()

    val dayLabel = date.dayOfWeek.name.first().toString()
    val dayNumber = date.dayOfMonth.toString()
    val isToday = date == today

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = dayLabel,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(4.dp))

        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(if (isToday) Color(17, 129, 217, 255) else Color.Transparent),
            contentAlignment = Alignment.Center
        ) {
            if (dayNumber.isNotEmpty()) {
                Text(
                    text = dayNumber,
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (isToday) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.onSurface,
                    fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal
                )
            }
        }
    }
}
