import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot")           version Versions.springBoot
    id("io.spring.dependency-management")    version Versions.springDepManagement
    id("org.jetbrains.kotlin.jvm")           version Versions.kotlinGlobal
    id("org.jetbrains.kotlin.kapt")          version Versions.kotlinGlobal
    id("org.jetbrains.kotlin.plugin.spring") version Versions.kotlinGlobal
}

group = "org.arhor.photospot"
description = "accountant-telegram-bot"

java {
    sourceCompatibility = JavaVersion.toVersion(Versions.javaGlobal)
    targetCompatibility = JavaVersion.toVersion(Versions.javaGlobal)
}

repositories {
    mavenCentral()
}

configurations {
    developmentOnly
    runtimeClasspath {
        extendsFrom(developmentOnly.get())
    }
    testImplementation {
        exclude(module = "junit-vintage-engine")
    }
}

dependencies {
    kapt("org.springframework:spring-context-indexer")

    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    implementation("com.google.api-client:google-api-client:${Versions.googleApiClient}")
    implementation("com.google.apis:google-api-services-sheets:${Versions.googleApiSheets}")
    implementation("com.google.auth:google-auth-library-oauth2-http:${Versions.googleOauthLib}")

    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.telegram:telegrambots-spring-boot-starter:${Versions.telegrambots}")
    implementation("org.telegram:telegrambotsextensions:${Versions.telegrambots}")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks {
    withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = Versions.javaGlobal
        }
    }

    withType<Test> {
        useJUnitPlatform()
    }

    bootRun {
        jvmArgs = listOf(
            "-XX:+UseSerialGC",
            "-XX:MaxRAM=100m",
            "-Xss512k",
        )
    }

    wrapper {
        gradleVersion = "7.3"
    }
}
