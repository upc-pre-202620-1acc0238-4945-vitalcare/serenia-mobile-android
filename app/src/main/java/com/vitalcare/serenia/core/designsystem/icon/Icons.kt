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

val chevronLeft: ImageVector by lazy {
    materialIcon(
        name = "chevronLeft",
        pathData = "M15.41 7.41L14 6l-6 6 6 6 1.41-1.41L10.83 12z"
    )
}

val contentCopy: ImageVector by lazy {
    materialIcon(
        name = "contentCopy",
        pathData = "M16 1H4c-1.1 0-2 0.9-2 2v14h2V3h12V1zm3 4H8c-1.1 0-2 0.9-2 2v14c0 1.1 0.9 2 2 2h11c1.1 0 2-0.9 2-2V7c0-1.1-0.9-2-2-2zm0 16H8V7h11v14z"
    )
}

val refresh: ImageVector by lazy {
    materialIcon(
        name = "refresh",
        pathData = "M17.65 6.35C16.2 4.9 14.21 4 12 4c-4.42 0-7.99 3.58-7.99 8s3.57 8 7.99 8c3.73 0 6.84-2.55 7.73-6h-2.08c-0.82 2.33-3.04 4-5.65 4-3.31 0-6-2.69-6-6s2.69-6 6-6c1.66 0 3.14 0.69 4.22 1.78L13 11h7V4l-2.35 2.35z"
    )
}

val mic: ImageVector by lazy {
    materialIcon(
        name = "mic",
        pathData = "M12 14c1.66 0 2.99-1.34 2.99-3L15 5c0-1.66-1.34-3-3-3S9 3.34 9 5v6c0 1.66 1.34 3 3 3zm5.3-3c0 3-2.54 5.1-5.3 5.1S6.7 14 6.7 11H5c0 3.41 2.72 6.23 6 6.72V21h2v-3.28c3.28-0.48 6-3.3 6-6.72h-1.7z"
    )
}

val chevronRight: ImageVector by lazy {
    materialIcon(
        name = "chevronRight",
        pathData = "M10 6L8.59 7.41 13.17 12l-4.58 4.59L10 18l6-6z"
    )
}

val favorite: ImageVector by lazy {
    materialIcon(
        name = "favorite",
        pathData = "M12 21.35l-1.45-1.32C5.4 15.36 2 12.28 2 8.5 2 5.42 4.42 3 7.5 3c1.74 0 3.41 0.81 4.5 2.09C13.09 3.81 14.76 3 16.5 3 19.58 3 22 5.42 22 8.5c0 3.78-3.4 6.86-8.55 11.54L12 21.35z"
    )
}

val groups: ImageVector by lazy {
    materialIcon(
        name = "groups",
        pathData = "M12 12.75c1.63 0 3.07 0.39 4.24 0.9 1.08 0.48 1.76 1.56 1.76 2.73V18H6v-1.61c0-1.18 0.68-2.26 1.76-2.73 1.17-0.52 2.61-0.91 4.24-0.91zM4 13c1.1 0 2-0.9 2-2s-0.9-2-2-2-2 0.9-2 2 0.9 2 2 2zm1.13 1.1c-0.37-0.06-0.74-0.1-1.13-0.1-0.99 0-1.93 0.21-2.78 0.58C0.48 14.9 0 15.62 0 16.43V18h4.5v-1.61c0-0.83 0.23-1.61 0.63-2.29zM20 13c1.1 0 2-0.9 2-2s-0.9-2-2-2-2 0.9-2 2 0.9 2 2 2zm4 3.43c0-0.81-0.48-1.53-1.22-1.85-0.85-0.37-1.79-0.58-2.78-0.58-0.39 0-0.76 0.04-1.13 0.1 0.4 0.68 0.63 1.46 0.63 2.29V18H24v-1.57zM12 6c1.66 0 3 1.34 3 3s-1.34 3-3 3-3-1.34-3-3 1.34-3 3-3z"
    )
}

val person: ImageVector by lazy {
    materialIcon(
        name = "person",
        pathData = "M12 12c2.21 0 4-1.79 4-4s-1.79-4-4-4-4 1.79-4 4 1.79 4 4 4zm0 2c-2.67 0-8 1.34-8 4v2h16v-2c0-2.66-5.33-4-8-4z"
    )
}

val personOutline: ImageVector by lazy {
    materialIcon(
        name = "personOutline",
        pathData = "M12 6c1.1 0 2 0.9 2 2s-0.9 2-2 2-2-0.9-2-2 0.9-2 2-2m0 9c2.7 0 5.8 1.29 6 2v1H6v-1c0.2-0.71 3.3-2 6-2m0-11C9.79 4 8 5.79 8 8s1.79 4 4 4 4-1.79 4-4-1.79-4-4-4zm0 9c-2.67 0-8 1.34-8 4v3h16v-3c0-2.66-5.33-4-8-4z"
    )
}

val mail: ImageVector by lazy {
    materialIcon(
        name = "mail",
        pathData = "M20 4H4c-1.1 0-1.99 0.9-1.99 2L2 18c0 1.1 0.9 2 2 2h16c1.1 0 2-0.9 2-2V6c0-1.1-0.9-2-2-2zm0 14H4V8l8 5 8-5v10zm-8-7L4 6h16l-8 5z"
    )
}

val lock: ImageVector by lazy {
    materialIcon(
        name = "lock",
        pathData = "M18 8h-1V6c0-2.76-2.24-5-5-5S7 3.24 7 6v2H6c-1.1 0-2 0.9-2 2v10c0 1.1 0.9 2 2 2h12c1.1 0 2-0.9 2-2V10c0-1.1-0.9-2-2-2zM8.9 6c0-1.71 1.39-3.1 3.1-3.1s3.1 1.39 3.1 3.1v2H8.9V6zM18 20H6V10h12v10zm-6-3c1.1 0 2-0.9 2-2s-0.9-2-2-2-2 0.9-2 2 0.9 2 2 2z"
    )
}

val visibility: ImageVector by lazy {
    materialIcon(
        name = "visibility",
        pathData = "M12 4.5C7 4.5 2.73 7.61 1 12c1.73 4.39 6 7.5 11 7.5s9.27-3.11 11-7.5c-1.73-4.39-6-7.5-11-7.5zM12 17c-2.76 0-5-2.24-5-5s2.24-5 5-5 5 2.24 5 5-2.24 5-5 5zm0-8c-1.66 0-3 1.34-3 3s1.34 3 3 3 3-1.34 3-3-1.34-3-3-3z"
    )
}

val visibilityOff: ImageVector by lazy {
    materialIcon(
        name = "visibilityOff",
        pathData = "M12 7c2.76 0 5 2.24 5 5 0 0.65-0.13 1.26-0.36 1.83l2.92 2.92c1.51-1.26 2.7-2.89 3.43-4.75-1.73-4.39-6-7.5-11-7.5-1.4 0-2.74 0.25-3.98 0.7l2.16 2.16C10.74 7.13 11.35 7 12 7zM2 4.27l2.28 2.28 0.46 0.46C3.08 8.3 1.78 10.02 1 12c1.73 4.39 6 7.5 11 7.5 1.55 0 3.03-0.3 4.38-0.84l0.42 0.42L19.73 22 21 20.73 3.27 3 2 4.27zM7.53 9.8l1.55 1.55c-0.05 0.21-0.08 0.43-0.08 0.65 0 1.66 1.34 3 3 3 0.22 0 0.44-0.03 0.65-0.08l1.55 1.55c-0.67 0.33-1.41 0.53-2.2 0.53-2.76 0-5-2.24-5-5 0-0.79 0.2-1.53 0.53-2.2zm4.31-0.78l3.15 3.15 0.02-0.16c0-1.66-1.34-3-3-3l-0.17 0.01z"
    )
}
