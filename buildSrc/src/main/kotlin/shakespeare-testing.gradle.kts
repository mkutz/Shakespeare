plugins {
  jacoco
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
