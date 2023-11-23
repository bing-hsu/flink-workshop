version = properties.getOrDefault("version", "").toString()

val packageName = "xyz.threadlite.flinkworkshop.statefulapp"
val entryClass = packageName + "." + properties.getOrDefault("main", "MinimumStatefulApp").toString()
val runLocal = properties["local"] != null


plugins {
    application
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
    }
}

val flinkVer = "1.17.1"
val flinkDomain = "org.apache.flink"

application {
    mainClass.set(entryClass)
}

repositories {
    mavenCentral()
}

dependencies {
    // shadowed dependencies will not be bundled into the shadowJar
    shadow("${flinkDomain}:flink-streaming-java:${flinkVer}")

    // native file connector is provided by runtime
    // shadow include for compilation.
    shadow("${flinkDomain}:flink-connector-files:${flinkVer}")

    // bundle jdbc connector
    // this is not included in default Flink runtime
    implementation("org.apache.flink:flink-connector-jdbc:3.1.0-1.17")
    // bundle JDBC driver for SQLite
    implementation("org.xerial:sqlite-jdbc:3.44.0.0")

    // use ./gradlew run -Plocal=1 to run it like a usual java executable
    if (runLocal) {
        runtimeOnly("${flinkDomain}:flink-runtime:${flinkVer}")
        runtimeOnly("${flinkDomain}:flink-table-runtime:${flinkVer}")
        runtimeOnly("${flinkDomain}:flink-table-planner-loader:${flinkVer}")
        // need flink-clients to run it like a usual java executable
        // https://stackoverflow.com/questions/63600971/no-executorfactory-found-to-execute-the-application-in-flink-1-11-1
        runtimeOnly("${flinkDomain}:flink-clients:${flinkVer}")
    }
}
