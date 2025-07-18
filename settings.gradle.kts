rootProject.name = "wooco-be"

include(
    "core",
    "bootstrap:api",
    "support:common",
    "support:metric",
    "support:logging",
    "infrastructure:aws",
    "infrastructure:rest",
    "infrastructure:mysql",
    "infrastructure:redis",
    "infrastructure:fcm",
)

pluginManagement {
    val kotlinVersion: String by settings
    val ktlintVersion: String by settings
    val springBootVersion: String by settings
    val springDependencyManagementVersion: String by settings

    resolutionStrategy {
        eachPlugin {
            when (requested.id.id) {
                "org.jetbrains.kotlin.jvm" -> useVersion(kotlinVersion)
                "org.jetbrains.kotlin.plugin.jpa" -> useVersion(kotlinVersion)
                "org.jetbrains.kotlin.plugin.spring" -> useVersion(kotlinVersion)
                "org.springframework.boot" -> useVersion(springBootVersion)
                "io.spring.dependency-management" -> useVersion(springDependencyManagementVersion)
                "org.jlleitschuh.gradle.ktlint" -> useVersion(ktlintVersion)
            }
        }
    }
}
