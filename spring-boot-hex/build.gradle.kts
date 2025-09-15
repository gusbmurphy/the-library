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

    testImplementation(libs.junit.jupiter)
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
        googleJavaFormat().aosp()
    }
}
