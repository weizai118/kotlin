
plugins {
    kotlin("jvm")
    id("jps-compatible")
}

jvmTarget = "1.6"

dependencies {
    compile(project(":core:descriptors"))
    compile(project(":core:descriptors.jvm"))
    compile(project(":compiler:util"))
    compile(project(":compiler:frontend"))

    bunched(Bunch.IJ_182) {
        compile("javax.annotation:jsr250-api:1.0")
    }

    compileOnly(intellijCoreDep()) { includeJars("intellij-core") }
    compileOnly(intellijDep()) { includeJars("annotations", "asm-all", "trove4j", "guava", rootProject = rootProject) }
}

sourceSets {
    "main" { projectDefault() }
    "test" {}
}

