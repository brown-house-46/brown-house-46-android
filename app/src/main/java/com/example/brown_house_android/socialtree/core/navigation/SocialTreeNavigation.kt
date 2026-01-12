package com.example.brown_house_android.socialtree.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.brown_house_android.socialtree.feature.auth.login.LoginScreen
import com.example.brown_house_android.socialtree.feature.auth.signup.SignUpScreen
import com.example.brown_house_android.socialtree.feature.home.HomeScreen
import com.example.brown_house_android.socialtree.feature.node.detail.NodeDetailScreen
import com.example.brown_house_android.socialtree.feature.node.register.NodeRegistrationScreen

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

        // Main flow - Home
        composable(SocialTreeRoutes.Home.route) {
            HomeScreen(
                onNodeClick = { nodeId ->
                    navController.navigate(SocialTreeRoutes.NodeDetail.createRoute(nodeId))
                },
                onAddNode = {
                    navController.navigate(SocialTreeRoutes.NodeRegister.createRoute())
                },
                onOpenProfile = {
                    // TODO: Add profile route when available
                },
                onOpenSettings = {
                    // TODO: Add settings route when available
                }
            )
        }

        // Node management screens
        composable(
            route = SocialTreeRoutes.NodeDetail.route,
            arguments = listOf(navArgument("nodeId") { type = NavType.StringType })
        ) { backStackEntry ->
            val nodeId = backStackEntry.arguments?.getString("nodeId").orEmpty()
            NodeDetailScreen(
                nodeId = nodeId,
                onNavigateBack = { navController.navigateUp() },
                onEditNode = { id ->
                    navController.navigate(SocialTreeRoutes.NodeRegister.createRoute(parentId = id))
                },
                onAddChildNode = { id ->
                    navController.navigate(SocialTreeRoutes.NodeRegister.createRoute(parentId = id))
                }
            )
        }

        composable(
            route = SocialTreeRoutes.NodeRegister.route,
            arguments = listOf(navArgument("parentId") { nullable = true })
        ) { backStackEntry ->
            val parentId = backStackEntry.arguments?.getString("parentId")
            NodeRegistrationScreen(
                parentId = parentId,
                onNavigateBack = { navController.navigateUp() },
                onRegistrationSuccess = {
                    navController.navigate(SocialTreeRoutes.Home.route) {
                        popUpTo(SocialTreeRoutes.Home.route) { inclusive = true }
                    }
                }
            )
        }
    }
}
