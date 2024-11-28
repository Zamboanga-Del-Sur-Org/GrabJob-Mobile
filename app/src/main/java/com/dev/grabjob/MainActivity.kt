package com.dev.grabjob

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.dev.grabjob.ui.screens.DashboardScreen
import com.dev.grabjob.ui.screens.registration.RegistrationScreen
import com.dev.grabjob.ui.screens.splash.AnimatedSplashScreen
import com.dev.grabjob.ui.theme.GrabJobTheme
import kotlinx.coroutines.launch

import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        var keepSplashScreen = true
        installSplashScreen().setKeepOnScreenCondition { keepSplashScreen }
        super.onCreate(savedInstanceState)
        
        setContent {
            GrabJobTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var currentScreen by remember { mutableStateOf("splash") }
                    
                    when (currentScreen) {
                        "splash" -> {
                            AnimatedSplashScreen {
                                currentScreen = "registration"
                                keepSplashScreen = false
                            }
                        }
                        "registration" -> {
                            LaunchedEffect(Unit) {
                                keepSplashScreen = false
                            }
                            RegistrationScreen(
                                onRegistrationComplete = {
                                    currentScreen = "dashboard"
                                }
                            )
                        }
                        "dashboard" -> {
                            LaunchedEffect(Unit) {
                                keepSplashScreen = false
                            }
                            DashboardScreen()
                        }
                    }
                }
            }
        }
    }
}
