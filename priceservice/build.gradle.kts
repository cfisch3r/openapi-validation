plugins {
    java
    id("org.springframework.boot").version("2.2.7.RELEASE")
    id("io.spring.dependency-management").version("1.0.8.RELEASE")
}

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    testImplementation("org.springframework.boot:spring-boot-starter-test:2.2.7.RELEASE") {
        exclude("org.junit.vintage","junit-vintage-engine").because("we are using Junit 5 und don't need Junit 4 support.")
    }
    testImplementation("io.rest-assured:rest-assured:4.2.0")
    testImplementation("io.rest-assured:json-path:4.2.0")
    testImplementation("io.rest-assured:xml-path:4.2.0")
    testImplementation("com.atlassian.oai:swagger-request-validator-mockmvc:2.9.0")
    testImplementation("de.agiledojo.cdd:price-api-store:1.6")
    testImplementation("de.agiledojo.cdd:price-api-marketing:1.4")

    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.4.2")
}

val test by tasks.getting(Test::class) {
    useJUnitPlatform()
}
