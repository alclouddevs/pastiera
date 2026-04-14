# Pastiera - Android Physical Keyboard IME

## Overview

Pastiera is an open-source Android Input Method Editor (IME) specifically designed for devices with physical keyboards (e.g., Unihertz Titan series). It enhances typing efficiency through advanced shortcuts, custom layouts, and gesture support.

## Tech Stack

- **Language**: Kotlin (100%)
- **UI Framework**: Jetpack Compose + legacy Android Views
- **Build System**: Gradle (Kotlin Script, `.gradle.kts`)
- **Min SDK**: Android 10 (API 29)
- **Target SDK**: Android 36

## Key Features

- Advanced modifier keys (Shift, Ctrl, Alt, Sym) with one-shot and lock modes
- Custom keyboard layouts (JSON import/export)
- Compact status bar with LED indicators
- Clipboard manager with multiple entries
- Emoji/symbol pages
- Dictionary-based autocorrection/suggestions
- Full backup/restore system

## Project Structure

```
app/src/main/java/it/palsoftware/pastiera/
├── core/           # Core logic: modifier states, suggestion engine
├── inputmethod/    # InputMethodService and key event routing
├── ui/             # Jetpack Compose themes and UI components
├── data/           # Repositories for layouts, emoji, key mappings
└── backup/         # ZIP-based backup/restore system

app/src/main/assets/common/
├── layouts/        # Default keyboard layout definitions (JSON)
├── dictionaries_serialized/ # Binary dictionary files (.dict)
└── emoji/          # Emoji categorization and search data
```

## Development Notes

- This is a **native Android app** — it cannot be deployed as a web service
- Building requires the Android SDK (not available in Replit's standard environment)
- Java (GraalVM 22.3.1 / OpenJDK 19) is installed for Gradle tooling
- The Gradle wrapper (`gradlew`) is available for build tasks

## Build Commands

```bash
# Build debug APK
./gradlew assembleStableDebug

# Run unit tests (no device needed)
./gradlew :app:testDebugUnitTest

# Run specific test suites
./gradlew :app:testDebugUnitTest --tests it.palsoftware.pastiera.core.ModifierStateControllerTest
```

## Signing & Release

Release signing requires keystore credentials via environment variables:
- `PASTIERA_KEYSTORE_PATH`
- `PASTIERA_KEYSTORE_PASSWORD`
- `PASTIERA_KEY_ALIAS`
- `PASTIERA_KEY_PASSWORD`

Or via `release/keystore.properties` (not tracked in git).

## Distribution

- F-Droid (metadata in `fdroiddata/`)
- GitHub Releases (standard APK)
- Nightly builds via GitHub Actions CI
