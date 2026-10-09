package com.vitalcare.serenia.features.home.presentation.home

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vitalcare.serenia.core.designsystem.icon.bedtime
import com.vitalcare.serenia.core.designsystem.theme.SereniaTheme
import com.vitalcare.serenia.core.designsystem.theme.dotCoral
import com.vitalcare.serenia.features.home.presentation.home.components.GreetingCard
import com.vitalcare.serenia.features.home.presentation.home.components.HelpButton
import com.vitalcare.serenia.features.home.presentation.home.components.MoodOption
import com.vitalcare.serenia.features.checkin.domain.Mood

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
    onMoodSelected: (Mood) -> Unit,
    onHelpClick: () -> Unit
) {

    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value

    HomeContent(
        uiState = uiState,
        modifier = modifier,
        onMoodClick = { mood ->
            viewModel.selectMood(mood)
            onMoodSelected(mood)
        },
        onSkipClick = viewModel::skipToday,
        onHelpClick = onHelpClick
    )
}

@Composable
fun HomeContent(
    uiState: HomeUiState,
    onMoodClick: (Mood) -> Unit,
    onSkipClick: () -> Unit,
    onHelpClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val decorationColor = MaterialTheme.colorScheme.outlineVariant

    Box(modifier = modifier.fillMaxSize()) {

        // Soft background decoration
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                color = decorationColor.copy(alpha = 0.35f),
                radius = size.width * 0.55f,
                center = Offset(0f, size.height)
            )
            drawCircle(
                color = decorationColor.copy(alpha = 0.3f),
                radius = size.width * 0.3f,
                center = Offset(size.width * 1.05f, size.height * 0.88f)
            )
            drawCircle(color = dotCoral, radius = 6.dp.toPx(), center = Offset(size.width * 0.95f, size.height * 0.72f))
            drawCircle(color = dotCoral.copy(alpha = 0.6f), radius = 4.dp.toPx(), center = Offset(size.width * 0.86f, size.height * 0.36f))
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            GreetingCard(
                formattedDate = uiState.formattedDate,
                greeting = uiState.greeting,
                userName = uiState.userName
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Mood.entries.forEach { mood ->
                    MoodOption(
                        mood = mood,
                        isSelected = uiState.selectedMood == mood,
                        onClick = { onMoodClick(mood) },
                        // The middle option sits lower, like a gentle wave
                        modifier = if (mood == Mood.NEUTRAL) Modifier.padding(top = 32.dp) else Modifier
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(onClick = onSkipClick) {
                Icon(
                    imageVector = bedtime,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (uiState.hasSkippedToday) "Está bien, mañana te preguntamos" else "Hoy no quiero responder",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    textDecoration = TextDecoration.Underline,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Leaves room so the help button never covers the content
            Spacer(modifier = Modifier.height(96.dp))
        }

        HelpButton(
            onClick = onHelpClick,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFBF4E9)
@Composable
fun HomeScreenPreview() {
    SereniaTheme {
        HomeContent(
            uiState = HomeUiState(
                userName = "Rosa",
                greeting = "Buenos días",
                formattedDate = "Lun 5 oct",
                selectedMood = Mood.NOT_GOOD
            ),
            onMoodClick = {},
            onSkipClick = {},
            onHelpClick = {}
        )
    }
}
