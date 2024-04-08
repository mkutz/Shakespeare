plugins {
  base
  id("org.sonarqube") version "5.0.0.4638"
  id("io.github.gradle-nexus.publish-plugin") version "2.0.0"
  id("info.solidsoft.pitest.aggregator")
}

repositories { mavenCentral() }

sonar {
  properties {
    property("sonar.projectKey", "mkutz_shakespeare")
    property("sonar.organization", "mkutz")
    property("sonar.host.url", "https://sonarcloud.io")
  }
}

nexusPublishing {
  repositories {
    sonatype {
      nexusUrl.set(uri("https://s01.oss.sonatype.org/service/local/"))
      snapshotRepositoryUrl.set(uri("https://s01.oss.sonatype.org/content/repositories/snapshots/"))
    }
  }
}
