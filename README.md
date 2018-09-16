# money-transfer-api
HTTP server with REST API for money transfer between bank accounts

Contents
--------
- [Usage](#usage)
- [Tech stack](#tech-stack)
- [Building and running application](#building-and-running-application)
- [Tests](#tests)
- [Code style](#code-style)
- [Static code analysis](#static-code-analysis)

Usage
-----

TBD.

Tech stack
----------

Tech stack used in this project is as follows:

- **Application**: Java 8, Gradle, Javalin, Slf4J, Dagger, Joda Money, RxJava, Gson
- **Tests**: JUnit, Truth, Mockito, REST Assured

Building and running application
--------------------------------

To build application, execute:

```
./gradlew build
```

To start application, execute:

```
./gradlew run
```

### Fat jar

To generate fat jar file with all dependencies, execute:

```
./gradlew shadowJar
```

Assuming you have executed command above, to run server as a standalone fat jar, execute:

```
java -jar build/libs/app-1.0-all.jar
```

Tests
-----

In order to execute tests run the following command:

```
./gradlew test
```

In order to generate test coverage report run the following command:

```
./gradlew test jacocoTestReport
```

Generated test can be found in `build/reports/jacoco/` directory

Code style
----------

Code style used in the project is called `Square` from [Java Code Styles repository by Square](https://github.com/square/java-code-styles).

Static code analysis
--------------------

Static code analysis runs CheckStyle, PMD and FindBugs. It can be executed with command:

```
./gradlew check
```