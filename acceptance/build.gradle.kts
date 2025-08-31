import org.gradle.kotlin.dsl.dockerCompose

plugins {
    java
    id("com.diffplug.spotless") version "7.2.1"
    id("com.avast.gradle.docker-compose") version "0.17.12"
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(libs.junit.jupiter)
    testImplementation("org.apache.kafka:kafka-clients:4.0.0")
    testImplementation("org.apache.httpcomponents:httpclient:4.5.14")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}

tasks.register<Test>("testSpringBoot") {
    group = "verification"
    description = "Runs acceptance tests against the Spring Boot application."
    dependsOn(":spring-boot:start").notCompatibleWithConfigurationCache("Uses a thread")
    dependsOn("test")
    tasks["test"].mustRunAfter(":spring-boot:start")
}

dockerCompose.isRequiredBy(tasks.named("test"))

dockerCompose {
    setProjectName("acceptance")
}

spotless {
    java {
        googleJavaFormat().aosp()
    }
}
