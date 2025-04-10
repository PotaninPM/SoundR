package com.potaninpm.soundr.presentation.navigation

import android.content.Context
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.potaninpm.soundr.presentation.screens.CalendarScreen
import com.potaninpm.soundr.presentation.screens.HomeScreen
import com.potaninpm.soundr.presentation.screens.ProfileScreen
import com.potaninpm.soundr.presentation.screens.TrainingScreen
import com.potaninpm.soundr.presentation.screens.WelcomeScreen

@Composable
fun RootNavigation() {
    val context = LocalContext.current
    val rootNavController = rememberNavController()
    val sharedPrefs = context.getSharedPreferences("soundr", Context.MODE_PRIVATE)

    val viewed = sharedPrefs.getBoolean("viewed", false)

    NavHost(
        startDestination = if (!viewed) RootNavDestinations.Welcome else RootNavDestinations.Home,
        navController = rootNavController,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None }
    ) {
        composable<RootNavDestinations.Welcome> {
            WelcomeScreen(navController = rootNavController)
        }

        composable<RootNavDestinations.Home> {
            HomeScreen(navController = rootNavController)
        }

        composable<RootNavDestinations.Profile> {
            ProfileScreen()
        }

        composable<RootNavDestinations.Calendar> {
            CalendarScreen()
        }

        composable<RootNavDestinations.Training> {
            TrainingScreen()
        }
    }
}