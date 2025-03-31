package com.potaninpm.soundr.presentation.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.potaninpm.soundr.presentation.screens.WelcomeScreen

@Composable
fun RootNavigation() {
    val rootNavController = rememberNavController()

    NavHost(
        startDestination = RootNavDestinations.Welcome,
        navController = rootNavController,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None }
    ) {
        composable<RootNavDestinations.Welcome> {
            WelcomeScreen()
        }
    }
}