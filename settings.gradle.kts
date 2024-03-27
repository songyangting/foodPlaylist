pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.github.com/deliveryhero/artefacts") {
            credentials {
                username = extra["PdGithubPackagesUser"].toString()
                password = extra["PdGithubPackagesToken"].toString()
            }
        }

    }
}

rootProject.name = "TryUseBento"
include(":app")
