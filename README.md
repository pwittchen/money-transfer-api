# money-transfer-api
HTTP server with REST API for money transfer between bank accounts

Contents
--------
- [Usage](#usage)
- [Tech Stack](#tech-stack)
- [Building and running application](#building-and-running-application)
- [Tests](#tests)
- [Code Style](#code-style)
- [Static Code Analysis](#static-code-analysis)

Usage
-----

TBD.

Tech Stack
----------

Tech Stack used in this project is as follows:

- **Application**: Java 8, Gradle, Javalin, Slf4J, Dagger, Joda Money, RxJava, Gson
- **Tests**: JUnit, Truth, Mockito, REST Assured

Building and running application
--------------------------------

To build application, execute::

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

Assuming you have generated fat jar, to run server as a standalone fat jar, execute:

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

Code Style
----------

Code style used in the project is called `Square` from [Java Code Styles repository by Square](https://github.com/square/java-code-styles).

Static Code Analysis
--------------------

Static Code Analysis runs CheckStyle, PMD and FindBugs. It can be executed with command:

```
./gradlew check
```