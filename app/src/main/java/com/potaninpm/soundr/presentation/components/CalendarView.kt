package com.potaninpm.soundr.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.potaninpm.soundr.R
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

@Composable
fun CalendarHeader(
    modifier: Modifier = Modifier,
    currentMonth: YearMonth = YearMonth.now(),
    onPreviousClick: () -> Unit,
    onNextClick: () -> Unit,
    month: String,
    year: String
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onPreviousClick,
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(12.dp))
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                contentDescription = "Previous month"
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = month,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = year,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
        }

        IconButton(
            onClick = onNextClick,
            enabled = currentMonth.plusMonths(1) <= YearMonth.now(),
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(12.dp))
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "Next month"
            )
        }
    }
}

@Composable
fun DaysOfWeekHeader(
    modifier: Modifier = Modifier
) {
    val daysOfWeek = listOf(
        stringResource(R.string.mon),
        stringResource(R.string.tue),
        stringResource(R.string.wed),
        stringResource(R.string.thu),
        stringResource(R.string.fri),
        stringResource(R.string.sat),
        stringResource(R.string.sun)
    )

    Row(
        modifier = modifier.fillMaxWidth()
    ) {
        for (day in daysOfWeek) {
            Box(
                modifier = Modifier
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = day,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
fun CalendarView(
    modifier: Modifier = Modifier,
    initialDate: LocalDate = LocalDate.of(LocalDate.now().year, LocalDate.now().month, LocalDate.now().dayOfMonth),
    onDateSelected: (LocalDate) -> Unit = {}
) {
    var currentMonth by remember { mutableStateOf(YearMonth.from(initialDate)) }
    var selectedDate by remember { mutableStateOf(initialDate) }

    val monthFormatter = DateTimeFormatter.ofPattern("MMMM")
    val yearFormatter = DateTimeFormatter.ofPattern("yyyy")

    val monthText = currentMonth.format(monthFormatter)
    val yearText = currentMonth.format(yearFormatter)

    val firstDayOfMonth = currentMonth.atDay(1)
    val firstDayOffset = (firstDayOfMonth.dayOfWeek.value - 1) % 7

    val firstDateToShow = firstDayOfMonth.minusDays(firstDayOffset.toLong())

    val maxLines = if (firstDayOffset >= 5 && currentMonth.lengthOfMonth() > 30) 6 else 5

    Card(
        modifier = Modifier
            .padding(horizontal = 6.dp)
            .background(MaterialTheme.colorScheme.surface),
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 14.dp, horizontal = 8.dp)
        ) {
            DaysOfWeekHeader()

            Spacer(modifier = Modifier.height(8.dp))

            Column(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
            ) {
                for (week in 0 until maxLines) {
                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        for (day in 0 until 7) {
                            val currentDate = firstDateToShow.plusDays((week * 7 + day).toLong())
                            val isCurrentMonth = currentDate.month == currentMonth.month
                            val isSelected = currentDate.equals(selectedDate)
                            val isToday = currentDate.equals(LocalDate.now())

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(1f)
                                    .padding(6.dp)
                                    .let {
                                        if (isSelected) {
                                            it.background(
                                                color = MaterialTheme.colorScheme.primary,
                                                shape = RoundedCornerShape(12.dp)
                                            )
                                        } else if (isToday) {
                                            it.background(
                                                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                                                shape = RoundedCornerShape(12.dp)
                                            )
                                        } else {
                                            it
                                        }
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = currentDate.dayOfMonth.toString(),
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    textAlign = TextAlign.Center,
                                    color = when {
                                        isSelected -> MaterialTheme.colorScheme.surface
                                        isCurrentMonth -> LocalContentColor.current
                                        else -> Color.Gray.copy(alpha = 0.5f)
                                    },
                                    modifier = Modifier
                                        .padding(8.dp)
                                        .clickable(enabled = true) {
                                            selectedDate = currentDate
                                            onDateSelected(currentDate)
                                        }
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            CalendarHeader(
                currentMonth = currentMonth,
                onPreviousClick = {
                    currentMonth = currentMonth.minusMonths(1)
                },
                onNextClick = {
                    currentMonth = currentMonth.plusMonths(1)
                },
                month = monthText,
                year = yearText
            )
        }
    }
}

@Preview
@Composable
fun CalendarPreview() {
    CalendarView()
}