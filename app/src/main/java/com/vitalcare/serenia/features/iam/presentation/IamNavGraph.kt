package com.vitalcare.serenia.features.iam.presentation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import kotlinx.serialization.Serializable
import com.vitalcare.serenia.features.home.presentation.HomeNavGraphRoute

@Serializable
data object IamNavGraphRoute

@Serializable
data object WelcomeRoute

@Serializable
data object RoleSelectionRoute

// The role is the name of a UserRole; it is kept in the route for the account creation step
@Serializable
data class SignUpRoute(val role: String)

@Serializable
data object SignInRoute

fun NavGraphBuilder.iamNavGraph(navController: NavController) {

    // Once the user is inside the app the whole access flow leaves the back stack
    val goToHome: () -> Unit = {
        navController.navigate(HomeNavGraphRoute) {
            popUpTo<IamNavGraphRoute> { inclusive = true }
            launchSingleTop = true
        }
    }

    navigation<IamNavGraphRoute>(startDestination = WelcomeRoute) {
        composable<WelcomeRoute> {
            WelcomeScreen(
                onStartClick = {
                    navController.navigate(RoleSelectionRoute) { launchSingleTop = true }
                },
                onHaveAccountClick = {
                    navController.navigate(SignInRoute) { launchSingleTop = true }
                }
            )
        }

        composable<RoleSelectionRoute> {
            RoleSelectionScreen(
                onRoleSelected = { role ->
                    navController.navigate(SignUpRoute(role = role.name)) { launchSingleTop = true }
                }
            )
        }

        composable<SignUpRoute> {
            SignUpScreen(
                onBack = { navController.popBackStack() },
                onSignedUp = goToHome
            )
        }

        composable<SignInRoute> {
            SignInScreen(
                onBack = { navController.popBackStack() },
                onCreateAccount = {
                    navController.navigate(RoleSelectionRoute) { launchSingleTop = true }
                },
                onSignedIn = goToHome
            )
        }
    }
}
