import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar;
import com.github.jengelman.gradle.plugins.shadow.tasks.ConfigureShadowRelocation;

plugins {
    java
    id("com.github.johnrengelman.shadow") version "7.1.0"
}

repositories {
    mavenLocal()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    maven("https://repo.maven.apache.org/maven2/")
    maven("https://rayzr.dev/repo/")
    mavenCentral()
    maven(url = "https://oss.sonatype.org/content/repositories/snapshots/") {
        name = "sonatype-oss-snapshots"
    }
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.18-R0.1-SNAPSHOT")
    compileOnly("org.projectlombok:lombok:1.18.22")
    compileOnly("me.clip:placeholderapi:2.11.1")

    annotationProcessor("org.projectlombok:lombok:1.18.22")

    implementation("net.kyori:adventure-platform-bukkit:4.1.0")
    implementation("net.kyori:adventure-text-minimessage:4.10.0")
}

group = "sh.charlie"
version = "1.0.6"
description = "ChitChat"
java.sourceCompatibility = JavaVersion.VERSION_16

val shadowJar: ShadowJar by tasks

tasks.withType<JavaCompile>() {
    options.encoding = "UTF-8"
}

task<ConfigureShadowRelocation>("relocateShadowJar") {
    target = tasks.shadowJar.get()
    prefix = "sh.charlie.chitchat.lib"
}

tasks.shadowJar.get().dependsOn(tasks.getByName("relocateShadowJar"))


tasks.withType<ProcessResources> {
    filesMatching("**/plugin.yml") {
        expand("version" to project.version)
    }
}

tasks.register<Copy>("copyJarToServerPlugins") {
    from(tasks.getByPath("shadowJar"))
    into(layout.projectDirectory.dir("server/plugins"))
}

tasks.withType<JavaCompile>() {
    options.encoding = "UTF-8"
}