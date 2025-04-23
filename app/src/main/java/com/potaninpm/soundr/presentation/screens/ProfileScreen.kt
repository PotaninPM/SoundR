package com.potaninpm.soundr.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil3.compose.rememberAsyncImagePainter
import com.potaninpm.soundr.presentation.components.UpperStatsPart
import com.potaninpm.soundr.presentation.viewModel.TrainingsViewModel

@Composable
fun ProfileScreen(
    trainingsViewModel: TrainingsViewModel = hiltViewModel(),
) {
    val totalTrainingsTime by trainingsViewModel.totalTime.collectAsState()
    val totalCompletedExercises by trainingsViewModel.totalCompletedExercises.collectAsState()

    val totalProgress = totalCompletedExercises / 100.0f


    ProfileScreenContent(
        totalTrainingsTime = totalTrainingsTime,
        totalCompletedExercises = totalCompletedExercises,
        totalProgress = totalProgress
    )
}

@Composable
private fun ProfileScreenContent(
    totalTrainingsTime: Long,
    totalCompletedExercises: Long,
    totalProgress: Float
) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            UserImage(
                image = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ29ppdzqXN6nP-msl1kg7C0Ry-YgR49gnMEQ&s",
            )

            Spacer(modifier = Modifier.size(16.dp))

            UserStats(
                name = "John Doe",
                totalTrainings = totalCompletedExercises,
                totalTrainingsTime = totalTrainingsTime,
                progress = totalProgress
            )
        }
    }
}

@Composable
fun UserStats(
    name: String,
    totalTrainings: Long,
    totalTrainingsTime: Long,
    progress: Float
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "Статистика $name",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(top = 12.dp),
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally),
            ) {
                UpperStatsPart(
                    header = totalTrainingsTime.toString(),
                    description = "Minutes"
                )

                VerticalDivider(
                    modifier = Modifier
                        .height(50.dp)
                        .align(Alignment.CenterVertically),
                    color = Color.LightGray
                )

                UpperStatsPart(
                    progress = progress
                )

                VerticalDivider(
                    modifier = Modifier
                        .height(50.dp)
                        .align(Alignment.CenterVertically),
                    color = Color.LightGray
                )

                UpperStatsPart(
                    header = totalTrainings.toString(),
                    description = "Trainings"
                )
            }
        }
    }
}

@Composable
fun UserImage(
    image: String
) {
    Box(
        modifier = Modifier
            .size(150.dp)
            .clip(CircleShape)
            .border(
                width = 4.dp,
                color = Color(0xFF4CAF50),
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = rememberAsyncImagePainter(image),
            contentDescription = null,
            modifier = Modifier
                .size(110.dp)
                .clip(CircleShape),
        )
    }
}