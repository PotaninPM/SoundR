package com.potaninpm.soundr.presentation.navigation

import android.content.Context
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.potaninpm.soundr.presentation.screens.CalendarScreen
import com.potaninpm.soundr.presentation.screens.HomeScreen
import com.potaninpm.soundr.presentation.screens.ProfileScreen
import com.potaninpm.soundr.presentation.screens.TrainingInfoScreen
import com.potaninpm.soundr.presentation.screens.TrainingScreen
import com.potaninpm.soundr.presentation.screens.WelcomeScreen

@Composable
fun RootNavigation(
    faceDown: Boolean
) {

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
            HomeScreen(
                navController = rootNavController,
                faceDown = faceDown,
            )
        }

        composable<RootNavDestinations.Profile> {
            ProfileScreen(
                navController = rootNavController,
            )
        }

        /*composable<RootNavDestinations.TrainingInfo> {
            TrainingInfoScreen(
                navController = rootNavController,

            )
        }*/

        composable(
            route = "${RootNavDestinations.TrainingInfo}/{trainingId}",
            arguments = listOf(navArgument("trainingId") { type = NavType.IntType })
        ) { backStackEntry ->
                val trainingId = backStackEntry.arguments!!.getInt("trainingId")
            TrainingInfoScreen(
                navController = rootNavController,
                trainingId
            )
        }

        composable<RootNavDestinations.Calendar> {
            CalendarScreen(navController = rootNavController)
        }

        composable<RootNavDestinations.Training> {
            TrainingScreen(navController = rootNavController)
        }
    }
}