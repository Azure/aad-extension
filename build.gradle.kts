import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.hivemq.extension")
    id("com.github.hierynomus.license")
    id("com.github.sgtsilvio.gradle.utf8")
    kotlin("jvm") version "1.7.20"
}

group = "com.hivemq.extensions"
description = "HiveMQ 4 Hello World Extension - a simple reference for all extension developers"

hivemqExtension {
    name.set("Hello World Extension")
    author.set("HiveMQ")
    priority.set(1000)
    startPriority.set(1000)
    mainClass.set("$group.helloworld.HelloWorldMain")
    sdkVersion.set("$version")

    resources {
        from("LICENSE")
    }
}

/* ******************** test ******************** */

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.google.code.gson:gson:2.10")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    testImplementation("org.junit.jupiter:junit-jupiter-api:${property("junit-jupiter.version")}")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    testImplementation("org.mockito:mockito-core:${property("mockito.version")}")
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}

/* ******************** integration test ******************** */

dependencies {
    integrationTestImplementation("com.hivemq:hivemq-mqtt-client:${property("hivemq-mqtt-client.version")}")
    integrationTestImplementation("org.testcontainers:junit-jupiter:${property("testcontainers.version")}")
    integrationTestImplementation("org.testcontainers:hivemq:${property("testcontainers.version")}")
    integrationTestRuntimeOnly("ch.qos.logback:logback-classic:${property("logback.version")}")
    integrationTestImplementation("org.reflections:reflections:0.10.2")
}

/* ******************** checks ******************** */

license {
    header = rootDir.resolve("HEADER")
    mapping("java", "SLASHSTAR_STYLE")
}

/* ******************** debugging ******************** */

tasks.prepareHivemqHome {
    hivemqHomeDirectory.set(file("/hivemq-4.9.0"))
    from("hivemq.jks")
    from("hivemq-truststore.jks")
}

tasks.runHivemqWithExtension {
    debugOptions {
        enabled.set(false)
    }
}
repositories {
    mavenCentral()
}
val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "1.8"
}
val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "1.8"
}