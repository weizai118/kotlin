
plugins {
    kotlin("jvm")
    id("jps-compatible")
}

dependencies {
    compile(project(":core:util.runtime"))
    compile(project(":compiler:frontend"))
    compile(project(":compiler:frontend.java"))
    compile(project(":compiler:util"))
    compile(project(":compiler:cli-common"))
    compile(project(":kotlin-build-common"))

    compile(project(":js:js.frontend"))

    compile(project(":idea"))
    compile(project(":idea:idea-jvm"))
    compile(project(":idea:idea-jps-common"))

    compileOnly(intellijDep())

    bunched(Bunch.IJ) {
        compileOnly(intellijPluginDep("maven"))
    }

    testCompile(projectTests(":idea"))
    testCompile(projectTests(":compiler:tests-common"))
    testCompile(projectTests(":idea:idea-test-framework"))

    testCompileOnly(intellijDep())

    bunched(Bunch.IJ) {
        testCompileOnly(intellijPluginDep("maven"))
    }

    testRuntime(projectDist(":kotlin-reflect"))
    testRuntime(project(":idea:idea-jvm"))
    testRuntime(project(":idea:idea-android"))
    testRuntime(project(":plugins:android-extensions-ide"))
    testRuntime(project(":plugins:lint"))
    testRuntime(project(":sam-with-receiver-ide-plugin"))
    testRuntime(project(":allopen-ide-plugin"))
    testRuntime(project(":noarg-ide-plugin"))

    testRuntime(intellijDep())
    // TODO: the order of the plugins matters here, consider avoiding order-dependency
    testRuntime(intellijPluginDep("junit"))
    testRuntime(intellijPluginDep("testng"))
    testRuntime(intellijPluginDep("properties"))
    testRuntime(intellijPluginDep("gradle"))
    testRuntime(intellijPluginDep("Groovy"))
    testRuntime(intellijPluginDep("coverage"))

    bunched(Bunch.IJ) {
        testRuntime(intellijPluginDep("maven"))
    }

    testRuntime(intellijPluginDep("android"))

    bunched(Bunch.IJ_18x, Bunch.AS) {
        testRuntime(intellijPluginDep("smali"))
    }
}

sourceSets {
    "main" { bunched(Bunch.IJ) { projectDefault() } }
    "test" { bunched(Bunch.IJ) { projectDefault() } }
}

testsJar()

projectTest {
    workingDir = rootDir
}
