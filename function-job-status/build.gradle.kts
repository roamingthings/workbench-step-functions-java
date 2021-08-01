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
    testImplementation("org.assertj:assertj-core:3.20.2")
    testImplementation ("org.mockito:mockito-core:3.+")
    testImplementation ("org.mockito:mockito-junit-jupiter:3.+")

    annotationProcessor("io.micronaut:micronaut-inject-java")
}

application {
    mainClass.set("de.roamingthings.jokes.fn.status.Application")
}
