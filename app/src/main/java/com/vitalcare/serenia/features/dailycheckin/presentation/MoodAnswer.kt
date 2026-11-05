package com.vitalcare.serenia.features.dailycheckin.presentation

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.vitalcare.serenia.core.designsystems.sentimentDissatisfied
import com.vitalcare.serenia.core.designsystems.sentimentNeutral
import com.vitalcare.serenia.core.designsystems.sentimentSatisfied
import com.vitalcare.serenia.core.designsystems.theme.SereniaPalette
import com.vitalcare.serenia.features.dailycheckin.domain.MoodLevel

enum class MoodAnswer(
    val label: String,
    val mood: MoodLevel,
    val icon: ImageVector,
    val background: Color,
    val border: Color,
    val circle: Color,
    val face: Color,
    val answeredBackground: Color
) {
    GOOD(
        label = "Muy bien",
        mood = MoodLevel.GOOD,
        icon = sentimentSatisfied,
        background = SereniaPalette.Verde50,
        border = SereniaPalette.Verde300,
        circle = SereniaPalette.Verde200,
        face = SereniaPalette.Verde900,
        answeredBackground = SereniaPalette.SuccessBg
    ),
    NEUTRAL(
        label = "Más o menos",
        mood = MoodLevel.NEUTRAL,
        icon = sentimentNeutral,
        background = SereniaPalette.Durazno50,
        border = SereniaPalette.Durazno300,
        circle = SereniaPalette.Durazno200,
        face = SereniaPalette.Durazno900,
        answeredBackground = SereniaPalette.Durazno100
    ),
    NOT_GOOD(
        label = "No tan bien",
        mood = MoodLevel.LOW,
        icon = sentimentDissatisfied,
        background = SereniaPalette.Teal50,
        border = SereniaPalette.Teal200,
        circle = SereniaPalette.Teal100,
        face = SereniaPalette.Teal900,
        answeredBackground = SereniaPalette.Teal50
    );

    val answeredMessage: String
        get() = when (this) {
            NOT_GOOD -> "Gracias por contarnos. Tu familia ya lo sabe."
            else -> "Respondiste «$label». Tu familia ya sabe cómo amaneciste."
        }
}

fun MoodLevel.toAnswer(): MoodAnswer {
    return when (this) {
        MoodLevel.VERY_GOOD, MoodLevel.GOOD -> MoodAnswer.GOOD
        MoodLevel.NEUTRAL -> MoodAnswer.NEUTRAL
        MoodLevel.LOW, MoodLevel.VERY_LOW -> MoodAnswer.NOT_GOOD
    }
}
