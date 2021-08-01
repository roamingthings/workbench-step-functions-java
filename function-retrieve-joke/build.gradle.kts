plugins {
    id("io.micronaut.library")
    id("com.github.johnrengelman.shadow")
}

dependencies {
    implementation("io.micronaut:micronaut-runtime")
    runtimeOnly("ch.qos.logback:logback-classic")
    implementation("io.micronaut:micronaut-validation")
    implementation("io.micronaut:micronaut-http-client")

    implementation("io.micronaut.aws:micronaut-function-aws")

    annotationProcessor("io.micronaut:micronaut-inject-java")
}
