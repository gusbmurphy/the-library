import com.avast.gradle.dockercompose.ComposeExtension

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

tasks.register<Test>("testSpringBootAdm") {
    group = "verification"
    description = "Runs acceptance tests against the anemic domain model Spring Boot application."

    testClassesDirs = files(test.map { it.sources.output.classesDirs })
    classpath = files(test.map { it.sources.runtimeClasspath })

    var composeUp = tasks.getByPath("springBootAdmComposeUp")
    var composeDown = tasks.getByPath("springBootAdmComposeDown")

    var startApplication = tasks.getByPath(":spring-boot-adm:start")
    startApplication.notCompatibleWithConfigurationCache("Uses a thread")

    var test = tasks.getByPath("test")

    dependsOn(startApplication)
    dependsOn(composeUp)
    dependsOn(test)
    dependsOn(composeDown)

    startApplication.mustRunAfter(composeUp)
    test.mustRunAfter(startApplication)
    composeDown.mustRunAfter(test)
}

tasks.register<Test>("testSpringBootHex") {
    group = "verification"
    description = "Runs acceptance tests against the Hexagonal Spring Boot application."

    testClassesDirs = files(test.map { it.sources.output.classesDirs })
    classpath = files(test.map { it.sources.runtimeClasspath })

    var composeUp = tasks.getByPath("composeUp")
    var composeDown = tasks.getByPath("composeDown")

    var startApplication = tasks.getByPath(":spring-boot-hex:start")
    startApplication.notCompatibleWithConfigurationCache("Uses a thread")

    var test = tasks.getByPath("test")

    dependsOn(startApplication)
    dependsOn(test)

    startApplication.mustRunAfter(composeUp)
    test.mustRunAfter(startApplication)
    composeDown.mustRunAfter(test)
}

configure<ComposeExtension> {
    setProjectName("acceptance")

    createNested("springBootAdm").apply {
        useComposeFiles = listOf("docker-compose.yml", "../spring-boot-adm/docker-compose.yml")
    }
}

spotless {
    java {
        googleJavaFormat().aosp()
    }
}
