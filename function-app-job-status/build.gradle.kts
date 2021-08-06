plugins {
    id("io.micronaut.application")
    id("com.github.johnrengelman.shadow")
}

dependencies {
    implementation("io.micronaut:micronaut-runtime")
    implementation("io.micronaut:micronaut-validation")

    implementation("software.amazon.awssdk:dynamodb:2.17.9")

    implementation("org.apache.logging.log4j:log4j-core:2.14.1")
    implementation("org.apache.logging.log4j:log4j-api:2.14.1")
    implementation("org.apache.logging.log4j:log4j-slf4j-impl:2.14.1")
    implementation("software.amazon.lambda:powertools-logging:1.7.2")
    implementation("software.amazon.lambda:powertools-metrics:1.7.2")
    implementation("software.amazon.lambda:powertools-tracing:1.7.2")

    compileOnly("org.projectlombok:lombok:1.18.20")

    testImplementation("io.micronaut:micronaut-http-client")
    testImplementation("org.assertj:assertj-core:3.20.2")
    testImplementation ("org.mockito:mockito-core:3.+")
    testImplementation ("org.mockito:mockito-junit-jupiter:3.+")
    testCompileOnly("org.projectlombok:lombok:1.18.20")

    annotationProcessor("org.projectlombok:lombok:1.18.20")
    annotationProcessor("io.micronaut:micronaut-inject-java")

    testAnnotationProcessor("org.projectlombok:lombok:1.18.20")
}

application {
    mainClass.set("de.roamingthings.jokes.fn.status.Application")
}
