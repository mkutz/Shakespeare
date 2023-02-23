plugins {
  `shakespeare-module`
  `shakespeare-publish`
  `shakespeare-style`
}

repositories { mavenCentral() }

dependencies {
  api(project(":modules:core"))

  testImplementation(platform("org.junit:junit-bom:5.9.2"))
  testImplementation("org.junit.jupiter:junit-jupiter-api")
  testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")

  testImplementation("org.assertj:assertj-core:3.24.2")
}

/*publishing {
   publications {
     create<MavenPublication>() {
       pom {
         name.set("Shakespeare Test Utils")
         description.set("Module with utils for testing other Shakespeare modules.")
       }
     }
   }
 }
 */
