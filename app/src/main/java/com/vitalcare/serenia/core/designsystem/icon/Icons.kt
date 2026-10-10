package com.vitalcare.serenia.core.designsystem.icon

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.addPathNodes
import androidx.compose.ui.unit.dp

private fun materialIcon(name: String, pathData: String): ImageVector =
    ImageVector.Builder(
        name = name,
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f,
    )
        .addPath(
            pathData = addPathNodes(pathData),
            fill = SolidColor(Color.Black)
        )
        .build()

val home: ImageVector by lazy {
    materialIcon(
        name = "home",
        pathData = "M12 5.69l5 4.5V18h-2v-6H9v6H7v-7.81l5-4.5M12 3L2 12h3v8h6v-6h2v6h6v-8h3L12 3z"
    )
}

val eventAvailable: ImageVector by lazy {
    materialIcon(
        name = "eventAvailable",
        pathData = "M16.53 11.06L15.47 10l-4.88 4.88-2.12-2.12-1.06 1.06L10.59 17l5.94-5.94zM19 3h-1V1h-2v2H8V1H6v2H5c-1.11 0-1.99 0.9-1.99 2L3 19c0 1.1 0.89 2 2 2h14c1.1 0 2-0.9 2-2V5c0-1.1-0.9-2-2-2zm0 16H5V8h14v11z"
    )
}

val family: ImageVector by lazy {
    materialIcon(
        name = "family",
        pathData = "M16.5 13c-1.2 0-3.07 0.34-4.5 1-1.43-0.67-3.3-1-4.5-1C5.33 13 1 14.08 1 16.25V19h22v-2.75c0-2.17-4.33-3.25-6.5-3.25zm-4 4.5h-10v-1.25c0-0.54 2.56-1.75 5-1.75s5 1.21 5 1.75v1.25zm9 0H14v-1.25c0-0.46-0.2-0.86-0.52-1.22 0.88-0.3 1.96-0.53 3.02-0.53 2.44 0 5 1.21 5 1.75v1.25zM7.5 12c1.93 0 3.5-1.57 3.5-3.5S9.43 5 7.5 5 4 6.57 4 8.5 5.57 12 7.5 12zm0-5.5c1.1 0 2 0.9 2 2s-0.9 2-2 2-2-0.9-2-2 0.9-2 2-2zm9 5.5c1.93 0 3.5-1.57 3.5-3.5S18.43 5 16.5 5 13 6.57 13 8.5s1.57 3.5 3.5 3.5zm0-5.5c1.1 0 2 0.9 2 2s-0.9 2-2 2-2-0.9-2-2 0.9-2 2-2z"
    )
}

val accountCircle: ImageVector by lazy {
    materialIcon(
        name = "accountCircle",
        pathData = "M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zM7.07 18.28c0.43-0.9 3.05-1.78 4.93-1.78s4.51 0.88 4.93 1.78C15.57 19.36 13.86 20 12 20s-3.57-0.64-4.93-1.72zm11.29-1.45c-1.43-1.74-4.9-2.33-6.36-2.33s-4.93 0.59-6.36 2.33C4.62 15.49 4 13.82 4 12c0-4.41 3.59-8 8-8s8 3.59 8 8c0 1.82-0.62 3.49-1.64 4.83zM12 6c-1.94 0-3.5 1.56-3.5 3.5S10.06 13 12 13s3.5-1.56 3.5-3.5S13.94 6 12 6zm0 5c-0.83 0-1.5-0.67-1.5-1.5S11.17 8 12 8s1.5 0.67 1.5 1.5S12.83 11 12 11z"
    )
}

val notificationsActive: ImageVector by lazy {
    materialIcon(
        name = "notificationsActive",
        pathData = "M7.58 4.08L6.15 2.65C3.75 4.48 2.17 7.3 2.03 10.5h2c0.15-2.65 1.51-4.97 3.55-6.42zm12.39 6.42h2c-0.15-3.2-1.73-6.02-4.12-7.85l-1.42 1.43c2.02 1.45 3.39 3.77 3.54 6.42zM18 11c0-3.07-1.64-5.64-4.5-6.32V4c0-0.83-0.67-1.5-1.5-1.5s-1.5 0.67-1.5 1.5v0.68C7.63 5.36 6 7.92 6 11v5l-2 2v1h16v-1l-2-2v-5zm-6 11c0.14 0 0.27-0.01 0.4-0.04 0.65-0.14 1.18-0.58 1.44-1.18 0.1-0.24 0.15-0.5 0.15-0.78h-4c0.01 1.1 0.9 2 2.01 2z"
    )
}

val bedtime: ImageVector by lazy {
    materialIcon(
        name = "bedtime",
        pathData = "M9.27 4.49c-1.63 7.54 3.75 12.41 7.66 13.8C15.54 19.38 13.81 20 12 20c-4.41 0-8-3.59-8-8 0-3.45 2.2-6.4 5.27-7.51m2.72-2.48C6.4 2.01 2 6.54 2 12c0 5.52 4.48 10 10 10 3.07 0 5.85-1.4 7.69-3.62-9.79-0.39-12.15-11.17-7.7-16.37z"
    )
}

val check: ImageVector by lazy {
    materialIcon(
        name = "check",
        pathData = "M9 16.17L4.83 12l-1.42 1.41L9 19 21 7l-1.41-1.41z"
    )
}

val checkCircle: ImageVector by lazy {
    materialIcon(
        name = "checkCircle",
        pathData = "M16.59 7.58L10 14.17l-3.59-3.58L5 12l5 5 8-8zM12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm0 18c-4.41 0-8-3.59-8-8s3.59-8 8-8 8 3.59 8 8-3.59 8-8 8z"
    )
}

val close: ImageVector by lazy {
    materialIcon(
        name = "close",
        pathData = "M19 6.41L17.59 5 12 10.59 6.41 5 5 6.41 10.59 12 5 17.59 6.41 19 12 13.41 17.59 19 19 17.59 13.41 12z"
    )
}

val add: ImageVector by lazy {
    materialIcon(
        name = "add",
        pathData = "M19 13h-6v6h-2v-6H5v-2h6V5h2v6h6v2z"
    )
}
