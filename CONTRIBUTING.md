# Contributing to Shakespeare

First, thank you so much for taking the time to contribute! 🙏

## Code of Conduct

Please note that this project has a [code of conduct](CODE_OF_CONDUCT.md).
By contributing, you are expected to uphold this code.

## How to Contribute

### Reporting Bugs

Before you report a new bug, please check [the current list of issues](https://github.com/mkutz/shakespeare/issues) to avoid creating duplicates.
If your issue is already reported, please feel free to add additional information to it.

When you [create a new issue](https://github.com/mkutz/shakespeare/issues/new/choose), please try to include the following information:

1. Describe the problem short and concise.
   What happens? What did you expect to happen?
2. List your tech stack.
   - Which version of Shakespeare? Which modules?
   - Which JDK?
   - If you code in something different from Java, which language in what version?
   - Which operating system?
   - Which build tool?
3. Provide information on how to reproduce the problem.
   Best provide a minimal, complete and verifiable example (see [Stackoverflow's Guidelines](https://stackoverflow.com/help/minimal-reproducible-example)).
   E.g. a JUnit test case.

### Suggest new Features

If you like to see a new feature in Shakespeare, please first check if the idea is already being discussed in the [current discussions](https://github.com/mkutz/shakespeare/discussions) and rather upvote it instead of creating a new one.
Otherwise, please feel free to [open a new discussion](https://github.com/mkutz/shakespeare/discussions/new).

Please add the following information in your suggestion:

1. _Why_ do you think the feature is a good addition to Shakespeare?
2. _What_ should be changed, added or removed?
3. (Optional) _How_ do you suggest implementing the feature?

When your idea is thoroughly discussed and accepted, an [issue](https://github.com/mkutz/shakespeare/issues) will be created.

Please avoid the temptation to work on your feature in the meantime.
It is not impossible that we disagree on the need for the feature.
It is not unlikely that somebody comes up with a different idea to achieve the same thing.
It is likely that there is another way to implement the feature.

### Improve Documentation

If you find any smaller piece of documentation in the [manual](https://shakespeareframework.org/current/manual/) that you'd like to change, please feel free to open a pull request.
Please mind the chapter on the [documentation guidelines below](#documentation).

For bigger changes or if you're not certain, please follow the same process as described [above](#suggest-new-features).

### Providing Code

If you want to provide code, please make sure that there's a consensus on the feature was reached (see [above](#suggest-new-features)) and read the following chapters on [project knowledge](#project-knowledge).

After you're done, please [open a new pull request](https://github.com/mkutz/shakespeare/compare) and wait for a review by a maintainer.
 
## Project Knowledge

In oder to prevent avoidable conflicts in pull requests, please read this in case you want to contribute any code to Shakespeare.

### Commit Messages

Each commit message should follow the guidelines in @cbeams article on [How to Write a Git Commit Message](https://cbea.ms/git-commit/).

Each commit message

- is written in present tense ("Add something", not "Added something),
- uses imperative mood ("Move stuff from A to B", not "Moves stuff from A to B"),
- limits the first line (the subject) to 50 characters,
- starts with a capital letter,
- does not end the subject line with a period,
- separates subject from the body (third line and following) with a blank line,
- put any references to issues, pull requests or external links in the body,
- wraps the body after 72 characters (exceptions for URLs or similar),
- explains in the body _what_ and _why_, not _how_.

### Building

The project uses [Gradle](https://gradle.org) for building.
To verify your changes, it should generally be enough to execute

```shell
./gradlew build
```

in the project's root folder.

As continuous integration and deployment tool, the project uses [GitHub Actions](https://github.com/features/actions).
All pipelines can be found in [.github/workflows](.github/workflows).

### Versioning

Shakespeare is using [SemVer 2.0](https://semver.org/spec/v2.0.0.html) but omits the patch digit in case it is 0.

### Project Structure

It is structured in four main modules:

- The [modules](modules) directory contains all the published code modules:
  - [core](modules/core) contains the code for basic elements (e.g. Agent, Ability, Task, Question, Fact),
  - [selenium](modules/selenium) contains the Selenium module for web testing,
  - [retrofit](modules/retrofit) contains the Retrofit module for HTTP API testing,
  - [testutils](modules/testutils) contains some utility classes which might be useful for testing (e.g. build ad-hoc test Tasks),
- the [bom](bom) directory contains the build file to generate a [Maven Bill of Material (BOM)](https://maven.apache.org/guides/introduction/introduction-to-dependency-mechanism.html#bill-of-materials-bom-poms) for all the Shakespeare modules, and
- the [manual](manual) directory contains the projects documentation written in [AsciiDoc](https://docs.asciidoctor.org/asciidoc/latest/).

There's also the [buildSrc](buildSrc) directory, which contains some shared build logic (see [Gradle documentation](https://docs.gradle.org/current/userguide/organizing_gradle_projects.html#sec:build_sources)).

### Packages

The main package of the project is `org.shakespeareframework`.
Only the [core](modules/core) module may use this package directly.
New modules should generally have an own root package below, e.g. `org.shakespeareframework.selenium`.

Packages are supposed to contain things that belong to the same domain, not the same type.
E.g. a sub package `tasks` that contains only Task classes is _not_ compliant to this.

### Immutability

In order to minimize side effects, all classes in the framework should be as immutable as possible.

Hence, wherever possible 

- class fields should be declared `final`,
- local variable should be declared `final`,
- fields should be initialized in the constructor or class body.

### Visibility

In order to prevent confusion, only API elements that are required by a user should be declared `public`.
Other internally used elements should have the lowest possible visibility:

- internally used classes should be package-private (default visibility),
- fields in `public` classes should be declared `private`,
- only abstract classes should declare `protected` fields.

### Local Variables

Local variables should be declared using the `var` keyword.

### Code Style

The project is written in Java 11 and uses [Spotless](https://github.com/diffplug/spotless) to format the code according to the [Google Java Style](https://google.github.io/styleguide/javaguide.html).

Generally files all code files should

- use two space indentation,
- end with a new line

Please follow [these instructions](https://github.com/google/google-java-format#intellij-android-studio-and-other-jetbrains-ides) to configure your IDE.

To reformat the code, please run

```shell
./gradlew spotlessApply
```

before committing.

### Tests

#### Stack

All tests are written in [JUnit 5](https://junit.org/junit5/) and use exclusively [AssertJ](https://assertj.github.io/doc/) for assertions and assumptions.
Tests may also be declared [parametrized](https://junit.org/junit5/docs/current/user-guide/#writing-tests-parameterized-tests), preferably with a `@ValueSource` or an `@EnumSource`.
Where useful tests may also declare properties using [jqwik](https://jqwik.net/).

#### Assertions

Each test must contain at least one assertion.

#### Test Code Style

In order to keep tests concise, their code should be compact and short.
Hence, within tests,

- all fields should not have any visibility modifiers,
- local variables should not be declared final,
- parameters should not be declared final.

#### Self-Sufficient

In order to make tests easy to analyse and understand, all information needed should be visible from the test code:

- non-trivial setup steps should be kept in the test itself and
- usage of `@BeforeAll`, `@BeforeEach`, `@AfterEach`, `@AfterAll` and similar should be kept to a technically necessary minimum (e.g. staring/stopping of doubles).

#### Test Method Names & Display Names

In order to avoid confusion due to out-dated method names,

- test method names should for simple classes should be numbered (`test1`, `test2`, …) or start with the method name for more complex classes (e.g. `toStringTest1`, `createStuffTest2`).

In order to keep test reports concise and useful, tests must provide a display name (e.g. via `@DisplayName`) that should

- be written in present tense (e.g. `createStuff throws InvalidStateException closed` instead of `createStuff will throw an InvalidStateException closed`),
- start with the name of the method under test (e.g. `createStuff throws InvalidStateException closed` instead of `throws InvalidStateException in createStuff when closed`),
- avoid trivial words like `should` or `does` (e.g. `createStuff throws InvalidStateException closed` instead of `createStuff should throw InvalidStateException closed`),
- not state default behaviour, but only exceptions form it (e.g. `toString` instead of `toString returns a human readable string`),
- not exceed 50 characters.

General pattern: `<what is under test>` `<exceptional state under test if any>` `<exceptional expectation if any>`

#### Dependencies

In order to avoid dependencies between tests,

- tests should be kept self-sufficient,
- tests may not rely on tear down steps (e.g. `@AfterEach`, `@AfterAll`) to run but must ensure the required state in their own setup phase (e.g. `@BeforeAll`, `@BeforeEach`), and
- dependencies to external services are to be avoided and their availability must be checked with an assumption.

#### Mocks

Using mocks should be avoided.
Instead, real objects should be used where possible.

If infrastructure is required for a test, a [Nullable Infrastructure component](https://www.jamesshore.com/v2/blog/2018/testing-without-mocks#nullable-infrastructure) should be used instead.

### Documentation

#### Manual

Each feature needs to be described in Shakespeare's [AsciiDoc manual](manual).

In order to make changes in Git less confusing, each sentence in the manual AsciiDoc should end with a linebreak.

All code samples should be included from actual tests in [the manual's tests](manual/src/test).

#### Markdown

In order to make changes in Git less confusing, each sentence in the manual Markdown should end with a linebreak.

#### JavaDoc

There must be JavaDoc for the following:

- each public class requires at least a summary,
- each public method requires a summary, its parameters and return value must be documented.

JavaDoc should follow the following rules:

- always link types (e.g. `@param actor the {@link Actor} performing the {@link Task}`),
- code is marked with `@code` (e.g. `when {@code agent.does(someTask)} is called`),
- summaries are full sentences and end with a `.`,
- parameter and return type documentation is usually not a full sentence and does not end with a `.`,
- thrown RuntimeExceptions should be documented with `@throws` and state the possible reason.
