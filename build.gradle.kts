import io.papermc.paperweight.userdev.ReobfArtifactConfiguration

plugins {
    id("java")
    id("io.papermc.paperweight.userdev") version "1.7.7"
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
    paperweight.paperDevBundle("1.20.6-R0.1-SNAPSHOT")
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
        minecraftVersion("1.21.3")
    }
}

indraSpotlessLicenser {
    licenseHeaderFile(rootProject.file("HEADER"))
    property("name", project.name)
    property("year", "2024")
}