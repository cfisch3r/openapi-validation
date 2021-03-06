
plugins {
    `java-library`
    `maven-publish`
}

repositories {
    jcenter()
}

dependencies {
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "de.agiledojo.cdd"
            artifactId = "price-api-marketing"
            version = "1.4"

            from(components["java"])
        }
    }
}
