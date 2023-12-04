plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

apply {
    from("$rootDir/compose-module.gradle")
}

android {
    namespace = "com.internetpolice.auth_presentation"
}

dependencies {
    implementation(project(Modules.designsystem))
    implementation(project(Modules.common))
    implementation(project(Modules.ui))
    implementation(project(Modules.auth_domain))
    implementation(project(Modules.database))
    implementation(project(Modules.datastore))
    implementation(project(Modules.network))
    implementation(Compose.DATASTORE_PREFERENCES)
}