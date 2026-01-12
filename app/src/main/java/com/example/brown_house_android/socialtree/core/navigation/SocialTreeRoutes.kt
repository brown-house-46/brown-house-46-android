package com.example.brown_house_android.socialtree.core.navigation

/**
 * Navigation routes for Social Tree app
 */
sealed class SocialTreeRoutes(val route: String) {
    // Authentication
    object Login : SocialTreeRoutes("login")
    object SignUp : SocialTreeRoutes("signup")

    // Main screens
    object Home : SocialTreeRoutes("home")

    // Node management
    object NodeDetail : SocialTreeRoutes("node/{nodeId}") {
        fun createRoute(nodeId: String) = "node/$nodeId"
    }

    object NodeRegister : SocialTreeRoutes("node/register?parentId={parentId}") {
        fun createRoute(parentId: String? = null) =
            if (parentId != null) "node/register?parentId=$parentId"
            else "node/register"
    }

    // Additional screens (for future)
    object Friends : SocialTreeRoutes("friends")
    object ShareTree : SocialTreeRoutes("share")
}
