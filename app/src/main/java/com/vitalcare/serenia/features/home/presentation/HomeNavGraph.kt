package com.vitalcare.serenia.features.home.presentation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import kotlinx.serialization.Serializable
import com.vitalcare.serenia.features.alertsandsafety.presentation.HelpSentRoute
import com.vitalcare.serenia.features.socialcompanionship.presentation.AudioMessageRoute
import com.vitalcare.serenia.features.socialcompanionship.presentation.FamilyNavGraphRoute
import com.vitalcare.serenia.features.socialcompanionship.presentation.PhotosRoute

@Serializable
data object HomeNavGraphRoute

@Serializable
data object HomeRoute

fun NavGraphBuilder.homeNavGraph(navController: NavController) {

    navigation<HomeNavGraphRoute>(startDestination = HomeRoute) {
        composable<HomeRoute> {
            HomeScreen(
                onHelpSent = { outcome ->
                    navController.navigate(HelpSentRoute(outcome = outcome.name)) { launchSingleTop = true }
                },
                onTellSomething = { navController.openInFamilyTab(AudioMessageRoute) },
                onSeePhotos = { navController.openInFamilyTab(PhotosRoute) }
            )
        }
    }
}

private fun NavController.openInFamilyTab(route: Any) {
    navigate(FamilyNavGraphRoute) {
        popUpTo<HomeNavGraphRoute> { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
    navigate(route) { launchSingleTop = true }
}
