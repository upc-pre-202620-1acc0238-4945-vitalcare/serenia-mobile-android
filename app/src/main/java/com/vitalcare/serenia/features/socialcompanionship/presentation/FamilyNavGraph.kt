package com.vitalcare.serenia.features.socialcompanionship.presentation

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.vitalcare.serenia.features.carecircle.presentation.CircleRoute
import kotlinx.serialization.Serializable

@Serializable
data object FamilyNavGraphRoute

@Serializable
data object FamilyRoute

@Serializable
data object AudioMessageRoute

@Serializable
data object PhotosRoute

@Serializable
data class PhotoViewerRoute(val photoMessageId: String)

private const val MESSAGE_KEY = "message"

fun NavGraphBuilder.familyNavGraph(navController: NavController) {

    navigation<FamilyNavGraphRoute>(startDestination = FamilyRoute) {
        composable<FamilyRoute> { backStackEntry ->
            val message by backStackEntry.savedStateHandle
                .getStateFlow<String?>(MESSAGE_KEY, null)
                .collectAsStateWithLifecycle()

            FamilyScreen(
                message = message,
                onMessageShown = { backStackEntry.savedStateHandle[MESSAGE_KEY] = null },
                onRecord = {
                    navController.navigate(AudioMessageRoute) { launchSingleTop = true }
                },
                onSeePhotos = {
                    navController.navigate(PhotosRoute) { launchSingleTop = true }
                },
                onPhotoClick = { photoMessageId ->
                    navController.navigate(PhotoViewerRoute(photoMessageId)) { launchSingleTop = true }
                },
                onCircle = {
                    navController.navigate(CircleRoute) { launchSingleTop = true }
                }
            )
        }

        composable<AudioMessageRoute> {
            AudioMessageScreen(
                onSent = {
                    navController.getBackStackEntry<FamilyRoute>().savedStateHandle[MESSAGE_KEY] =
                        "Listo, tu familia ya puede escucharlo."
                    navController.popBackStack<FamilyRoute>(inclusive = false)
                },
                onClose = { navController.popBackStack<FamilyRoute>(inclusive = false) }
            )
        }

        composable<PhotosRoute> {
            PhotosScreen(
                onBack = { navController.popBackStack() },
                onPhotoClick = { photoMessageId ->
                    navController.navigate(PhotoViewerRoute(photoMessageId)) { launchSingleTop = true }
                }
            )
        }

        composable<PhotoViewerRoute> { backStackEntry ->
            val route: PhotoViewerRoute = backStackEntry.toRoute()
            PhotoViewerScreen(
                photoMessageId = route.photoMessageId,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
