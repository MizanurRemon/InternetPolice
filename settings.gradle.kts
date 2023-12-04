pluginManagement {
    repositories {
        google()
        mavenCentral()
        maven { setUrl("https://jitpack.io") }
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { setUrl("https://jitpack.io") }
    }
}
rootProject.name = "InternetPolice"
include(":app")
include(":core:ui")
include(":core:common")
include(":core:designsystem")
include(":core:network")
include(":features:auth:auth_presentation")
include(":features:notification:notification_presentation")
include(":features:profile:profile_presentation")
include(":core:datastore")
include(":core:database")
include(":features:onboardings:onboardings_presentation")
include(":features:auth:auth_data")
include(":features:auth:auth_domain")
include(":features:news:news_data")
include(":features:news:news_domain")
include(":features:news:news_presentation")
include(":features:report:report_presentation")
include(":features:report:report_data")
include(":features:report:report_domain")
include(":features:settings:settings_data")
include(":features:settings:settings_domain")
include(":features:settings:settings_presentation")
include(":features:profile:profile_domain")
include(":features:profile:profile_data")
include(":core:data")
include(":core:domain")
