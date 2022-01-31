plugins {
    java
    id("com.github.johnrengelman.shadow") version("7.1.1")
}

repositories {
    mavenLocal()
    maven("https://repo.unnamed.team/repository/unnamed-public/")
    maven("https://maven.enginehub.org/repo/")
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains:annotations:22.0.0")
    implementation("me.fixeddev:commandflow-brigadier:0.5.0-SNAPSHOT")
    implementation("me.yushust.inject:core:0.4.5-SNAPSHOT")

    compileOnly("org.spigotmc:spigot-api:1.17.1-R0.1-SNAPSHOT")
    compileOnly("com.sk89q.worldedit:worldedit-bukkit:7.2.8")
    compileOnly("com.sk89q.worldguard:worldguard-bukkit:7.0.6")
}

tasks {
    shadowJar {
        val path = "net.cosmogrp.thousing.libs"
        relocate("me.fixeddev", "$path.fixeddev")
        relocate("me.yushust", "$path.yushust")
        relocate("me.lucko", "$path.lucko")
        relocate("net.kyori", "$path.kyori")
        relocate("com.mojang", "$path.mojang")
    }

    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(16))
        }
    }

    processResources {
        filesMatching("**/*.yml") {
            filter<org.apache.tools.ant.filters.ReplaceTokens>(
                "tokens" to mapOf("version" to project.version)
            )
        }
    }
}
