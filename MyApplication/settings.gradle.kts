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

<<<<<<< HEAD:MyApplication/settings.gradle.kts
rootProject.name = "My Application"
include(":app")
=======

rootProject.name = "ExamPractice"
include(":app")

>>>>>>> df05a7a05c9586c55c67161ee8bf5ab7960d6bea:settings.gradle.kts
