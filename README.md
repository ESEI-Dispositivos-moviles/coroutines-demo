# Coroutines

Android sample app that demonstrates using Kotlin coroutines to load news headlines from [NewsAPI](https://newsapi.org/) and display them with a simple MVVM + Jetpack UI stack. The project is organised as a set of learning steps: the `main` branch provides the starter code with TODOs, and successive branches implement coroutine best practices including repository suspension, `viewModelScope`, and parallel work with `async`.

## Project Highlights
- MVVM presentation layer with `NewsFragment`, `NewsViewModel`, and a small domain model.
- Retrofit + Moshi client configured to talk to NewsAPI via an authenticated OkHttp pipeline.
- Gradle version catalog driven dependencies (Android Gradle Plugin 8.13, Kotlin 2.2).
- Coroutines-first architecture that evolves across branches, covering structured concurrency and background dispatchers.

## Requirements
- Android Studio Koala or newer with Android Gradle Plugin 8.13 support.
- JDK 11 (configured via the Gradle toolchain).
- A valid NewsAPI key. Provide it either as:
  - an environment variable `NEWSAPI_KEY`, or
  - a `NEWSAPI_KEY` entry inside `local.properties`.

## Build & Run
1. Set the NewsAPI key as described above.
2. From the project root run `./gradlew assembleDebug`, or open the project in Android Studio and use *Run*.
3. Install the resulting APK on an Android device or emulator running API level 36 or higher.

## Repository Structure
- `app/src/main/java/gal/uvigo/coroutines` — Kotlin sources split into `domain`, `model`, `network`, and `ui`.
- `app/src/main/res` — XML resources for layouts, menus, and strings.
- `gradle/libs.versions.toml` — centralises dependency versions, including coroutines (to be added in the exercises), Retrofit, Moshi, and Lifecycle components.

## Branch Guide
| Branch | Purpose |
| --- | --- |
| `main` | Starter branch with blocking network code and inline TODOs guiding the coroutine migration. |
| `2-coroutines-foundations` | Implements coroutine-friendly repository and ViewModel layers using explicit `CoroutineScope` and suspend functions. |
| `3-viewmodelscope-starter` | Prepares the switch to `viewModelScope`, reintroducing manual scope management as TODOs for the learner. |
| `4-viewmodelscope` | Uses the lifecycle-aware `viewModelScope` extension and cleans up manual scope cancellation. |
| `5-async-starter` | Sets up an exercise to launch multiple category fetches in parallel with `async`/`await`. |
| `6-async` | Final solution combining concurrent requests and merging the article lists before updating UI state. |

Clone the repository, checkout the branch that matches the learning stage you need, and compare across branches to see each coroutine concept applied in context.
