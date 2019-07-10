val group: String by project
val version: String by project

plugins {
    kotlin("multiplatform").version("1.3.31")
    id("maven-publish")
}

repositories {
    mavenLocal()
    mavenCentral()
}

kotlin {
    jvm()
}

dependencies {

    commonMainImplementation(kotlin("stdlib-common"))
    commonTestImplementation(kotlin("test-common"))
    commonTestImplementation(kotlin("test-annotations-common"))

    "jvmMainImplementation"(kotlin("stdlib-jdk8"))
}
