@file:Suppress("DSL_SCOPE_VIOLATION")

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.kotlin.spring)
}

group = "org.arhor.photospot"
description = "accountant-telegram-bot"

val javaVersion = "17"

java {
    sourceCompatibility = JavaVersion.toVersion(javaVersion)
    targetCompatibility = JavaVersion.toVersion(javaVersion)
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
    kapt(libs.spring.context.indexer)

    implementation(libs.kotlin.reflect)
    implementation(libs.kotlin.stdlib.jdk8)
    implementation(libs.google.api.client)
    implementation(libs.google.api.sheets)
    implementation(libs.google.oauth.lib)
    implementation(libs.spring.boot.starter)
    implementation(libs.spring.boot.starter.telegrambots)
    implementation(libs.telegrambots.extensions)

    testImplementation(libs.spring.boot.starter.test)
}

tasks {
    withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = javaVersion
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
