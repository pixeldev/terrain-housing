plugins {
    java
    id("com.github.johnrengelman.shadow") version("7.1.1")
}

repositories {
    mavenLocal()
    maven("https://repo.unnamed.team/repository/unnamed-public/")
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    mavenCentral()
}

dependencies {
    implementation("com.zaxxer:HikariCP:5.0.1")
    implementation("org.jetbrains:annotations:22.0.0")
    implementation("me.yushust.inject:core:0.4.5-SNAPSHOT")

    compileOnly("org.spigotmc:spigot-api:1.17.1-R0.1-SNAPSHOT")
    compileOnly("com.fastasyncworldedit:FastAsyncWorldEdit-Core:2.0.1") { isTransitive = false }
    compileOnly("com.fastasyncworldedit:FastAsyncWorldEdit-Bukkit:2.0.1") { isTransitive = false }
}

tasks {
    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(17))
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
