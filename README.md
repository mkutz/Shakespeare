# Shakespeare [![Build](https://github.com/mkutz/shakespeare/actions/workflows/build.yml/badge.svg)](https://github.com/mkutz/shakespeare/actions/workflows/build.yml)

A framework helping to write tests like screenplays.
It is based on the ideas from [Page Objects Refactored] by Antony Marcano, Andy Palmer & John Ferguson Smart, with Jan Molak.

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

There is probably no reason to use this module directly as all other modules make this available transitively.

### [`selenium`](modules/selenium)

Allows automating browsers using [Selenium WebDriver](https://www.selenium.dev/documentation/en/webdriver/) via the [BrowseTheWeb Ability](modules/selenium/src/main/java/org/shakespeareframework/selenium/BrowseTheWeb.java).

It also provides some [WebDriverSupplier](modules/selenium/src/main/java/org/shakespeareframework/selenium/WebDriverSupplier.java) implementations to handle typical WebDriver setup scenarios:

- [LocalWebDriverSupplier](modules/selenium/src/main/java/org/shakespeareframework/selenium/LocalWebDriverSupplier.java)
- [DockerWebDriverSupplier](modules/selenium/src/main/java/org/shakespeareframework/selenium/DockerWebDriverSupplier.java)


[Page Objects Refactored]: <https://ideas.riverglide.com/page-objects-refactored-12ec3541990#.ekkiguobe>
