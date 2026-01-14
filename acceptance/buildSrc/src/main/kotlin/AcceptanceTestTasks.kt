import org.gradle.api.Project
import org.gradle.api.tasks.Exec
import org.gradle.kotlin.dsl.register

fun createDockerComposeCommand(vararg args: String, composeFiles: List<String>): List<String> {
    return listOf("docker-compose", "--project-directory", ".") +
            composeFiles.flatMap { listOf("-f", it) } +
            listOf("-p", "acceptance") +
            args.toList()
}

fun Project.registerAcceptanceTestTasks(appName: String, appDescription: String, composeFiles: List<String>) {
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
