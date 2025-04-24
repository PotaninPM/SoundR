package com.potaninpm.soundr.presentation.screens

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.potaninpm.soundr.R
import com.potaninpm.soundr.presentation.navigation.RootNavDestinations
import com.potaninpm.soundr.presentation.viewModel.ProfileViewModel

@Composable
fun WelcomeScreen(
    navController: NavController,
    profileViewModel: ProfileViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val sharedPrefs = context.getSharedPreferences("soundr", Context.MODE_PRIVATE)

    WelcomeScreenContent(
        onGetStartedButtonClicked = { name ->
            profileViewModel.saveUserName(name)

            navController.navigate(RootNavDestinations.Home) {
                popUpTo(RootNavDestinations.Welcome) { inclusive = true }
            }
            sharedPrefs.edit().putBoolean("viewed", true).apply()
        }
    )
}

@Composable
private fun WelcomeScreenContent(
    onGetStartedButtonClicked: (String) -> Unit = {}
) {
    var name by remember { mutableStateOf("") }
    var isNameEntered by remember { mutableStateOf(false) }

    var showContent by remember { mutableStateOf(false) }
    var showNameInput by remember { mutableStateOf(false) }
    var showButton by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (showContent) 1f else 0.8f,
        animationSpec = tween(
            durationMillis = 800,
            easing = FastOutSlowInEasing
        ),
        label = "logo_scale"
    )

    LaunchedEffect(key1 = true) {
        showContent = true
        showNameInput = true
        showButton = true
    }
    
    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color(35, 164, 255, 255).copy(alpha = 0.6f),
                            Color(122, 0, 255, 255).copy(alpha = 0.2f)
                        ),
                        start = androidx.compose.ui.geometry.Offset(0f, 0f),
                        end = androidx.compose.ui.geometry.Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                    )
                )
                .windowInsetsPadding(WindowInsets.systemBars.only(WindowInsetsSides.Vertical))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()

            )
            
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
            ) {
                AnimatedVisibility(
                    visible = showContent,
                    enter = fadeIn(animationSpec = tween(800)) + 
                            slideInVertically(
                                animationSpec = tween(800),
                                initialOffsetY = { it / 2 }
                            )
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .padding(bottom = 32.dp)
                            .scale(scale)
                    ) {
                        Card(
                            shape = CircleShape,
                            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                            modifier = Modifier.size(160.dp)
                        ) {
                            Image(
                                painter = painterResource(R.drawable.logo),
                                contentDescription = "App Logo",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }

                AnimatedVisibility(
                    visible = showContent,
                    enter = fadeIn(animationSpec = tween(800, delayMillis = 300)) + 
                            slideInVertically(
                                animationSpec = tween(800, delayMillis = 300),
                                initialOffsetY = { it / 3 }
                            )
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Welcome to SoundR",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        
                        Text(
                            text = "Your personal pronunciation assistant",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(bottom = 24.dp)
                        )
                    }
                }

                AnimatedVisibility(
                    visible = showNameInput,
                    enter = fadeIn(animationSpec = tween(800)) + slideInVertically(
                                animationSpec = tween(800),
                                initialOffsetY = { it / 4 }
                            )
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "What's your name?",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(bottom = 16.dp)
                            )
                            
                            OutlinedTextField(
                                value = name,
                                onValueChange = { 
                                    name = it
                                    isNameEntered = it.isNotBlank()
                                },
                                label = { Text("Your name") },
                                singleLine = true,
                                shape = RoundedCornerShape(12.dp),
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = Color.Transparent,
                                    unfocusedContainerColor = Color.Transparent,
                                    focusedIndicatorColor = MaterialTheme.colorScheme.primary
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 8.dp)
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))

                AnimatedVisibility(
                    visible = showButton,
                    enter = fadeIn(animationSpec = tween(800)) + 
                            slideInVertically(
                                animationSpec = tween(800),
                                initialOffsetY = { it / 2 }
                            )
                ) {
                    Button(
                        onClick = { onGetStartedButtonClicked(name) },
                        enabled = isNameEntered,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            disabledContainerColor = Color.Gray.copy(0.3f)
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            text = "Get Started",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}

