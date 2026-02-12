plugins {
    java
    id("org.springframework.boot") version "3.5.11-SNAPSHOT"
    id("io.spring.dependency-management") version "1.1.7"
    id("org.graalvm.buildtools.native") version "0.10.4"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"
description = "Tracker task"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

graalvmNative {
    binaries {
        named("main") {
            buildArgs.add("--initialize-at-run-time=io.netty.util.internal.shaded.org.jctools.util.UnsafeAccess")
            buildArgs.add("--initialize-at-build-time=io.netty.util.internal.logging.InternalLoggerFactory")

            buildArgs.add("-H:+IncludeAllLocales")

            buildArgs.add("-O2")
        }
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
    maven { url = uri("https://repo.spring.io/snapshot") }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb-reactive")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-aop")

    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    implementation("org.mapstruct:mapstruct:1.6.3")
    annotationProcessor("org.mapstruct:mapstruct-processor:1.6.3")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")

    testImplementation("org.testcontainers:mongodb")
    testImplementation("org.testcontainers:junit-jupiter")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
