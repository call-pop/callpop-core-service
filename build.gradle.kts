import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    java
    id("org.springframework.boot") version "3.3.4"
    id("io.spring.dependency-management") version "1.1.6"
}

group = "com.sdcompany.callpop"
version = "0.0.1"

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

repositories {
    mavenCentral()
}

val jasyptVersion = "3.0.5"
val jjwtVersion = "0.11.5"
val mamStructVersion = "1.5.3.Final"
val spotbugsVersion = "4.9.3"
val springCloudVersion = "2023.0.3"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-websocket")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-json")

    implementation("org.springframework.cloud:spring-cloud-starter-openfeign")

    implementation("org.mapstruct:mapstruct:${mamStructVersion}")
    implementation("com.github.spotbugs:spotbugs-annotations:${spotbugsVersion}")

    runtimeOnly("com.h2database:h2") // 로컬 테스트
    // runtimeOnly("org.postgresql:postgresql") // 운영

    //jasypt
    implementation("com.github.ulisesbocchio:jasypt-spring-boot-starter:${jasyptVersion}")

    // jwt
    implementation("io.jsonwebtoken:jjwt-api:${jjwtVersion}")
    implementation("io.jsonwebtoken:jjwt-impl:${jjwtVersion}")
    implementation("io.jsonwebtoken:jjwt-jackson:${jjwtVersion}")

    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${springCloudVersion}")
    }
}

tasks.test {
    useJUnitPlatform()
}
