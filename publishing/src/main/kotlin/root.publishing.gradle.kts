plugins {
    id("io.github.gradle-nexus.publish-plugin")
}

allprojects {
    group = "io.github.ahmad-hamwi"
    version = "1.7.3"
}

nexusPublishing {
    // Configure maven central repository
    // https://github.com/gradle-nexus/publish-plugin#publishing-to-maven-central-via-sonatype-ossrh
    repositories {
        sonatype {  // The replacement url of https://s01.oss.sonatype.org as its now deprecated
            nexusUrl.set(uri("https://ossrh-staging-api.central.sonatype.com/service/local/"))
            snapshotRepositoryUrl.set(uri("https://central.sonatype.com/repository/maven-snapshots/"))
        }
    }
}
