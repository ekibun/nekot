plugins {
    id("com.android.application")
    kotlin("android")
    id("org.jetbrains.compose")
}

android {
    compileSdkVersion(30)

    defaultConfig {
        minSdkVersion(21)
        targetSdkVersion(30)
        versionCode = 1
        versionName = "1.0"
        externalNativeBuild {
            cmake {
                abiFilters = setOf("armeabi-v7a", "arm64-v8a")
            }
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    externalNativeBuild {
        cmake {
            setPath("$rootDir/cxx/CMakeLists.txt")
        }
    }
}

dependencies {
    implementation(project(":common"))
    implementation("androidx.activity:activity-compose:1.3.0-alpha03")
}