package com.potaninpm.soundr.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.time.LocalDate

@Composable
fun TodayInfoCard(
    date: LocalDate
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 5.dp
        )
    ) {
        Column {
            Text(
                "Today",
                modifier = Modifier.padding(16.dp),
                fontWeight = MaterialTheme.typography.bodyLarge.fontWeight,
            )

            Text(
                text = "${date.month.name} ${date.dayOfMonth}, ${date.year}",
                modifier = Modifier.padding(16.dp),
                fontWeight = MaterialTheme.typography.bodyMedium.fontWeight,
            )

            TrainingInfo()

            HorizontalDivider()

            Text(
                text = "Уведомления о тренировках",
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                fontWeight = MaterialTheme.typography.bodyMedium.fontWeight,
            )
        }
    }
}

@Composable
fun TrainingInfo() {

}

@Preview
@Composable
fun TodayInfoCardScreenDarkPreview() {
    MaterialTheme(
        colorScheme = darkColorScheme()
    ) {
        TodayInfoCard(
            LocalDate.now()
        )
    }
}

@Preview
@Composable
fun TodayInfoCardScreenLightPreview() {
    MaterialTheme(
        colorScheme = lightColorScheme()
    ) {
        TodayInfoCard(
            LocalDate.now()
        )
    }
}

