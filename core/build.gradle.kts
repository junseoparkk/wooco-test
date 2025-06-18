import org.springframework.boot.gradle.tasks.bundling.BootJar

val jar: Jar by tasks
val bootJar: BootJar by tasks

jar.enabled = true
bootJar.enabled = false

dependencies {
    implementation(project(":support:common"))

    implementation("org.springframework:spring-tx")
    implementation("org.springframework:spring-aspects")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("org.apache.httpcomponents.client5:httpclient5:5.3")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("jakarta.annotation:jakarta.annotation-api:2.1.1")
}
