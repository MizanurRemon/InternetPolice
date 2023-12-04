// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id (Build.androidGradlePlugin) version Build.androidGradlePluginVersion apply false
    id (Build.libraryGradlePlugin) version Build.androidGradlePluginVersion apply false
    id (Build.kotlinGradlePlugin) version Kotlin.version apply false
    id (Hilt.hiltAndroid) version Hilt.version apply false
}