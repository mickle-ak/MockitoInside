plugins {
    java
    idea
}

description = "Simplified copy of Mockito to show, how Mockito works."

allprojects {
    group = "io.github.mickle-ak.MockitoInside"
    version = "1.0.1"
}

val junit5Version  = "5.10.0"
val assertjVersion = "3.24.2"
val lombokVersion  = "1.18.28"
val mockitoVersion = "5.5.0"

subprojects {
    repositories {
        mavenCentral()
    }

    apply(plugin = "java")
    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(17))
        }
    }

    dependencies {

        "implementation"("org.eclipse.jdt:org.eclipse.jdt.annotation:2.2.700")

        "testImplementation"(platform("org.junit:junit-bom:$junit5Version"))
        "testImplementation"("org.junit.jupiter:junit-jupiter:$junit5Version")
        "testImplementation"("org.assertj:assertj-core:$assertjVersion")

        "compileOnly"("org.projectlombok:lombok:$lombokVersion")
        "annotationProcessor"("org.projectlombok:lombok:$lombokVersion")
        "testCompileOnly"("org.projectlombok:lombok:$lombokVersion")
        "testAnnotationProcessor"("org.projectlombok:lombok:$lombokVersion")
    }

    apply(plugin = "idea")
    idea {
        module {
            setDownloadJavadoc(true)
            setDownloadSources(true)
        }
    }
}

project(":MyMocker") {
    dependencies {
        "implementation"(project(":prod"))
    }
}

project(":Mockito") {
    dependencies {
        "implementation"(project(":prod"))
        "testImplementation"("org.mockito:mockito-core:$mockitoVersion")
    }
}


subprojects {

    tasks.withType<Test> {
        useJUnitPlatform()

        with(testLogging) {
            events("skipped", "failed")
            showStandardStreams = true
            showExceptions = true
            showCauses = true
            showStackTraces = true
        }
        enableAssertions = true
        failFast = false

        addTestListener(
            object : TestListener {
                override fun beforeSuite(suite: TestDescriptor) {}
                override fun beforeTest(testDescriptor: TestDescriptor) {}
                override fun afterTest(testDescriptor: TestDescriptor, result: TestResult) {}
                override fun afterSuite(suite: TestDescriptor, result: TestResult) {
                    if (suite.parent == null) { // will match the outermost suite
                        println(
                            "Test result: ${result.resultType} " +
                                    "(${result.testCount} tests, " +
                                    "${result.successfulTestCount} successes, " +
                                    "${result.failedTestCount} failures, " +
                                    "${result.skippedTestCount} skipped)"
                        )
                    }
                }
            })
    }
}
