package com.potaninpm.soundr.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.potaninpm.soundr.R

@Composable
fun CustomSplashScreen(
    navController: NavController
) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
    ) {
        val composition by rememberLottieComposition(
            LottieCompositionSpec.RawRes(R.raw.splashscreen)
        )

        val logoAnimationState = animateLottieCompositionAsState(composition = composition)

        LottieAnimation(
            progress = {
                logoAnimationState.progress
            },
            composition = composition
        )

    }
}

