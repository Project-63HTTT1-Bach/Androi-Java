pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

<<<<<<< HEAD:settings.gradle.kts
<<<<<<< HEAD
rootProject.name = "Quiz"
=======
rootProject.name = "Gk Quiz Hindi"
>>>>>>> ae55638b6c2f84b2b929a4dec0f612f8860f150a
=======
rootProject.name = "QuizTest"
>>>>>>> TruongQuocBao:QuizTest2/settings.gradle.kts
include(":app")
 