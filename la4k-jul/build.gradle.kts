/*
 * Any copyright is dedicated to the Public Domain.
 * https://creativecommons.org/publicdomain/zero/1.0/
 */

import java.net.URI

import org.jetbrains.dokka.gradle.DokkaTask

val mavenUrlBase: String? by project
val mavenUsername: String? by project
val mavenPassword: String? by project

plugins {
    kotlin("jvm")
    id("org.jetbrains.dokka")
    id("maven-publish")
}

tasks {

    register<Jar>("sourcesJar") {
        group = "Build"
        description = "Packages all sources into a JAR."
        classifier = "sources"
        from(sourceSets["main"].allSource)
    }

    register<Jar>("dokkaHtmlJar") {
        group = "Build"
        description = "Packages dokkaHtml output into a JAR."
        classifier = "dokka"
        from(dokkaHtml)
    }
}

kotlin {
    explicitApi()
}

dependencies {
    implementation(project(":la4k-api"))
    testImplementation(kotlin("test"))
    testImplementation(kotlin("test-annotations-common"))
    testImplementation(kotlin("test-junit"))
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["kotlin"])
            artifact(tasks["sourcesJar"])
            artifact(tasks["dokkaHtmlJar"])
        }
    }
    repositories {
        maven {
            url = URI("$mavenUrlBase/la4k-jul;publish=1")
            credentials {
                username = mavenUsername
                password = mavenPassword
            }
        }
    }
}
