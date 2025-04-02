package com.potaninpm.soundr.presentation.screens

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
import androidx.navigation.NavController
import com.potaninpm.soundr.presentation.components.CalendarView
import java.time.LocalDate

@Composable
fun HomeScreen(
    navController: NavController
) {
    HomeScreenContent()
}

@Composable
private fun HomeScreenContent() {
    var selectedDate by remember { mutableStateOf<LocalDate>(LocalDate.now()) }

    val scrollState = rememberScrollState()

    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = MaterialTheme.colorScheme.background
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
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            SelectedDayInfo(
                date = selectedDate
            )
        }
    }
}

@Composable
fun SelectedDayInfo(
    date: LocalDate
) {

}

@Preview
@Composable
private fun HomeScreenDarkPreview() {
    MaterialTheme(
        colorScheme = darkColorScheme()
    ) {
        HomeScreenContent()
    }
}

@Preview
@Composable
private fun HomeScreenLightPreview() {
    MaterialTheme(
        colorScheme = lightColorScheme()
    ) {
        HomeScreenContent()
    }
}

