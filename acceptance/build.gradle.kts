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

fun createDockerComposeCommand(vararg args: String, composeFiles: List<String>): List<String> {
    return listOf("docker-compose", "--project-directory", ".") +
            composeFiles.flatMap { listOf("-f", it) } +
            listOf("-p", "acceptance") +
            args.toList()
}

fun registerAcceptanceTestTasks(appName: String, appDescription: String, composeFiles: List<String>) {
    val cleanTask = tasks.register("clean$appName", Exec::class) {
        description = "Clean up $appName services"
        group = "docker"
        workingDir = file("..")
        commandLine = createDockerComposeCommand("down", "-v", composeFiles = composeFiles)
        isIgnoreExitValue = true
    }

    val startTask = tasks.register("start$appName", Exec::class) {
        description = "Start $appName services with docker-compose"
        group = "docker"
        workingDir = file("..")

        dependsOn(cleanTask)

        doFirst {
            logger.lifecycle("Starting services for $appName...")
        }

        commandLine = createDockerComposeCommand("up", "-d", "--build", "--wait", composeFiles = composeFiles)
    }

    val stopTask = tasks.register("stop$appName", Exec::class) {
        description = "Stop $appName services"
        group = "docker"
        workingDir = file("..")

        commandLine = createDockerComposeCommand("down", "-v", composeFiles = composeFiles)
        isIgnoreExitValue = true

        doFirst {
            logger.lifecycle("Cleaning up $appName services...")
        }
    }

    tasks.register("test$appName") {
        description = "Run acceptance tests against the $appDescription"
        group = "verification"

        dependsOn(startTask, tasks.named("test"))
        finalizedBy(stopTask)

        doLast {
            logger.lifecycle("$appName acceptance tests completed successfully!")
        }
    }

    tasks.named("test") {
        mustRunAfter(startTask)
    }
}

registerAcceptanceTestTasks(
    "SpringBootHex",
    "Hexagonal Spring Boot application",
    listOf("acceptance/docker-compose.yml", "spring-boot-hex/docker-compose.yml")
)

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
