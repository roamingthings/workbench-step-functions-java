plugins {
    id("com.github.johnrengelman.shadow") version "7.0.0" apply (false)
    id("io.micronaut.application") version "1.5.4" apply (false)
    id("io.micronaut.library") version "1.5.4" apply (false)
}

subprojects {
    group = "de.roamingthings"
    version = "0.1"

    repositories {
        mavenCentral()
    }

    plugins.withType(org.gradle.api.plugins.JavaPlugin::class) {
        extensions.configure(JavaPluginExtension::class.java) {
            sourceCompatibility = JavaVersion.VERSION_11
            targetCompatibility = JavaVersion.VERSION_11
        }
    }

    plugins.withType(io.micronaut.gradle.MicronautApplicationPlugin::class) {
        extensions.configure(io.micronaut.gradle.MicronautExtension::class) {
            runtime("lambda")
            testRuntime("junit5")
            processing {
                incremental(true)
                annotations("de.roamingthings.*")
            }
        }
    }

    plugins.withType(io.micronaut.gradle.MicronautLibraryPlugin::class) {
        extensions.configure(io.micronaut.gradle.MicronautExtension::class) {
            runtime("lambda")
            testRuntime("junit5")
            processing {
                incremental(true)
                annotations("de.roamingthings.*")
            }
        }
    }

    afterEvaluate {
        if (plugins.hasPlugin("io.micronaut.library")) {
            tasks.named("assemble") {
                dependsOn("shadowJar")
            }
        }
    }
}
