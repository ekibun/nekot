buildscript {
    // __LATEST_COMPOSE_RELEASE_VERSION__
    val composeVersion = System.getenv("COMPOSE_TEMPLATE_COMPOSE_VERSION") ?: "0.3.0"

    repositories {
        // TODO: remove after new build is published
        maven("https://maven.aliyun.com/nexus/content/groups/public/")
        maven("https://maven.aliyun.com/nexus/content/repositories/jcenter")
        google()
        jcenter()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }

    dependencies {
        classpath("org.jetbrains.compose:compose-gradle-plugin:$composeVersion")
        classpath("com.android.tools.build:gradle:4.0.1")
        // __KOTLIN_COMPOSE_VERSION__
        classpath(kotlin("gradle-plugin", version = "1.4.30"))
    }
}

allprojects {
    repositories {
        maven("https://maven.aliyun.com/nexus/content/groups/public/")
        maven("https://maven.aliyun.com/nexus/content/repositories/jcenter")
        google()
        jcenter()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}
