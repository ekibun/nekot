import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

val currentOs = org.gradle.internal.os.OperatingSystem.current()!!

plugins {
    kotlin("multiplatform") // kotlin("jvm") doesn't work well in IDEA/AndroidStudio (https://github.com/JetBrains/compose-jb/issues/22)
    id("org.jetbrains.compose")
}

tasks.create<Exec>("buildJniNativeWindows") {
    group = "build"
  
    inputs.dir(rootDir.resolve("cxx"))
    outputs.dir(projectDir.resolve(".cxx/Release"))
  
    workingDir(projectDir)
    executable = "cmd"
    args("/C", "build-windows.cmd")
}

kotlin {
    jvm {
        withJava()

        val processResources = compilations["main"].processResourcesTaskName
        (tasks[processResources] as ProcessResources).apply {
            onlyIf { currentOs.isWindows }
            dependsOn("buildJniNativeWindows")
            from(projectDir.resolve(".cxx/Release"))
        }
    }
    sourceSets {
        named("jvmMain") {
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation(project(":common"))
            }
        }
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "KotlinMultiplatformComposeDesktopApplication"
            packageVersion = "1.0.0"
        }
    }
}