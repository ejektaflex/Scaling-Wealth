import org.gradle.language.jvm.tasks.ProcessResources
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val v_forge = "1.12.2-14.23.5.2779"
val v_forgelin = "1.8.2"
val v_jei = "4.11.0.206"
val v_minecraft_root = "1.12"
val v_minecraft = "$v_minecraft_root.2"

val v_gamestages = "2.0.105"
val v_bookshelf = "2.3.557"

buildscript {
    repositories {
        jcenter()
        maven("http://files.minecraftforge.net/maven")
    }
    dependencies {
        classpath("net.minecraftforge.gradle:ForgeGradle:2.3-SNAPSHOT")
    }
}

val scaVersion: String
    get() {
        val versionFile = File("src/main/kotlin/ejektaflex/scalingwealth/ScalingWealthInfo.kt")
        val gotVersion = if (versionFile.exists()) {
            versionFile.readLines().find { "VERSION" in it }?.dropWhile { it != '"' }?.drop(1)?.dropLast(1) ?: "UNDEFINED"
        } else {
            "UNDEFINED"
        }
        println("build.gradle.kts got Scaling Wealth version: $gotVersion")
        return gotVersion
    }

version = scaVersion

base {
    archivesBaseName = "ScalingWealth"
}

plugins {
    java
    idea
    kotlin("jvm") version "1.3.10"
    id("net.minecraftforge.gradle.forge") version "2.0.2"
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

minecraft {
    setUseDepAts(true)
    version = v_forge
    runDir = "run"
    mappings = "stable_39"
}

repositories {
    jcenter()
    // Forgelin
    maven("http://maven.shadowfacts.net/")
}

dependencies {
    compile(files("libs"))
    subprojects.forEach { compile(it) }
    compile(kotlin("stdlib", "1.3.10"))
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.withType<ProcessResources> {
    from(sourceSets["main"].resources.srcDirs) {
        include("mcmod.info")
        expand(mapOf("version" to project.version, "mcversion" to project.minecraft.version))
    }
    from(sourceSets["main"].resources.srcDirs) {
        exclude("mcmod.info")
    }
}

// This part may be messy
tasks.withType<Jar> {
    //from(sourceSets["main"].output)
    from(sourceSets["api"].output.classesDirs)
}

tasks.register<Jar>("apiJar") {
    classifier = "api"
    from(sourceSets["api"].output)
    from(sourceSets["api"].allJava)
}

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "1.8"
}

val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "1.8"
}