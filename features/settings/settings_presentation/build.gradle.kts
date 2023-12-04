plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

apply {
    from("$rootDir/compose-module.gradle")
}

android {
    namespace = "com.internetpolice.settings_presentation"
}

dependencies {
    implementation(project(Modules.designsystem))
    implementation(project(Modules.common))
    implementation(project(Modules.ui))
    implementation(project(Modules.datastore))
    implementation(project(Modules.settings_domain))
    implementation(Compose.DATASTORE_PREFERENCES)
    implementation(Compose.JSOUP)
    implementation(Compose.htmlText)
}