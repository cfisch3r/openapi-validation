plugins {
    id("org.openapi.generator") version "4.2.3"
}

val contractPackage by extra("de.agiledojo.cdd:price-api-marketing:1.4")
val contractPath by extra("de/agiledojo/cdd/price-api/marketing.yml")

repositories {
    mavenLocal()
    jcenter()
}

val openAPI by configurations.creating
dependencies {
    openAPI(contractPackage)
}

task<org.openapitools.generator.gradle.plugin.tasks.GenerateTask>("generateApiClient")  {
    group = "build setup"
    description = "Generates Price API Client from openapi contract."
    generatorName.set("java")
    val specPath = openAPI.resolve().single { f -> f.isFile }.path
    val contractFile = zipTree(specPath)
            .filter { f -> f.absolutePath.endsWith(contractPath) }
            .singleFile
    inputSpec.set(contractFile.absolutePath)
    outputDir.set("$projectDir/price-api-marketing-java-client")
    apiPackage .set("de.agiledojo.cdd.marketing.price-api.api")
    invokerPackage.set("de.agiledojo.cdd.marketing.price-api.invoker")
    modelPackage.set("de.agiledojo.cdd.marketing.price-api.model")
    configOptions.put("artifactId","price-api-marketing-java-client")
    configOptions.put("dateLibrary","java8")
    configOptions.put("java8","true")
    configOptions.put("library","feign")
    configOptions.put("hideGenerationTimestamp","true")
}
