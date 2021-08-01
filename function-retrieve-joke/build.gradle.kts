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

    testImplementation("org.assertj:assertj-core:3.20.2")
    testImplementation ("org.mockito:mockito-core:3.+")
    testImplementation ("org.mockito:mockito-junit-jupiter:3.+")

    annotationProcessor("io.micronaut:micronaut-inject-java")
}
