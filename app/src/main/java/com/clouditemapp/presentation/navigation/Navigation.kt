package com.clouditemapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.clouditemapp.presentation.ui.game.GameScreen
import com.clouditemapp.presentation.ui.learning.LearningScreen
import com.clouditemapp.presentation.ui.main.MainScreen
import com.clouditemapp.presentation.ui.profile.ProfileScreen
import com.clouditemapp.presentation.ui.settings.SettingsScreen

@Composable
fun CloudItemNavHost(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = "main"
    ) {
        composable("main") {
            MainScreen(navController = navController)
        }

        composable("learning") {
            LearningScreen(navController = navController)
        }

        composable("game") {
            GameScreen(navController = navController)
        }

        composable("profile") {
            ProfileScreen(navController = navController)
        }

        composable("settings") {
            SettingsScreen(navController = navController)
        }
    }
}