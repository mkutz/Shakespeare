plugins {
  base
  alias(libs.plugins.sonar)
  alias(libs.plugins.nexusPublish)
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
