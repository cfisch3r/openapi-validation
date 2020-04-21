
plugins {
    // Apply the java-library plugin to add support for Java Library
    `java-library`
    `maven-publish`
}

repositories {
    // Use jcenter for resolving dependencies.
    // You can declare any Maven/Ivy/file repository here.
    jcenter()
}

dependencies {
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "de.agiledojo.cdd"
            artifactId = "price-api-store"
            version = "1.6"

            from(components["java"])
        }
    }
}
