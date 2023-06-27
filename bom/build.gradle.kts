plugins {
  `java-platform`
  `shakespeare-publish`
  signing
}

dependencies {
  constraints {
    (parent?.subprojects)
      ?.filter { it != project && it.name != "manual" && it.subprojects.isEmpty() }
      ?.sortedBy { it.name }
      ?.forEach { api(it) }
  }
}

signing {
  setRequired({ !version.toString().endsWith("SNAPSHOT") && gradle.taskGraph.hasTask("publish") })
  useInMemoryPgpKeys(
    findProperty("signingKey")?.toString(),
    findProperty("signingPassword")?.toString())
  sign(publishing.publications[name])
}
