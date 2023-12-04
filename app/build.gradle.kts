import java.util.Properties
import java.io.FileInputStream

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.dagger.hilt.android")
    id("kotlin-kapt")
}

val keystorePropertiesFile = rootProject.file("keystore.properties")
val keystoreProperties = Properties()
keystoreProperties.load(FileInputStream(keystorePropertiesFile))

android {
    namespace = ProjectConfig.appId
    compileSdk = ProjectConfig.compileSdk

    defaultConfig {
        applicationId = ProjectConfig.appId
        minSdk = ProjectConfig.minSdk
        targetSdk = ProjectConfig.targetSdk
        versionCode = ProjectConfig.versionCode
        versionName = ProjectConfig.versionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    bundle {
        language {
            enableSplit = false
        }
    }

    signingConfigs {
        create("release") {
            keyAlias = keystoreProperties["keyAlias"] as String
            keyPassword = keystoreProperties["keyPassword"] as String
            storeFile = file(keystoreProperties["storeFile"] as String)
            storePassword = keystoreProperties["storePassword"] as String
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true

            signingConfig = signingConfigs.getByName("release")

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        getByName("debug") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            isDebuggable = true
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = ProjectConfig.jvmTarget
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = Compose.composeCompilerVersion
    }
    packagingOptions {
        resources.excludes.add("META-INF/*")
        resources.excludes.add("META-INF/LICENSE.md")
        resources.excludes.add("META-INF/LICENSE-notice.md")

    }

//    applicationVariants.all {
//        val variant = this
//        variant.outputs
//            .map { it as com.android.build.gradle.internal.api.BaseVariantOutputImpl }
//            .forEach { output ->
//                val outputFileName =
//                    "IP_${variant.baseName}_${variant.versionName}_march20.apk"
//                println("OutputFileName: $outputFileName")
//                output.outputFileName = outputFileName
//            }
//    }

}

dependencies {

    implementation(AndroidX.coreKtx)
    implementation(AndroidX.appCompat)
    implementation(Compose.viewModelCompose)
    implementation(Coil.coilCompose)
    implementation(Exoplayer.exoPlayer)

    implementation(Hilt.hiltAndroidVersion)
    kapt(Hilt.hiltCompiler)
    implementation(project(Modules.ui))
    implementation(Retrofit.retrofit)
    implementation(Retrofit.okHttp)
    implementation(Retrofit.okHttpLoggingInterceptor)

    implementation(Coroutines.coroutines)

    implementation(project(Modules.domain))
    implementation(project(Modules.data))
    implementation(project(Modules.notification_presentation))
    implementation(project(Modules.auth_presentation))
    implementation(project(Modules.auth_domain))
    implementation(project(Modules.auth_data))
    implementation(project(Modules.report_presentation))
    implementation(project(Modules.report_domain))
    implementation(project(Modules.report_data))
    implementation(project(Modules.common))
    implementation(project(Modules.network))
    implementation(project(Modules.database))
    implementation(project(Modules.datastore))
    implementation(project(Modules.designsystem))
    implementation(project(Modules.profile_presentation))
    implementation(project(Modules.profile_data))
    implementation(project(Modules.profile_domain))
    implementation(project(Modules.onboardings_presentation))
    implementation(project(Modules.settings_presentation))
    implementation(project(Modules.settings_domain))
    implementation(project(Modules.settings_data))
    implementation(project(Modules.news_data))
    implementation(project(Modules.news_domain))
    implementation(project(Modules.news_presentation))

    //Compose
    implementation(Compose.compiler)
    implementation(Compose.ui)
    implementation(Compose.uiToolingPreview)
    implementation(Compose.hiltNavigationCompose)
    implementation(Compose.material3)
    implementation(Compose.runtime)
    implementation(Compose.navigation)
    implementation(Compose.activityCompose)
    implementation(Compose.constraintlayoutCompose)
    implementation(Compose.GSON)
    implementation(Compose.BILLING)
    implementation(Compose.DATASTORE_PREFERENCES)
    implementation(Compose.RETROFIT2_KOTLIN_SERIALIZATION_CONVERTER)
    implementation(Compose.KOTLINX_SERIALIZATION_JSON)
    implementation(Compose.ACCOMPANIST_SYSTEMUICONTROLLER)
    implementation(Compose.ACCOMPANIST_PAGER)
    implementation(Compose.GLIDE)
    androidTestImplementation("junit:junit:4.13.2")
    implementation(Compose.GLIDE)

    debugImplementation(Compose.uiTool)

    //app update
    implementation(Compose.appUpdate)
    implementation(Compose.inAppUpdate)


}

