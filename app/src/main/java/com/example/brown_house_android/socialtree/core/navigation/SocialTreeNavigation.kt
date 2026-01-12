package com.example.brown_house_android.socialtree.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.brown_house_android.socialtree.feature.auth.login.LoginScreen
import com.example.brown_house_android.socialtree.feature.auth.signup.SignUpScreen

/**
 * Social Tree Navigation Host
 * Manages navigation between all screens in the app
 */
@Composable
fun SocialTreeNavHost(
    navController: NavHostController = rememberNavController(),
    startDestination: String = SocialTreeRoutes.Login.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // Authentication flow
        composable(SocialTreeRoutes.Login.route) {
            LoginScreen(
                onNavigateToSignUp = {
                    navController.navigate(SocialTreeRoutes.SignUp.route)
                },
                onLoginSuccess = {
                    navController.navigate(SocialTreeRoutes.Home.route) {
                        popUpTo(SocialTreeRoutes.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(SocialTreeRoutes.SignUp.route) {
            SignUpScreen(
                onNavigateBack = { navController.navigateUp() },
                onSignUpSuccess = {
                    navController.navigate(SocialTreeRoutes.Home.route) {
                        popUpTo(SocialTreeRoutes.Login.route) { inclusive = true }
                    }
                }
            )
        }

        // Main flow - Home (placeholder for now)
        composable(SocialTreeRoutes.Home.route) {
            // TODO: Implement HomeScreen in Milestone 3
            PlaceholderHomeScreen()
        }

        // Node management screens (for future)
        // TODO: Implement in Milestone 4
    }
}

@Composable
private fun PlaceholderHomeScreen() {
    androidx.compose.foundation.layout.Box(
        modifier = androidx.compose.ui.Modifier
            .fillMaxSize()
            .background(com.example.brown_house_android.socialtree.core.designsystem.tokens.BrownColor.BackgroundLight),
        contentAlignment = androidx.compose.ui.Alignment.Center
    ) {
        androidx.compose.material3.Text(
            text = "Home Screen\n(Coming in Milestone 3)",
            style = androidx.compose.material3.MaterialTheme.typography.headlineMedium,
            color = com.example.brown_house_android.socialtree.core.designsystem.tokens.BrownColor.Primary,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}
