plugins {
  `shakespeare-module`
  `shakespeare-publish`
  `shakespeare-style`
  `shakespeare-testing`
}

repositories { mavenCentral() }

dependencies {
  implementation("com.google.code.findbugs:jsr305:3.0.2")

  implementation("org.slf4j:slf4j-api:2.0.9")

  testImplementation(project(":modules:testutils"))
  testImplementation("ch.qos.logback:logback-classic:1.4.11")

  testImplementation(platform("org.junit:junit-bom:5.10.1"))
  testImplementation("org.junit.jupiter:junit-jupiter-api")
  testImplementation("org.junit.jupiter:junit-jupiter-params")
  testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")

  testImplementation("org.assertj:assertj-core:3.24.2")

  testImplementation("net.jqwik:jqwik:1.8.1")
}
