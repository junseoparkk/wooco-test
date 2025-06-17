import org.springframework.boot.gradle.tasks.bundling.BootJar

val jar: Jar by tasks
val bootJar: BootJar by tasks

jar.enabled = true
bootJar.enabled = false

dependencies {
    implementation(project(":core"))

    implementation("com.google.firebase:firebase-admin:${property("firebaseAdminVersion")}")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("org.apache.httpcomponents.client5:httpclient5:5.3")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
}
