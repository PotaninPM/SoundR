package com.potaninpm.soundr.presentation.screens

import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.potaninpm.soundr.presentation.components.UpperStatsPart
import com.potaninpm.soundr.presentation.viewModel.ProfileViewModel
import com.potaninpm.soundr.presentation.viewModel.TrainingsViewModel

@Composable
fun ProfileScreen(
    trainingsViewModel: TrainingsViewModel = hiltViewModel(),
    profileViewModel: ProfileViewModel = hiltViewModel(),
) {
    val totalTrainingsTime by trainingsViewModel.totalTime.collectAsState()
    val totalCompletedExercises by trainingsViewModel.totalCompletedExercises.collectAsState()
    val userName by profileViewModel.userName.collectAsState()
    val hasProfileImage by profileViewModel.hasProfileImage.collectAsState()

    val totalProgress = totalCompletedExercises / 100.0f

    ProfileScreenContent(
        totalTrainingsTime = totalTrainingsTime,
        totalCompletedExercises = totalCompletedExercises,
        totalProgress = totalProgress,
        userName = userName,
        hasProfileImage = hasProfileImage,
        profileViewModel = profileViewModel
    )
}

@Composable
private fun ProfileScreenContent(
    totalTrainingsTime: Long,
    totalCompletedExercises: Long,
    totalProgress: Float,
    userName: String,
    hasProfileImage: Boolean,
    profileViewModel: ProfileViewModel
) {
    val bitmap = remember { mutableStateOf<Bitmap?>(null) }

    if (hasProfileImage && bitmap.value == null) {
        bitmap.value = profileViewModel.getProfileImageBitmap()
    }

    var avatarUri by rememberSaveable { mutableStateOf<Uri?>(null) }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) {
        uri: Uri? -> avatarUri = uri
    }

    val onImageClick: () -> Unit = {
        imagePickerLauncher.launch("image/*")
    }

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
                bitmap = bitmap.value,
                onClick = {
                    onImageClick()
                }
            )

            Spacer(modifier = Modifier.size(16.dp))

            UserStats(
                name = userName,
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
    bitmap: Bitmap?,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(150.dp)
            .clip(CircleShape)
            .border(
                width = 4.dp,
                color = Color(0xFF4CAF50),
                shape = CircleShape
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        bitmap?.let {
            Image(
                bitmap = it.asImageBitmap(),
                contentDescription = "Profile picture",
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        } ?: run {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add profile picture",
                    modifier = Modifier.size(40.dp)
                )
            }
        }
    }
}