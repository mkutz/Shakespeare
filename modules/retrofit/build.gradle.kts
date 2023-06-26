plugins {
  `shakespeare-module`
  `shakespeare-publish`
  `shakespeare-style`
  `shakespeare-testing`
}

repositories { mavenCentral() }

dependencies {
  api(project(":modules:core"))

  api("com.squareup.retrofit2:retrofit:2.9.0")
  api("com.squareup.okhttp3:okhttp:4.11.0")
  api("com.squareup.retrofit2:converter-jackson:2.9.0")
  implementation(platform("com.fasterxml.jackson:jackson-bom:2.15.2"))
  api("com.squareup.retrofit2:converter-scalars:2.9.0")

  testImplementation("com.squareup.okhttp3:mockwebserver:4.11.0")
  testImplementation("no.nav.security:mock-oauth2-server:0.5.9")

  implementation("com.google.code.findbugs:jsr305:3.0.2")

  testImplementation("org.slf4j:slf4j-api:2.0.7")
  testImplementation("ch.qos.logback:logback-classic:1.4.8")

  testImplementation(platform("org.junit:junit-bom:5.9.3"))
  testImplementation("org.junit.jupiter:junit-jupiter-api")
  testImplementation("org.junit.jupiter:junit-jupiter-params")
  testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")

  testImplementation("org.assertj:assertj-core:3.24.2")
}
