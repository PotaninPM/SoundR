package com.potaninpm.soundr.presentation.screens

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.potaninpm.soundr.R
import com.potaninpm.soundr.presentation.navigation.RootNavDestinations

@Composable
fun WelcomeScreen(
    navController: NavController
) {
    val context = LocalContext.current
    val sharedPrefs = context.getSharedPreferences("soundr", Context.MODE_PRIVATE)

    WelcomeScreenContent(
        onGetStartedButtonClicked = {
            navController.navigate(RootNavDestinations.Home)
            sharedPrefs.edit().putBoolean("viewed", true).apply()
        }
    )
}

@Composable
private fun WelcomeScreenContent(
    onGetStartedButtonClicked: () -> Unit = {}
) {
    Surface {
        Column(
            verticalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.systemBars.only(WindowInsetsSides.Vertical)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.ic_launcher_background),
                contentDescription = null,
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .height(300.dp)
                    .fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Это приложение поможет вам добиться четкого произношения звука 'Р', а также четко произносить слова с этим звуком.",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    onGetStartedButtonClicked()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Text(text = "Get Started")
            }
        }
    }
}

@Preview
@Composable
private fun WelcomeScreenDarkPreview() {
    MaterialTheme(
        colorScheme = darkColorScheme()
    ) {
        WelcomeScreenContent()
    }
}

@Preview
@Composable
private fun WelcomeScreenLightPreview() {
    MaterialTheme(
        colorScheme = lightColorScheme()
    ) {
        WelcomeScreenContent()
    }
}

