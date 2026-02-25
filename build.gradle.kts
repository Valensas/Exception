import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "3.2.0"
    id("io.spring.dependency-management") version "1.1.4"
    id("org.jmailen.kotlinter") version "4.1.0"
    id("maven-publish")
    id("java-library")
    id("jacoco")
    kotlin("jvm") version "1.9.21"
    kotlin("plugin.spring") version "1.9.21"
    id("net.thebugmc.gradle.sonatype-central-portal-publisher") version "1.2.4"
}

group = "com.valensas"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")

    compileOnly("org.springframework.cloud:spring-cloud-starter-openfeign")
    compileOnly("org.springframework.boot:spring-boot-starter-web")
    compileOnly("org.springframework:spring-webflux")
    compileOnly("org.springframework.boot:spring-boot-starter-validation")

    implementation("jakarta.annotation:jakarta.annotation-api:2.1.1")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.cloud:spring-cloud-contract-wiremock")
    testImplementation("org.springframework.cloud:spring-cloud-starter-openfeign")
    testImplementation("org.springframework.boot:spring-boot-starter-web")
    testImplementation("org.springframework:spring-webflux")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:2023.0.0")
    }
}

publishing {
    publications {
        create("library", MavenPublication::class.java) {
            artifactId = "exception"
            from(components["java"])
        }
    }
    repositories {
        mavenLocal()
    }
}

signing {
    val keyId = System.getenv("SIGNING_KEYID")
    val secretKey = System.getenv("SIGNING_SECRETKEY")
    val passphrase = System.getenv("SIGNING_PASSPHRASE")

    useInMemoryPgpKeys(keyId, secretKey, passphrase)
}

centralPortal {
    name = "exception"
    username = System.getenv("SONATYPE_USERNAME")
    password = System.getenv("SONATYPE_PASSWORD")
    pom {
        name = "Exception"
        description = "A simple library to manage exceptions for Spring Boot."
        url = "https://valensas.com/"
        scm {
            url = "https://github.com/Valensas/exception"
        }

        licenses {
            license {
                name.set("MIT License")
                url.set("https://mit-license.org")
            }
        }

        developers {
            developer {
                id.set("0")
                name.set("Valensas")
                email.set("info@valensas.com")
            }
        }
    }
}

tasks.test {
    finalizedBy(tasks.jacocoTestCoverageVerification)
}

tasks.jacocoTestCoverageVerification {
    violationRules {
        rule {
            limit {
                counter = "LINE"
                minimum = "0.30".toBigDecimal()
            }
        }
    }
}