import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

val currentOs = org.gradle.internal.os.OperatingSystem.current()!!

plugins {
    kotlin("multiplatform") // kotlin("jvm") doesn't work well in IDEA/AndroidStudio (https://github.com/JetBrains/compose-jb/issues/22)
    id("org.jetbrains.compose")
}

tasks.create<Exec>("buildJni") {
    group = "build"
  
    inputs.dir(rootDir.resolve("cxx"))
    inputs.file(projectDir.resolve("build.jni.sh"))
    outputs.dir(projectDir.resolve(".cxx/bin"))
  
    workingDir(projectDir)
    executable = "bash"
    args("-c", "./build.jni.sh \\\"${org.gradle.internal.jvm.Jvm.current().javaHome}\\\"")
}

kotlin {
    jvm {
        withJava()

        val processResources = compilations["main"].processResourcesTaskName
        (tasks[processResources] as ProcessResources).apply {
            dependsOn("buildJni")
            from(projectDir.resolve(".cxx/bin"))
        }
    }
    sourceSets {
        val jvmMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation(project(":common"))
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation("org.jetbrains.kotlin:kotlin-test")
                implementation("org.jetbrains.kotlin:kotlin-test-junit")
            }
        }
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "nekot"
            packageVersion = "1.0.0"
        }
    }
}