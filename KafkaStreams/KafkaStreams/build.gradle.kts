import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.31"
    id("com.github.davidmc24.gradle.plugin.avro") version "1.2.0"
    application
}

application{
    mainClass.set("MainKt")
}

repositories {
    mavenCentral()
    maven {
        url = uri("https://packages.confluent.io/maven/")
    }
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.5.31")
    implementation("org.apache.logging.log4j:log4j-api-kotlin:1.0.0")
    implementation("org.apache.logging.log4j:log4j-core:2.12.0")
    implementation("org.apache.logging.log4j:log4j-slf4j-impl:2.12.0")

    implementation("org.apache.kafka:kafka-streams:3.0.0")
    implementation("io.confluent:kafka-avro-serializer:7.0.0")
    implementation("io.confluent:kafka-streams-avro-serde:5.2.1")
}


tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}
