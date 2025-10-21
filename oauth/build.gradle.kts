plugins {
    id("java")
    id("application")
}

group = "edu.batodev"
version = "0.0.1-SNAPSHOT"
description = "Sonalake Worldwide Windsurfer’s Weather Service"

repositories {
    mavenCentral()
}

val mockOAuth2ServerVersion = "3.0.0"
dependencies {
    implementation("no.nav.security:mock-oauth2-server:${mockOAuth2ServerVersion}")
}

application {
    mainClass = "com.batodev.oauth.Entrypoint"
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}