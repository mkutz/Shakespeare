plugins {
  `shakespeare-module`
  `shakespeare-publish`
  `shakespeare-style`
}

repositories { mavenCentral() }

dependencies {
  api(project(":modules:core"))

  api("com.squareup.retrofit2:retrofit:2.9.0")
  api("com.squareup.okhttp3:okhttp:4.10.0")
  api("com.squareup.retrofit2:converter-jackson:2.9.0")
  implementation(platform("com.fasterxml.jackson:jackson-bom:2.14.2"))
  api("com.squareup.retrofit2:converter-scalars:2.9.0")

  testImplementation("com.squareup.okhttp3:mockwebserver:4.10.0")
  testImplementation("no.nav.security:mock-oauth2-server:0.5.8")

  implementation("com.google.code.findbugs:jsr305:3.0.2")

  testImplementation("org.slf4j:slf4j-api:2.0.6")
  testImplementation("ch.qos.logback:logback-classic:1.4.5")

  testImplementation(platform("org.junit:junit-bom:5.9.2"))
  testImplementation("org.junit.jupiter:junit-jupiter-api")
  testImplementation("org.junit.jupiter:junit-jupiter-params")
  testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")

  testImplementation("org.assertj:assertj-core:3.24.2")
}
