plugins {
    id("io.micronaut.application")
    id("com.github.johnrengelman.shadow")
}

dependencies {
    implementation("io.micronaut:micronaut-runtime")
    runtimeOnly("ch.qos.logback:logback-classic")
    implementation("io.micronaut:micronaut-validation")

    implementation(platform("software.amazon.awssdk:bom:2.17.9"))
    implementation("software.amazon.awssdk:dynamodb")

    testImplementation("io.micronaut:micronaut-http-client")

    annotationProcessor("io.micronaut:micronaut-inject-java")
}

application {
    mainClass.set("de.roamingthings.jokes.fn.status.Application")
}
