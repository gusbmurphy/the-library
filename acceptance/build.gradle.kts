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
}

val test by testing.suites.existing(JvmTestSuite::class)

// Docker Compose orchestration tasks

fun createDockerComposeCommand(vararg args: String, composeFiles: List<String>): List<String> {
    return listOf("docker-compose", "--project-directory", ".") +
        composeFiles.flatMap { listOf("-f", it) } +
        listOf("-p", "acceptance") +
        args.toList()
}

// Spring Boot Hex tasks
val composeFilesHex = listOf("acceptance/docker-compose.yml", "spring-boot-hex/docker-compose.yml")

val cleanSpringBootHex by tasks.registering(Exec::class) {
    description = "Clean up spring-boot-hex services"
    group = "docker"
    workingDir = file("..")
    commandLine = createDockerComposeCommand("down", "-v", composeFiles = composeFilesHex)
    isIgnoreExitValue = true
}

val startSpringBootHex by tasks.registering(Exec::class) {
    description = "Start spring-boot-hex services with docker-compose"
    group = "docker"
    workingDir = file("..")

    dependsOn(cleanSpringBootHex)

    doFirst {
        logger.lifecycle("Starting services for spring-boot-hex...")
    }

    commandLine = createDockerComposeCommand("up", "-d", "--build", "--wait", composeFiles = composeFilesHex)
}

val stopSpringBootHex by tasks.registering(Exec::class) {
    description = "Stop spring-boot-hex services"
    group = "docker"
    workingDir = file("..")

    commandLine = createDockerComposeCommand("down", "-v", composeFiles = composeFilesHex)
    isIgnoreExitValue = true

    doFirst {
        logger.lifecycle("Cleaning up spring-boot-hex services...")
    }
}

tasks.register("testSpringBootHex") {
    description = "Run acceptance tests against the Hexagonal Spring Boot application"
    group = "verification"

    dependsOn(startSpringBootHex, tasks.named("test"))
    finalizedBy(stopSpringBootHex)

    doLast {
        logger.lifecycle("Spring Boot Hex acceptance tests completed successfully!")
    }
}

tasks.named("test") {
    mustRunAfter(startSpringBootHex)
}

// Spring Boot ADM tasks (placeholder for future implementation)
tasks.register("testSpringBootAdm") {
    description = "Run acceptance tests against the Anemic Domain Model Spring Boot application"
    group = "verification"

    doFirst {
        throw GradleException("Spring Boot ADM acceptance tests not yet implemented. TODO: Add Dockerfile and update docker-compose.yml for spring-boot-adm")
    }
}

spotless {
    java {
        googleJavaFormat().aosp()
    }
}
