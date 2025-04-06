package com.potaninpm.soundr.presentation.screens

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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.potaninpm.soundr.domain.model.TrainingInfo
import com.potaninpm.soundr.domain.model.UserInfo
import com.potaninpm.soundr.presentation.components.TodayInfoCard
import com.potaninpm.soundr.presentation.components.TrainingsStatsCard
import com.potaninpm.soundr.presentation.navigation.RootNavDestinations
import kotlinx.coroutines.launch
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,

) {
    val scope = rememberCoroutineScope()
    var selectedTab by remember { mutableIntStateOf(0) }
    val pagerState = rememberPagerState(
        pageCount = { 3 }
    )

    val trainings = listOf(
        TrainingInfo(
            id = 1,
            date = LocalDate.of(2025, 3, 21),
            progress = 80,
            timeStart = 121231231313L,
            timeEnd = 131231231313L,
            duration = 9999L,
            allExercisesId = listOf(1, 2, 3),
            madeExercisesId = listOf(1, 2)
        ),
        TrainingInfo(
            id = 1,
            date = LocalDate.of(2025, 3, 21),
            progress = 80,
            timeStart = 121231231313L,
            timeEnd = 131231231313L,
            duration = 9999L,
            allExercisesId = listOf(1, 2, 3),
            madeExercisesId = listOf(1, 2)
        )
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
                    trainings = trainings,
                    navController = navController
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
    trainings: List<TrainingInfo>
) {
    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .padding(top = 6.dp)
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
                    navController.navigate(RootNavDestinations.Calendar)
                }
            )

            Spacer(modifier = Modifier.padding(8.dp))

            TodayInfoCard(trainings)
        }

    }
}
