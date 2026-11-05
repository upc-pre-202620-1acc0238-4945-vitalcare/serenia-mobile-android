package com.vitalcare.serenia.features.socialcompanionship.presentation.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import kotlinx.serialization.Serializable
import com.vitalcare.serenia.features.socialcompanionship.presentation.newreminder.NewReminderScreen
import com.vitalcare.serenia.features.socialcompanionship.presentation.reminders.RemindersScreen
import com.vitalcare.serenia.features.socialcompanionship.presentation.reminders.RemindersViewModel

@Serializable
data object SocialCompanionshipNavGraphRoute

@Serializable
data object RemindersRoute

@Serializable
data object NewReminderRoute

private const val REMINDER_SAVED_KEY = "reminder_saved"

fun NavGraphBuilder.socialCompanionshipNavGraph(navController: NavController) {

    navigation<SocialCompanionshipNavGraphRoute>(startDestination = RemindersRoute) {
        composable<RemindersRoute> { backStackEntry ->
            val viewModel: RemindersViewModel = hiltViewModel()

            // Result sent by the new reminder screen after saving
            val savedStateHandle = backStackEntry.savedStateHandle
            val isReminderSaved by savedStateHandle
                .getStateFlow(REMINDER_SAVED_KEY, false)
                .collectAsStateWithLifecycle()

            LaunchedEffect(isReminderSaved) {
                if (isReminderSaved) {
                    viewModel.onReminderSaved()
                    savedStateHandle[REMINDER_SAVED_KEY] = false
                }
            }

            RemindersScreen(viewModel = viewModel) {
                navController.navigate(NewReminderRoute) {
                    launchSingleTop = true
                }
            }
        }

        composable<NewReminderRoute> {
            NewReminderScreen(
                onBack = {
                    navController.popBackStack<RemindersRoute>(inclusive = false)
                },
                onReminderSaved = {
                    navController.getBackStackEntry<RemindersRoute>()
                        .savedStateHandle[REMINDER_SAVED_KEY] = true
                    navController.popBackStack<RemindersRoute>(inclusive = false)
                }
            )
        }
    }
}
