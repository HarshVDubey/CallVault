# CallVault — Android Build Configuration Audit

**Audit date:** 27 November 2026  
**Repository:** HarshVDubey/CallVault, branch main  
**Scope:** Static review of checked-in Gradle wrapper, settings, version catalog, root/app build scripts and Gradle properties.  
**Verification level:** Repository-source inspection only. No Gradle build or dependency-resolution command was executed in this review.

## 1. Observed configuration

| Item | Checked-in value | Source |
|---|---|---|
| Gradle wrapper | 9.6.0 (binary distribution) | gradle/wrapper/gradle-wrapper.properties |
| Wrapper distribution checksum | Present | gradle/wrapper/gradle-wrapper.properties |
| Android Gradle Plugin | 9.5.3 | gradle/libs.versions.toml |
| Kotlin Compose plugin | 2.2.10 | gradle/libs.versions.toml |
| KSP | 2.2.10-2.0.2 | gradle/libs.versions.toml |
| Compose BOM | 2026.02.01 | gradle/libs.versions.toml |
| Material3 explicit version alias | 1.5.0 | gradle/libs.versions.toml |
| Room | 2.8.5 | gradle/libs.versions.toml |
| compileSdk | 37 | app/build.gradle.kts |
| targetSdk | 37 | app/build.gradle.kts |
| minSdk | 26 | app/build.gradle.kts |
| Java source compatibility | 11 | app/build.gradle.kts |
| Java target compatibility | 11 | app/build.gradle.kts |
| Namespace/application ID | dev.callvault.app | app/build.gradle.kts |
| Gradle modules | Single :app module | settings.gradle.kts |
| Gradle daemon JVM toolchain | 25 | gradle/gradle-daemon-jvm.properties |
| Gradle configuration cache | Enabled | gradle.properties |
| KSP/Kotlin compatibility workaround | android.disallowKotlinSourceSets=false | gradle.properties |

## 2. Build configuration observations

### 2.1 Gradle and plugin setup
- The wrapper pins Gradle 9.6.0 and includes a distribution SHA-256.
- Plugin management uses Google, Maven Central and Gradle Plugin Portal.
- Dependency resolution is centralized and rejects project-level repositories.
- Root build applies Android application, Kotlin Compose and KSP plugins through the version catalog.
- The project includes one Android application module.

### 2.2 Kotlin, Java and daemon JVM
- Kotlin Compose plugin is pinned to Kotlin 2.2.10.
- App Java source/target compatibility is 11.
- Gradle daemon JVM toolchain is pinned to 25 in gradle/gradle-daemon-jvm.properties. This is the Gradle daemon runtime/toolchain setting; it does not by itself mean app bytecode targets Java 25.
- Confirm the local Android Studio/Gradle JDK selection and wrapper behavior on a clean machine before changing this setting.

### 2.3 Dependency catalog observations
- Room runtime and KTX use Room 2.8.5; compiler is configured through KSP.
- KSP version is 2.2.10-2.0.2.
- Compose BOM is 2026.02.01.
- The catalog declares two aliases for the same Material3 artifact: androidx-compose-material3 (version supplied by BOM) and androidx-material3 (explicit version 1.5.0). The app currently declares both dependencies. This duplication should be simplified only after confirming the intended Material3 version resolved by Gradle.
- Core KTX 1.10.1, Activity Compose 1.8.0 and Lifecycle Runtime KTX 2.6.1 are pinned. Review these older-looking AndroidX pins during a deliberate dependency-upgrade task; do not upgrade them incidentally during architectural refactoring.

### 2.4 KSP compatibility setting
- gradle.properties contains android.disallowKotlinSourceSets=false with a comment describing it as a temporary compatibility setting for KSP with AGP built-in Kotlin.
- Retain for now because project history records successful builds with this workaround.
- Do not remove or change until a controlled build verifies the current AGP/Kotlin/KSP combination without it.

### 2.5 SDK and manifest
- App targets SDK 37, supports min SDK 26 and compiles against SDK 37.
- The checked-in AndroidManifest.xml declares no permissions, services or receivers.
- This is consistent with the current import-only implementation using Android's Storage Access Framework; do not add microphone, phone-state, storage or foreground-service permissions until a specific feature is approved and implemented.

## 3. Findings and recommendations

| Finding | Risk / impact | Recommended action |
|---|---|---|
| Material3 artifact declared twice through two aliases | Version-resolution ambiguity and unnecessary dependency declaration | In a dedicated dependency cleanup, retain one alias and verify resolved dependency graph. |
| Gradle daemon JVM 25 while app Java compatibility is 11 | Easy to misinterpret; not inherently a mismatch | Document distinction; verify local JDK and reproducible build. |
| Temporary KSP source-set workaround present | May mask an upstream compatibility issue | Preserve until controlled build proves safe removal. |
| AndroidX core/activity/lifecycle pins are older than other catalog entries | Potential API/bug-fix drift | Evaluate upgrades separately with tests; do not bulk-upgrade. |
| No Gradle build was run during this remote source audit | Compilation and dependency resolution remain unverified | Run clean gradlew.bat :app:assembleDebug and :app:testDebugUnitTest in the project environment. |
| Test source tree contains only generated example unit/instrumentation tests | No meaningful business-logic regression coverage confirmed | Add tests after T-007/T-008 architecture and state boundaries are established. |

## 4. What this audit does not prove

- It does not prove that the checked-in project currently builds.
- It does not prove that every dependency version is mutually compatible.
- It does not prove emulator or OnePlus 10T runtime behavior.
- It does not independently validate the user-reported previous successful builds.
- It does not authorize a Gradle, AGP, Kotlin, KSP, SDK or dependency upgrade.

## 5. Acceptance and next actions

T-006 is complete as a **static repository configuration audit** because the actual checked-in versions and relevant build settings are recorded. Build execution remains an explicit validation gap and must be handled in the project environment.

Next in sequence: T-007 — move database/repository/import orchestration out of HomeScreen into ViewModel/use-case boundaries. Before changing code, inspect the full current source and choose the smallest migration that preserves the existing SAF import behavior.
