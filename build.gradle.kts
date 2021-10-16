description = "Simplified copy of Mockito to show, how Mockito works."

allprojects {
    group = "io.github.mickle-ak.MockitoInside"
    version = "1.0.0"
}

subprojects {
    repositories {
        mavenCentral()
    }

    apply(plugin = "java")
    the<JavaPluginExtension>().apply {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    dependencies {
        val junit5Version = "5.8.1"
        val assertjVersion = "3.21.0"
        val lombokVersion = "1.18.20"

        "implementation"("org.eclipse.jdt:org.eclipse.jdt.annotation:2.2.600")

        "testImplementation"(platform("org.junit:junit-bom:$junit5Version"))
        "testImplementation"("org.junit.jupiter:junit-jupiter:$junit5Version")
        "testImplementation"("org.assertj:assertj-core:$assertjVersion")

        "compileOnly"("org.projectlombok:lombok:$lombokVersion")
        "annotationProcessor"("org.projectlombok:lombok:$lombokVersion")
        "testCompileOnly"("org.projectlombok:lombok:$lombokVersion")
        "testAnnotationProcessor"("org.projectlombok:lombok:$lombokVersion")
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
        "testImplementation"("org.mockito:mockito-inline:4.0.0")
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
