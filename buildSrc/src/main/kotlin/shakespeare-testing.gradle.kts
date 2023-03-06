plugins {
  jacoco
  id("info.solidsoft.pitest") apply false
  java
}

tasks.withType<Test> {
  useJUnitPlatform()
  systemProperty("junit.jupiter.execution.parallel.enabled", "true")
  systemProperty("junit.jupiter.execution.parallel.mode.default", "concurrent")
}

tasks.jacocoTestReport {
  reports {
    xml.required.set(true)
  }
}

pitest {
  junit5PluginVersion.set("1.0.0")
  outputFormats.addAll("XML", "HTML")
  timestampedReports.set(false)
}
