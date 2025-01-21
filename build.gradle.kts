import io.papermc.paperweight.userdev.ReobfArtifactConfiguration

plugins {
    id("java")
    id("io.papermc.paperweight.userdev") version "2.0.0-beta.14"
    id("net.kyori.indra.licenser.spotless") version "3.1.3"
    id("xyz.jpenilla.run-paper") version "2.3.1"
}

group = "net.strokkur"
version = "0.1-DEV"

paperweight.reobfArtifactConfiguration = ReobfArtifactConfiguration.MOJANG_PRODUCTION

repositories {
    mavenCentral()
}

dependencies {
    paperweight.paperDevBundle("1.21.4-R0.1-SNAPSHOT")
    compileOnly("net.luckperms:api:5.4")
}

tasks {
    spotlessCheck {
        dependsOn(spotlessApply)
    }

    processResources {
        val props = mapOf("version" to version)
        inputs.properties(props)
        filteringCharset = "UTF-8"
        filesMatching("paper-plugin.yml") {
            expand(props)
        }
    }

    runServer {
        minecraftVersion("1.21.4")
        downloadPlugins {
            // LuckPerms
            url("https://download.luckperms.net/1568/bukkit/loader/LuckPerms-Bukkit-5.4.151.jar")
        }
    }
}

indraSpotlessLicenser {
    licenseHeaderFile(rootProject.file("HEADER"))
    property("name", project.name)
    property("year", "2024")
}