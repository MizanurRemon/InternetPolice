plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

apply {
    from("$rootDir/base-module.gradle")
}

android {
    namespace = "com.internetpolice.core.common"
}

dependencies {
    implementation(Compose.COIL)
    implementation(Compose.GUAVA)
}