import org.gradle.api.Project
import org.gradle.api.tasks.Exec
import org.gradle.kotlin.dsl.register
import java.io.ByteArrayOutputStream

fun createDockerComposeCommand(vararg args: String, composeFiles: List<String>): List<String> {
    return listOf("docker-compose", "--project-directory", ".") +
            composeFiles.flatMap { listOf("-f", it) } +
            listOf("-p", "acceptance") +
            args.toList()
}

fun Project.registerAcceptanceTestTasks(appName: String, appDescription: String, composeFile: String) {
    val allComposeFiles = listOf(composeFile, "acceptance/docker-compose.yml")

    val cleanTask = tasks.register("clean$appName", Exec::class) {
        description = "Clean up $appName services"
        group = "docker"
        workingDir = file("..")
        commandLine = createDockerComposeCommand("down", "-v", composeFiles = allComposeFiles)
        isIgnoreExitValue = true

        standardOutput = ByteArrayOutputStream()
        errorOutput = ByteArrayOutputStream()
    }

    val startTask = tasks.register("start$appName", Exec::class) {
        description = "Start $appName services with docker-compose"
        group = "docker"
        workingDir = file("..")

        dependsOn(cleanTask)

        val stdout = ByteArrayOutputStream()
        val stderr = ByteArrayOutputStream()

        standardOutput = stdout
        errorOutput = stderr

        doLast {
            if (executionResult.get().exitValue != 0) {
                logger.error(stdout.toString())
                logger.error(stderr.toString())
            }
        }

        commandLine = createDockerComposeCommand("up", "-d", "--build", "--wait", composeFiles = allComposeFiles)
    }

    val stopTask = tasks.register("stop$appName", Exec::class) {
        description = "Stop $appName services"
        group = "docker"
        workingDir = file("..")

        commandLine = createDockerComposeCommand("down", "-v", composeFiles = allComposeFiles)
        isIgnoreExitValue = true

        standardOutput = ByteArrayOutputStream()
        errorOutput = ByteArrayOutputStream()
    }

    tasks.register("test$appName") {
        description = "Run acceptance tests against the $appDescription"
        group = "verification"

        dependsOn(startTask, tasks.named("test"))
        finalizedBy(stopTask)
    }

    tasks.named("test") {
        mustRunAfter(startTask)
    }
}
