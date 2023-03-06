plugins {
  `maven-publish`
  signing
}

publishing {
  publications {
    create<MavenPublication>(name) {
      artifactId = "${rootProject.name}-${name}"
      from(components.findByName("java") ?: components.getByName("javaPlatform"))
      pom {
        project.properties["mavenPomName"]?.let { displayName -> name.set(displayName.toString()) }
        project.properties["mavenPomDescription"]?.let { displayName -> description.set(displayName.toString()) }
        url.set("https://shakespeareframework.org")
        licenses {
          license {
            name.set("The Apache License, Version 2.0")
            url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
          }
        }
        developers {
          developer {
            id.set("mkutz")
            name.set("Michael Kutz")
            email.set("mail@michael-kutz.de")
          }
        }
        scm {
          connection.set("scm:git:git://github.com/mkutz/shakespeare.git")
          developerConnection.set("scm:git:ssh://github.com/mkutz/shakespeare.git")
          url.set("https://github.com/mkutz/shakespeare")
        }
      }
    }
  }
}

signing {
  isRequired = !version.toString().endsWith("SNAPSHOT")
  useInMemoryPgpKeys(findProperty("signingKey")?.toString(), findProperty("signingPassword")?.toString())
  sign(publishing.publications[name])
}
