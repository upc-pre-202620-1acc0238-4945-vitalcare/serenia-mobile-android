package com.vitalcare.serenia.features.socialcompanionship.presentation

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import kotlinx.serialization.Serializable

@Serializable
data object SocialCompanionshipNavGraphRoute

@Serializable
data object RemindersRoute

@Serializable
data object NewReminderRoute

private const val MESSAGE_KEY = "message"

fun NavGraphBuilder.socialCompanionshipNavGraph(navController: NavController) {

    navigation<SocialCompanionshipNavGraphRoute>(startDestination = RemindersRoute) {
        composable<RemindersRoute> { backStackEntry ->
            val message by backStackEntry.savedStateHandle
                .getStateFlow<String?>(MESSAGE_KEY, null)
                .collectAsStateWithLifecycle()

            RemindersScreen(
                message = message,
                onMessageShown = { backStackEntry.savedStateHandle[MESSAGE_KEY] = null },
                onNewReminder = {
                    navController.navigate(NewReminderRoute) { launchSingleTop = true }
                }
            )
        }

        composable<NewReminderRoute> {
            NewReminderScreen(
                onBack = { navController.popBackStack() },
                onSaved = {
                    navController.previousBackStackEntry?.savedStateHandle?.set(
                        MESSAGE_KEY,
                        "Listo, guardamos tu recordatorio."
                    )
                    navController.popBackStack()
                }
            )
        }
    }
}
