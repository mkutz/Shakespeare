# Shakespeare
[![Build](https://github.com/mkutz/shakespeare/actions/workflows/build.yml/badge.svg)](https://github.com/mkutz/shakespeare/actions/workflows/build.yml)
[![Sonar Quality Gate](https://img.shields.io/sonar/quality_gate/mkutz_shakespeare?server=https%3A%2F%2Fsonarcloud.io)](https://sonarcloud.io/dashboard?id=mkutz_shakespeare)
[![Sonar Coverage](https://img.shields.io/sonar/coverage/mkutz_shakespeare?server=http%3A%2F%2Fsonarcloud.io)](https://sonarcloud.io/dashboard?id=mkutz_shakespeare)
[![Maven Central](https://img.shields.io/maven-central/v/org.shakespeareframework/core)](https://s01.oss.sonatype.org/content/repositories/releases/org/shakespeareframework/)

A framework helping to write tests like screenplays.
It is based on the ideas from [Page Objects Refactored] by Antony Marcano, Andy Palmer & John Ferguson Smart, with Jan Molak.

## Getting Started

In oder to use Shakespeare you need a JDK 11 or higher.

Add the following dependencies to your build.

Gradle:

```groovy
implementation 'org.shakespeareframework:core:[VERSION]'
```

Maven:

```xml
<dependency>
  <groupId>org.shakespeareframework</groupId>
  <artifactId>core</artifactId>
  <version>[VERSION]</version>
</dependency>
```

After that you should be able to import the Actor class and start using it.

Here's a simple JUnit example:

```java
import org.junit.jupiter.api.Test;

import org.shakespeareframework.Actor;

class MyScreenplay {

    @Test
    void act1() {
        actor.does(they -> System.out.println("Hello World!"));
    }
}
```

## Modules

### [`core`](modules/core)

Contains base classes utilized by all other modules:

- [Actor](modules/core/src/main/java/org/shakespeareframework/Actor.java)
- [Ability](modules/core/src/main/java/org/shakespeareframework/Ability.java)
- [Task](modules/core/src/main/java/org/shakespeareframework/Task.java),
  [RetryableTask](modules/core/src/main/java/org/shakespeareframework/RetryableTask.java)
- [Question](modules/core/src/main/java/org/shakespeareframework/Question.java),
  [RetryableQuestion](modules/core/src/main/java/org/shakespeareframework/RetryableQuestion.java)
- [Fact](modules/core/src/main/java/org/shakespeareframework/Fact.java)

### [`selenium`](modules/selenium)

Allows automating browsers using [Selenium WebDriver](https://www.selenium.dev/documentation/en/webdriver/) via the [BrowseTheWeb Ability](modules/selenium/src/main/java/org/shakespeareframework/selenium/BrowseTheWeb.java).

It also provides some [WebDriverSupplier](modules/selenium/src/main/java/org/shakespeareframework/selenium/WebDriverSupplier.java) implementations to handle typical WebDriver setup scenarios:

- [LocalWebDriverSupplier](modules/selenium/src/main/java/org/shakespeareframework/selenium/LocalWebDriverSupplier.java)
- [DockerWebDriverSupplier](modules/selenium/src/main/java/org/shakespeareframework/selenium/DockerWebDriverSupplier.java)

### [`retrofit`](modules/retrofit)

Allows performing type-safe HTTP API calls using [Retrofit](https://square.github.io/retrofit/) via the [CallHttpApis Ability](modules/retrofit/src/main/java/org/shakespeareframework/retrofit/CallHttpApis.java).

[Page Objects Refactored]: <https://ideas.riverglide.com/page-objects-refactored-12ec3541990#.ekkiguobe>
