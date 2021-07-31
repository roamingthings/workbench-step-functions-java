plugins {
    id("com.github.johnrengelman.shadow")
    id("io.micronaut.application")
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

    implementation(platform("software.amazon.awssdk:bom:2.17.9"))
    implementation("software.amazon.awssdk:dynamodb")

    testImplementation("io.micronaut:micronaut-http-client")
}

application {
    mainClass.set("de.roamingthings.jokes.fn.status.Application")
}
java {
    sourceCompatibility = JavaVersion.toVersion("11")
    targetCompatibility = JavaVersion.toVersion("11")
}
