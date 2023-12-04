plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

apply {
    from("$rootDir/compose-module.gradle")
}

android {
    namespace = "com.internetpolice.core.designsystem"
}

dependencies {
    api(Compose.JUNIT_KTX)
    implementation(project(Modules.common))
    implementation(project(Modules.ui))
}