plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    kotlin("plugin.serialization") version "1.8.10"
}

apply {
    from("$rootDir/base-module.gradle")
}

android {
    namespace = "com.internetpolice.profile_data"
    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(Compose.KOTLINX_SERIALIZATION_JSON)
    implementation(Retrofit.retrofit)
    implementation(Retrofit.okHttp)
    implementation(Retrofit.okHttpLoggingInterceptor)
    implementation(Compose.RETROFIT2_KOTLIN_SERIALIZATION_CONVERTER)
    implementation(project(Modules.network))
    implementation(project(Modules.database))
    implementation(project(Modules.datastore))
    implementation(project(Modules.common))
    implementation(project(Modules.profile_domain))
    implementation(Compose.DATASTORE_PREFERENCES)
}
