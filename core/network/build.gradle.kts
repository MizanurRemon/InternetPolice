plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    kotlin("plugin.serialization") version "1.8.10"
}

apply {
    from("$rootDir/base-module.gradle")
}

android {
    namespace = "com.internetpolice.core.network"

}


dependencies {
    implementation(Retrofit.retrofit)
    implementation(Retrofit.okHttp)
    implementation(Retrofit.okHttpLoggingInterceptor)
    implementation(project(Modules.datastore))
    implementation(project(Modules.common))
    implementation(Compose.RETROFIT2_KOTLIN_SERIALIZATION_CONVERTER)
    implementation(Compose.KOTLINX_SERIALIZATION_JSON)
    implementation(Compose.DATASTORE_PREFERENCES)

}