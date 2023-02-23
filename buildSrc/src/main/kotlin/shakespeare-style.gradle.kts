plugins { id("com.diffplug.spotless") }

spotless {
  format("misc") {
    target("**/*.md", "**/*.xml", "**/*.yml", "**/*.yaml", ".gitignore")
    targetExclude("**/build/**/*", "**/.idea/**")
    trimTrailingWhitespace()
    endWithNewline()
    indentWithSpaces(2)
  }

  format("prettier") {
    target("**/*.xml", "**/*.svg", "**/*.yml", "**/*.yaml", "**/*.json")
    targetExclude("**/build/**/*", "**/.idea/**")
    //prettier().config(tabWidth to 2)
  }

  java {
    target("**/.java")
    targetExclude("**/build/**/*")
    googleJavaFormat().reflowLongStrings()
    removeUnusedImports()
    indentWithSpaces(2)
  }

  kotlinGradle {
    target("**/*.gradle.kts")
    targetExclude("**/build/**/*.gradle.kts")
    ktfmt().googleStyle()
  }

  freshmark {
    target("*.md")
  }
}
