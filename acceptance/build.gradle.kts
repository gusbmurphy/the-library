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
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}

spotless {
    java {
        googleJavaFormat().aosp()
    }
}
