# Agent Guidelines: CloudItemApp

This document provides essential information for agentic coding agents to work effectively in the CloudItemApp repository.

## Project Overview
CloudItemApp is an Android application built with Kotlin and Jetpack Compose, following Clean Architecture principles. It uses Hilt for dependency injection, Room for local storage, and follows a structured package layout.

## Build, Lint, and Test Commands

### General Commands
- **Build Project:** `./gradlew assembleDebug`
- **Clean Project:** `./gradlew clean`
- **Lint:** `./gradlew lint` (standard Android lint)

### Testing Commands
- **Run all unit tests:** `./gradlew test`
- **Run a single unit test class:**
  `./gradlew :app:testDebugUnitTest --tests "com.clouditemapp.package.ClassName"`
- **Run a single test method:**
  `./gradlew :app:testDebugUnitTest --tests "com.clouditemapp.package.ClassName.methodName"`
- **Run Android Instrumented Tests:** `./gradlew connectedAndroidTest`

## Architecture & Directory Structure
- **`domain`**: Contains business logic: `model`, `repository` interfaces, and `usecase`. Pure Kotlin, no Android dependencies.
- **`data`**: Implementation of `domain/repository`. Contains Room `local` (entities, DAOs, database), `repository` implementations, and `initializer`.
- **`presentation`**: UI layer using Jetpack Compose. Contains `ui` (screens), `viewmodel`, and `navigation`.
- **`di`**: Hilt modules for providing dependencies.

## Code Style & Guidelines

### Kotlin & Android
- **Language:** Kotlin 1.9.22, Target SDK 34, JVM 17.
- **Dependency Injection:** Use Hilt. Annotate ViewModels with `@HiltViewModel` and entry points with `@AndroidEntryPoint`.
- **UI:** Jetpack Compose (Material3).
- **Asynchronous Work:** Use Coroutines and Flow (especially in DAOs and Repositories).

### Naming Conventions
- **Classes:** PascalCase (e.g., `GetItemsByCategoryUseCase`).
- **Functions/Variables:** camelCase (e.g., `itemRepository`).
- **Entities:** Suffix with `Entity` (e.g., `ItemEntity`).
- **Repositories:** Interfaces in `domain`, implementation in `data` suffixed with `Impl` (e.g., `ItemRepositoryImpl`).
- **UI Components:** Suffix screens with `Screen` (e.g., `MainScreen`).

### Imports & Formatting
- Avoid wildcard imports.
- Follow standard Kotlin coding conventions (2-space or 4-space indentation as per project files).
- Use `androidx.compose.material3` for UI components.

### Error Handling
- Use `Result` or custom state classes for repository responses.
- Handle database nullability in DAOs (`suspend fun getItemById(id: Long): ItemEntity?`).
- Use `try-catch` blocks within repositories or use cases for IO/Database exceptions.

### Specific Patterns
- **Room DAOs:** Return `Flow<List<T>>` for observable data.
- **Use Cases:** Implement the `operator fun invoke` pattern.
- **ViewModels:** Expose state via `StateFlow` or Compose `State`.

## Content & Resource Standards

### Item Categories & Data
The app contains 235 items across 8 categories. When adding new items, update `DataInitializer.kt` and follow the established ID ranges:
- **Animals (动物世界)**: ID 1-40
- **Fruits (美味水果)**: ID 101-130
- **Vegetables (新鲜蔬菜)**: ID 201-230
- **Transportation (交通工具)**: ID 301-335
- **Daily Items (日常用品)**: ID 401-440
- **Nature (自然现象)**: ID 501-520
- **Food & Drink (食物与饮料)**: ID 601-625
- **Body Parts (身体部位)**: ID 701-715

### AI Asset Integration
- **Images**: Located in `res/drawable`. Filenames must match the `imageRes` field (lowercase snake_case, e.g., `panda.png`). Style: 3D Clay, white background.
- **Audio**: Located in `res/raw`. Naming convention: `[ID]_cn.mp3` for Chinese and `[ID]_en.mp3` for English.
- **Animations**: Use Lottie. Place JSON files in `res/raw`.

### Interaction Logic
- **Audio Management**: Always use `AudioManager` to play/stop sounds. Do not create raw `MediaPlayer` instances in ViewModels.
- **Achievements**: Achievement checking is handled via `CheckAchievementsUseCase`, triggered automatically after learning or game events.

## Additional Context
- Refer to `项目总结.md` (Project Summary) for history and `PROMPTS.md` for AI asset generation.
- The app uses Lottie for animations and Coil for image loading.
- Color scheme is based on a sky-themed gradient (primary: `0xFF0277BD`, secondary: `0xFF81C784`).
