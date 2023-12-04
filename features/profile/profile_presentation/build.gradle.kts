plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

apply {
    from("$rootDir/compose-module.gradle")
}

android {
    namespace = "com.internetpolice.profile_presentation"
}

dependencies {
    implementation(project(Modules.designsystem))
    implementation(project(Modules.common))
    implementation(project(Modules.ui))
    implementation(project(Modules.profile_domain))
    implementation(project(Modules.database))
    implementation(project(Modules.auth_domain))

    implementation(Compose.COMPOSE_SCREENSHOT)
    implementation(Compose.GSON)
}
