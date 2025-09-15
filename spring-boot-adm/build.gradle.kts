import com.github.psxpaul.task.JavaExecFork

plugins {
    application
    id("com.diffplug.spotless") version "7.2.1"
    id("com.github.psxpaul.execfork") version "0.2.2"
    id("io.freefair.lombok") version "9.0.0-rc2"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web:3.5.5")
    implementation("org.springframework.kafka:spring-kafka:3.3.9")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:3.5.5")
    implementation("org.postgresql:postgresql:42.7.7")

    testImplementation(libs.junit.jupiter)
    testImplementation("org.mockito:mockito-core:5.19.0")
    testImplementation("org.mockito:mockito-junit-jupiter:5.19.0")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    implementation(libs.guava)
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

application {
    mainClass = "fun.gusmurphy.library.springbootadm.App"
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
