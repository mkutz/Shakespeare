plugins {
  `java-library`
  jacoco
}

java {
  withJavadocJar()
  withSourcesJar()
  toolchain {
    languageVersion.set(JavaLanguageVersion.of(17))
  }
}
