import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.tasks.bundling.Jar
import org.gradle.kotlin.dsl.`maven-publish`

plugins {
    `maven-publish`
    signing
}

publishing {

    publications.withType<MavenPublication> {
        artifact(tasks.register("${name}JavadocJar", Jar::class) {
            archiveClassifier.set("javadoc")
            archiveAppendix.set(this@withType.name)
        })

        pom {
            name.set("Lazy Pagination Compose")
            description.set("A Compose Multiplatform pagination solution built on top of lazy lists and handles pagination states automatically as the user scrolls.")
            url.set("https://github.com/Ahmad-Hamwi/lazy-pagination-compose")

            // This is naming the artifact when using publishKotlinMultiplatformPublicationToSonatypeRepository
            // Other tasks are naming their artifacts from the module's name
            artifactId = "lazy-pagination-compose"

            licenses {
                license {
                    name.set("MIT")
                    url.set("https://opensource.org/licenses/MIT")
                }
            }
            developers {
                developer {
                    id.set("Ahmad-Hamwi")
                    name.set("Ahmad Hamwi")
                    url.set("https://github.com/Ahmad-Hamwi")
                }
            }
            scm {
                url.set("https://github.com/Ahmad-Hamwi/lazy-pagination-compose")
            }
        }
    }
}

signing {
    if (project.hasProperty("signing.gnupg.keyName")) {
        useGpgCmd()
        sign(publishing.publications)
    }
}
