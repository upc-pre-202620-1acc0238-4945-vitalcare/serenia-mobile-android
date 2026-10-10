package com.vitalcare.serenia.features.socialcompanionship.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable
import com.vitalcare.serenia.features.home.presentation.navigation.HomeRoute
import com.vitalcare.serenia.features.socialcompanionship.presentation.audiosent.AudioSentScreen
import com.vitalcare.serenia.features.socialcompanionship.presentation.circle.CircleScreen
import com.vitalcare.serenia.features.socialcompanionship.presentation.family.FamilyScreen
import com.vitalcare.serenia.features.socialcompanionship.presentation.photodetail.PhotoDetailScreen
import com.vitalcare.serenia.features.socialcompanionship.presentation.tellday.TellDayScreen

@Serializable
data object FamilyNavGraphRoute

@Serializable
data object FamilyRoute

@Serializable
data class PhotoDetailRoute(val senderName: String, val sentAt: String)

@Serializable
data object TellDayRoute

@Serializable
data object AudioSentRoute

@Serializable
data object CircleRoute

fun NavGraphBuilder.familyNavGraph(navController: NavController) {

    navigation<FamilyNavGraphRoute>(startDestination = FamilyRoute) {
        composable<FamilyRoute> {
            FamilyScreen(
                onPhotoClick = { photo ->
                    navController.navigate(PhotoDetailRoute(photo.senderName, photo.sentAt)) {
                        launchSingleTop = true
                    }
                },
                onRecordClick = {
                    navController.navigate(TellDayRoute) {
                        launchSingleTop = true
                    }
                },
                onCircleClick = {
                    navController.navigate(CircleRoute) {
                        launchSingleTop = true
                    }
                }
            )
        }

        composable<PhotoDetailRoute> { backStackEntry ->
            val route: PhotoDetailRoute = backStackEntry.toRoute()
            PhotoDetailScreen(
                senderName = route.senderName,
                sentAt = route.sentAt,
                onBack = {
                    navController.popBackStack<FamilyRoute>(inclusive = false)
                }
            )
        }

        composable<TellDayRoute> {
            TellDayScreen(
                onBack = {
                    navController.popBackStack<FamilyRoute>(inclusive = false)
                },
                onAudioSent = {
                    // The recording screen leaves the back stack so back never returns to it
                    navController.navigate(AudioSentRoute) {
                        popUpTo<FamilyRoute> { inclusive = false }
                        launchSingleTop = true
                    }
                },
                onDiscard = {
                    navController.popBackStack<FamilyRoute>(inclusive = false)
                }
            )
        }

        composable<AudioSentRoute> {
            AudioSentScreen(
                onBackToHome = {
                    navController.popBackStack<HomeRoute>(inclusive = false)
                }
            )
        }

        composable<CircleRoute> {
            CircleScreen(
                onBack = {
                    navController.popBackStack<FamilyRoute>(inclusive = false)
                }
            )
        }
    }
}
