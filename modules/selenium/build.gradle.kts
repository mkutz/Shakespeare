plugins {
  `shakespeare-module`
  `shakespeare-publish`
  `shakespeare-style`
  `shakespeare-testing`
}

repositories { mavenCentral() }

dependencies {
  api(project(":modules:core"))

  api("org.seleniumhq.selenium:selenium-java:4.18.1")

  api("io.github.bonigarcia:webdrivermanager:5.7.0")
  implementation("commons-io:commons-io:2.15.1")
  implementation(platform("com.fasterxml.jackson:jackson-bom:2.17.0"))
  implementation("org.bouncycastle:bcprov-jdk15on:1.70")

  testImplementation("org.slf4j:slf4j-api:2.0.12")
  testImplementation("ch.qos.logback:logback-classic:1.5.3")

  testImplementation(project(":modules:testutils"))

  testImplementation(platform("org.junit:junit-bom:5.10.2"))
  testImplementation("org.junit.jupiter:junit-jupiter-api")
  testImplementation("org.junit.jupiter:junit-jupiter-params")
  testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")

  testImplementation("org.assertj:assertj-core:3.25.3")
}
