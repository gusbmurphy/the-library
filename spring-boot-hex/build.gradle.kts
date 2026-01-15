import com.github.psxpaul.task.JavaExecFork

plugins {
    application
    id("com.diffplug.spotless") version "7.2.1"
    id("com.github.psxpaul.execfork") version "0.2.2"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web:3.5.5")
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb:3.5.6")
    implementation("org.springframework.kafka:spring-kafka:3.3.9")

    testImplementation(libs.junit.jupiter)
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.10.0")
    testImplementation("com.tngtech.archunit:archunit-junit5:1.4.1")
    testImplementation("org.springframework.boot:spring-boot-testcontainers:3.5.7")
    testImplementation("org.springframework.boot:spring-boot-starter-test:3.5.7")
    testImplementation("org.testcontainers:junit-jupiter:1.18.3")
    testImplementation("org.testcontainers:mongodb:1.18.3")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    implementation(libs.guava)
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

application {
    mainClass = "fun.gusmurphy.library.springboothex.App"
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}

tasks.register<JavaExecFork>("start") {
    classpath = sourceSets.main.get().runtimeClasspath
    main = application.mainClass.get()
    waitForOutput = "Started App"
}

spotless {
    java {
        googleJavaFormat("1.33.0").aosp()
    }
}
