plugins {
    id("io.micronaut.library")
    id("com.github.johnrengelman.shadow")
}

version = "0.1"
group = "de.roamingthings"

repositories {
    mavenCentral()
}

micronaut {
    runtime("lambda")
    testRuntime("junit5")
    processing {
        incremental(true)
        annotations("de.roamingthings.*")
    }
}

dependencies {
    implementation("io.micronaut:micronaut-runtime")
    runtimeOnly("ch.qos.logback:logback-classic")
    implementation("io.micronaut:micronaut-validation")
    implementation("io.micronaut:micronaut-http-client")

    implementation("io.micronaut.aws:micronaut-function-aws")

}

java {
    sourceCompatibility = JavaVersion.toVersion("11")
    targetCompatibility = JavaVersion.toVersion("11")
}

tasks.named("assemble") {
    dependsOn("shadowJar")
}
