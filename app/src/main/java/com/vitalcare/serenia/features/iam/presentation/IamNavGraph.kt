package com.vitalcare.serenia.features.iam.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import kotlinx.serialization.Serializable
import com.vitalcare.serenia.features.home.presentation.navigation.HomeNavGraphRoute
import com.vitalcare.serenia.features.iam.presentation.roleselection.RoleSelectionScreen
import com.vitalcare.serenia.features.iam.presentation.signin.SignInScreen
import com.vitalcare.serenia.features.iam.presentation.signup.SignUpScreen
import com.vitalcare.serenia.features.iam.presentation.welcome.WelcomeScreen

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
