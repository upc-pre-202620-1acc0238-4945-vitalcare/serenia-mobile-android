# Serenia Mobile Android

Serenia Mobile Android is the native Android application for older adults of the Serenia platform. Built with Kotlin and Jetpack Compose on the foundations of Domain-Driven Design (DDD) and Clean Architecture, it lets older adults answer a daily check-in, ask their family for help, keep social reminders, send voice messages, receive family photos, and manage their care circle. The app is designed for accessibility first: large type, wide touch targets, in-card confirmations, and a simplified mode. It consumes the Serenia Platform REST API and is organized into the same bounded contexts as the backend.

## Tech Stack

- **Language:** Kotlin 2.4
- **UI:** Jetpack Compose (Compose BOM 2026.09) with Material 3
- **Architecture:** DDD + Clean Architecture, MVVM with `StateFlow` UI states
- **Dependency Injection:** Hilt (with KSP)
- **Networking:** Retrofit 3 + Gson over OkHttp
- **Image Loading:** Coil 3 (shares the authenticated OkHttp client)
- **Local Storage:** Jetpack DataStore (session)
- **Navigation:** Navigation Compose with type-safe `@Serializable` routes
- **Media:** `MediaRecorder` / `MediaPlayer` (AAC audio messages), Android Photo Picker
- **Build Tool:** Gradle 9.8 (Gradle Wrapper included) with Android Gradle Plugin 9.4
- **SDK:** `minSdk` 24, `targetSdk` / `compileSdk` 37

## Prerequisites

- Android Studio (latest stable) with its bundled JDK
- Android SDK 37
- An emulator or device running Android 7.0 (API 24) or newer
- The [Serenia Platform](https://github.com/upc-pre-202620-1acc0238-4945-vitalcare/serenia-platform) backend running locally (see Getting Started)

## Project Structure

The project is organized into bounded contexts following DDD principles:

```text
app/src/main/java/com/vitalcare/serenia/
├── core/
│   ├── designsystems/        # Serenia design system (theme, tokens, icons, components)
│   ├── format/               # Spanish date, time, and name formats
│   ├── network/              # Retrofit setup, auth interceptor, API errors
│   └── session/              # Session storage (DataStore)
├── features/
│   ├── iam/                  # Welcome, sign in, sign up, profile data, profile photo
│   ├── dailycheckin/         # Daily check-in, check-in time, daily pause, simplified mode
│   ├── alertsandsafety/      # "Pedir ayuda" emergency button and its outcome
│   ├── carecircle/           # Invitation code, "Mi círculo", family members
│   ├── socialcompanionship/  # Reminders, audio messages, family photos
│   ├── wellbeingmonitoring/  # Reserved (no older-adult screens)
│   ├── home/                 # "Inicio" tab, composes the sections of other contexts
│   └── account/              # "Cuenta" tab, composes IAM and check-in preferences
└── main/                     # MainScreen, navigation host, and bottom navigation bar
```

Each bounded context follows the same layered layout: `domain` (entities and repository contracts, pure Kotlin), `application` (use cases), `infrastructure` (Retrofit APIs, DTOs, mappers, repository implementations, Hilt modules, and device services), and `presentation` (Compose screens, ViewModels, UI states, and navigation graphs). `home` and `account` are presentation-only: they compose the screens of other bounded contexts.

## Configuration

The app reads its configuration from `app/build.gradle.kts` and the Android resources:

- `API_BASE_URL` (`BuildConfig`): Base URL of the Serenia Platform API. Defaults to the deployed backend, `https://serenia-platform.azurewebsites.net/api/v1/`. To use a local backend, build with `-PapiBaseUrl=http://10.0.2.2:8080/api/v1/` (`10.0.2.2` is the host machine as seen from the Android emulator).
- `src/main/res/xml/network_security_config.xml`: Blocks cleartext (HTTP) traffic in release builds.
- `src/debug/res/xml/network_security_config.xml`: Allows cleartext traffic only to `10.0.2.2` and `localhost`, so debug builds can reach the local backend.
- `AndroidManifest.xml`: Declares the `INTERNET`, `VIBRATE`, and `RECORD_AUDIO` permissions. The microphone is requested at runtime the first time the user records a message; profile photos use the system Photo Picker, which needs no permission.

## Getting Started

### 1) Choose the Backend

By default the app talks to the deployed backend (`https://serenia-platform.azurewebsites.net`), so there is nothing to start.

To use a local backend instead, start [Serenia Platform](https://github.com/upc-pre-202620-1acc0238-4945-vitalcare/serenia-platform) with its default `dev` profile so it listens on `http://localhost:8080`, and build the app with `-PapiBaseUrl=http://10.0.2.2:8080/api/v1/`, for example `./gradlew installDebug -PapiBaseUrl=http://10.0.2.2:8080/api/v1/`.

### 2) Run the Application

#### Using Android Studio

1. Open the project folder in Android Studio and let Gradle sync.
2. Select an emulator (API 24+) and run the `app` configuration.

#### Using Gradle

```bash
# Build the debug APK
./gradlew assembleDebug

# Install it on the connected emulator or device
./gradlew installDebug
```

### 3) Using a Physical Device

With the deployed backend a physical device only needs internet access. `10.0.2.2` only exists inside the emulator, so to use a local backend on a physical device, either forward the port with `adb reverse tcp:8080 tcp:8080` and build with `-PapiBaseUrl=http://localhost:8080/api/v1/`, or point `apiBaseUrl` to the backend's LAN address and add that address to the debug network security config.

### 4) Emulator Tips

- **Audio messages:** enable *Extended controls → Microphone → "Virtual microphone uses host audio input"*.
- **Family photos:** only relatives upload photos. To test them, register a `DISTANT_RELATIVE` in the backend Swagger UI, join the care circle with the older adult's invitation code (`POST /api/v1/family-links`), then upload and share a photo (`POST /api/v1/care-circles/{careCircleId}/photo-messages` and `/share`).

## Scripts and Commands

- `./gradlew assembleDebug`: Build the debug APK.
- `./gradlew installDebug`: Install the debug APK on a connected device.
- `./gradlew :app:compileDebugKotlin`: Compile the Kotlin sources only.
- `./gradlew test`: Run the unit tests.
- `./gradlew connectedAndroidTest`: Run the instrumented tests on a connected device.

## Features

### IAM (Identity and Access Management)

- Welcome, role selection, sign in, and sign up (step 1 of 3) with optional birth date and phone number.
- Session restore on launch and automatic sign out when the session expires (401).
- "Mis datos": edit name, birth date, and phone number, and change the profile photo.
- Change password and sign out with in-card confirmation.

### Daily Check-in

- Daily question with three fixed answers ("Muy bien", "Más o menos", "No tan bien") on the "Inicio" tab.
- Answered, paused ("Hoy prefiero no responder" / "Retomar mis preguntas"), missed, and unavailable states.
- Check-in time chosen at registration (step 2 of 3) and in "Cuenta" (between 6:00 a. m. and 8:00 p. m.).
- Simplified mode: larger answers and a two-tab navigation bar ("Inicio" and "Cuenta").

### Alerts and Safety

- "Pedir ayuda" button: hold for 3 seconds with a countdown ring and vibration; a short tap only shows a hint.
- Outcome screen: family notified, no relatives linked yet, or failure.

### Care Circle

- Invitation code at registration (step 3 of 3) and in "Mi círculo", shareable through the system share sheet, with "Código nuevo".
- Family members with their relationship and in-card removal.

### Social Companionship

- "Recordar" tab: "Es hora" card ("Ya lo hice" with undo, "Más tarde" adds one hour, "Ya no lo necesito"), today's and upcoming reminders, and new reminders with day and time pickers.
- "Cuéntame algo": record up to 2 minutes (tap to start, tap to stop), listen to it, and send it to the family.
- "Fotos de tu familia": gallery grouped by week with "Nueva" badges, and a full-screen viewer with swipe and arrows.
- "Inicio" shortcuts after the check-in: "Cuéntame algo", "Fotos nuevas" with a counter, and the next reminder.

## Security Model

- **Authentication:** The JWT and session id returned by the backend are stored with DataStore and sent by `AuthInterceptor` as `Authorization: Bearer <token>` on every request.
- **Session Expiration:** A `401` response on an authenticated request clears the session and sends the user back to "Iniciar sesión".
- **Protected Media:** Audio, photo, and profile photo files are served by protected endpoints; Coil reuses the authenticated OkHttp client so images load with the token.
- **Transport:** Cleartext HTTP is allowed only for the local backend in debug builds.
- **Language:** Requests send `Accept-Language: es-PE`, so backend error messages arrive in Spanish.

## Development Conventions

- **DDD + Clean Architecture:** `domain` and `application` are pure Kotlin (no Android, Retrofit, or Room); DTOs never leave `infrastructure`.
- **Use Cases:** Each operation is a use case with `operator fun invoke` returning `Result<T>`.
- **MVVM:** ViewModels expose a single `StateFlow` UI state; screens are split into a stateful `Screen` and a stateless, previewable `Content`.
- **Design System:** Colors, typography (Nunito), spacing, radii, and touch targets come from `core/designsystems`; minimum touch target is 56 dp and primary actions use 64 dp.
- **Accessibility:** Icons always come with text or a content description, state is never shown only by color, destructive actions are confirmed inside the card, and errors use a field border plus a message.
- **Navigation:** Type-safe routes per bounded context (`*NavGraph.kt`); the bottom bar stays visible on subscreens and tapping the active tab returns to its root screen.
- **Commits:** Conventional Commits with the feature as scope (e.g. `feat(daily-check-in): ...`) and one `feature/*` branch per bounded context, merged into `develop` through pull requests.
