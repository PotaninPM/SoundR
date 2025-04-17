package com.potaninpm.soundr.presentation.screens

import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.potaninpm.soundr.domain.model.UserInfo
import com.potaninpm.soundr.presentation.components.NotificationsInfo
import com.potaninpm.soundr.presentation.components.TodayInfoCard
import com.potaninpm.soundr.presentation.components.TrainingsStatsCard
import com.potaninpm.soundr.presentation.navigation.RootNavDestinations
import com.potaninpm.soundr.presentation.viewModel.NotificationViewModel
import com.potaninpm.soundr.presentation.viewModel.TrainingsViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    faceDown: Boolean
) {
    val scope = rememberCoroutineScope()
    var selectedTab by remember { mutableIntStateOf(0) }
    val pagerState = rememberPagerState(
        pageCount = { 3 }
    )

    val allHeaders = listOf(
        "Home",
        "Calendar",
        "Profile"
    )



    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }
            .collect { page ->
                selectedTab = page
            }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .windowInsetsPadding(WindowInsets.systemBars.only(WindowInsetsSides.Top))
                    ) {
                        item {
                            Spacer(modifier = Modifier.width(8.dp))
                        }

                        items(allHeaders) { header ->
                            val index = allHeaders.indexOf(header)

                            Text(
                                text = header,
                                modifier = Modifier
                                    .clickable {
                                        selectedTab = index
                                        scope.launch { pagerState.animateScrollToPage(index) }
                                    },
                                style = if (selectedTab == index) {
                                    MaterialTheme.typography.titleLarge.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                } else {
                                    MaterialTheme.typography.titleLarge.copy(
                                        fontWeight = FontWeight.Normal,
                                        fontSize = MaterialTheme.typography.titleMedium.fontSize,
                                        color = Color.Gray
                                    )
                                }
                            )
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) { page ->
            when (page) {
                0 -> HomeScreenContent(
                    navController = navController,
                    onCardClick = {
                        selectedTab = 1
                        scope.launch {
                            pagerState.animateScrollToPage(selectedTab)
                        }
                    },
                    faceDown = faceDown
                )
                1 -> CalendarScreen()
                2 -> ProfileScreen()
            }
        }
    }
}

@Composable
private fun HomeScreenContent(
    navController: NavController,
    onCardClick: () -> Unit = {},
    faceDown: Boolean,
    notificationViewModel: NotificationViewModel = hiltViewModel(),
    trainingsViewModel: TrainingsViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    var counter by remember { mutableIntStateOf(0) }

    val prefs = remember { context.getSharedPreferences("soundr", Context.MODE_PRIVATE) }

    var showNotifications by remember {
        mutableStateOf(prefs.getBoolean("show_notifications", true))
    }

    var showTrainings by remember {
        mutableStateOf(prefs.getBoolean("show_trainings", true))
    }

    var wasFaceDown by remember { mutableStateOf(false) }

    LaunchedEffect(faceDown) {
        if (faceDown) {
            wasFaceDown = true
        } else if (wasFaceDown) {
            showNotifications = !showNotifications
            showTrainings = !showTrainings

            prefs.edit()
                .putBoolean("show_notifications", showNotifications)
                .putBoolean("show_trainings", showTrainings)
                .apply()

            wasFaceDown = false
        }
    }

    val reminders by notificationViewModel.reminders.collectAsState()
    val todayTrainings by trainingsViewModel.todayTrainings.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .verticalScroll(rememberScrollState()),
    ) {
        TrainingsStatsCard(
            userInfo = UserInfo(
                name = "John Doe",
                streak = 5,
                bestStreak = 10,
                totalTrainings = 20,
                totalTrainingsTime = 300,
                progress = 0.75f
            ),
            onClick = {
                onCardClick()
            }
        )

        Spacer(modifier = Modifier.padding(8.dp))

        TodayInfoCard(
            trainings = todayTrainings,
            onStartTrainingClick = {
                navController.navigate(RootNavDestinations.Training)
            },
            showTrainings = showTrainings,
            onShowTrainingsChange = { newValue ->
                prefs.edit().putBoolean("show_trainings", newValue).apply()
                showTrainings = newValue
            }
        )

        Spacer(modifier = Modifier.padding(8.dp))

        NotificationsInfo(
            reminders = reminders,
            onConfirmTime = { newReminder ->
                notificationViewModel.insertReminder(newReminder)
            },
            onUpdateReminder = { newReminder ->
                notificationViewModel.updateReminder(newReminder)
            },
            onDeleteReminder = { reminderToDelete ->
                notificationViewModel.deleteReminder(reminderToDelete)
            },
            showNotifications = showNotifications,
            onShowNotificationsChange = { newValue ->
                prefs.edit().putBoolean("show_notifications", newValue).apply()
                showNotifications = newValue
            }
        )
    }
}