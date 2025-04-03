package com.potaninpm.soundr.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.potaninpm.soundr.presentation.components.CalendarView
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun CalendarScreen() {
    CalendarScreenContent()
}

@Composable
private fun CalendarScreenContent() {
    val scrollState = rememberScrollState()
    var selectedDate by remember { mutableStateOf<LocalDate>(LocalDate.of(2021, 9, 2)) }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.systemBars.only(WindowInsetsSides.Top))
                .scrollable(scrollState, orientation = Orientation.Vertical)
        ) {
            CalendarView(
                onDateSelected = { date ->
                    selectedDate = date
                }
            )

            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
            )

            SelectedDayInfo(
                date = selectedDate
            )
        }
    }
}

@Composable
private fun SelectedDayInfo(date: LocalDate) {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Text(
            text = "Selected Date:",
            style = MaterialTheme.typography.titleMedium
        )
        
        Text(
            text = date.format(DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy")),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

@Preview
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
}

