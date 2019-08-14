# money-transfer-api

[![Build Status](https://img.shields.io/travis/pwittchen/money-transfer-api.svg?branch=master&style=flat-square)](https://travis-ci.org/pwittchen/money-transfer-api) [![codecov](https://img.shields.io/codecov/c/github/pwittchen/money-transfer-api/master.svg?style=flat-square&label=coverage)](https://codecov.io/gh/pwittchen/money-transfer-api/branch/master)

HTTP server with REST API for money transfer between bank accounts

Description of the task can be found in [TASK.md](https://github.com/pwittchen/money-transfer-api/blob/master/TASK.md) file.

Contents
--------
- [Tech stack](#tech-stack)
- [Building and running application](#building-and-running-application)
- [API](#api)
- [Tests](#tests)
- [Code style](#code-style)

Tech stack
----------

Tech stack used in this project is as follows:

- **Application**: Java 11, [Gradle](https://gradle.org/), [Javalin](https://javalin.io), [Slf4J](https://www.slf4j.org/), [Dagger](https://github.com/google/dagger), [Joda Money](http://www.joda.org/joda-money/), [Gson](https://github.com/google/gson)
- **Tests**: [JUnit](https://junit.org/), [Truth](https://github.com/google/truth), [Mockito](https://github.com/mockito/mockito), [Concurrent Unit](https://github.com/jhalterman/concurrentunit), [REST Assured](https://github.com/rest-assured/rest-assured)

Building and running application
--------------------------------

Please note: If you're on Windows, use `gradlew.bat` instead of `./gradlew` script

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

Server will start running on port `8000`

### Sample data

To generate sample data with two accounts, type: `./gradlew sampleData`

API
---

- To view API documentation, start application and run ReDoc: `./gradlew redoc`
- Now, we can open the following address in web browser: `http://localhost:9000`
- Moreover, API is also documented in `RestApiIntegrationTest` class

Tests
-----

- running unit tests: `./gradlew test`
- running integration tests: `./gradlew test -Dtest.profile=integration`
- creating test coverage report: `./gradlew test jacocoTestReport`

Generated test report can be found in `build/reports/jacoco/` directory

Code style
----------

Code style used in the project is called `Square` from [Java Code Styles repository by Square](https://github.com/square/java-code-styles).
