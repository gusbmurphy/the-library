plugins {
    java
    id("com.diffplug.spotless") version "7.2.1"
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

    testClassesDirs = files(test.map { it.sources.output.classesDirs })
    classpath = files(test.map { it.sources.runtimeClasspath })

    testLogging {
        events("passed", "skipped", "failed")
        showStandardStreams = false
        showExceptions = true
        showCauses = true
        showStackTraces = true
        exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
    }
}

val test by testing.suites.existing(JvmTestSuite::class)

registerAcceptanceTestTasks(
    "SpringBootHex",
    "Hexagonal Spring Boot application",
    listOf("acceptance/docker-compose.yml", "spring-boot-hex/docker-compose.yml")
)

registerAcceptanceTestTasks(
    "SpringBootAdm",
    "Anemic Domain Model Spring Boot application",
    listOf("acceptance/docker-compose.yml", "spring-boot-adm/docker-compose.yml")
)

spotless {
    java {
        googleJavaFormat().aosp()
    }
}
