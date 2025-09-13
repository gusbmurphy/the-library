import com.avast.gradle.dockercompose.ComposeExtension
import com.avast.gradle.dockercompose.tasks.ComposeBuild
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
    testImplementation("com.fasterxml.jackson.core:jackson-databind:2.20.0")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.named<Test>("test") {
    useJUnitPlatform()
    // Since we (effectively) don't own the system under test, this should always rerun the tests
    outputs.upToDateWhen { false }
}

val test by testing.suites.existing(JvmTestSuite::class)

var testSpringBoot = tasks.register<Test>("testSpringBoot") {
    group = "verification"
    description = "Runs acceptance tests against the Spring Boot application."

    testClassesDirs = files(test.map { it.sources.output.classesDirs })
    classpath = files(test.map { it.sources.runtimeClasspath })

    var springBootComposeUp = tasks.getByPath("springBootComposeUp")
    var springBootComposeDown = tasks.getByPath("springBootComposeDown")

    var springBootStart = tasks.getByPath(":spring-boot:start")
    springBootStart.notCompatibleWithConfigurationCache("Uses a thread")

    var test = tasks.getByPath("test")

    dependsOn(springBootStart)
    dependsOn(springBootComposeUp)
    dependsOn(test)
    dependsOn(springBootComposeDown)

    springBootStart.mustRunAfter(springBootComposeUp)
    test.mustRunAfter(springBootStart)
    springBootComposeDown.mustRunAfter(test)
}

configure<ComposeExtension> {
    setProjectName("acceptance")

    createNested("springBoot").apply {
        useComposeFiles = listOf("docker-compose.yml", "../spring-boot/docker-compose.yml")
    }
}

spotless {
    java {
        googleJavaFormat().aosp()
    }
}
