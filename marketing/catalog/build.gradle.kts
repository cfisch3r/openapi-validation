plugins {
    `java-library`
}

repositories {
    mavenLocal()
    jcenter()
}

dependencies {
    implementation("javax.validation:validation-api:2.0.1.Final")
    implementation("org.hibernate.validator:hibernate-validator:6.1.2.Final")
    implementation("org.hibernate.validator:hibernate-validator-annotation-processor:6.1.2.Final")
    implementation("javax.el:javax.el-api:3.0.0")
    implementation("org.glassfish.web:javax.el:2.2.6")
    implementation(project(":price-api-marketing-java-client"))

    testImplementation("org.assertj:assertj-core:3.11.1")
    testImplementation("com.github.tomakehurst:wiremock-jre8:2.26.1")
    testImplementation("com.atlassian.oai:swagger-request-validator-wiremock:2.9.0")
    testImplementation(rootProject.extra["contractPackage"]!!)
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.5.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.5.1")
}

val test by tasks.getting(Test::class) {
    useJUnitPlatform()
}
