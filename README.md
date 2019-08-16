# money-transfer-api

[![Build Status](https://img.shields.io/travis/pwittchen/money-transfer-api.svg?branch=master&style=flat-square)](https://travis-ci.org/pwittchen/money-transfer-api) [![codecov](https://img.shields.io/codecov/c/github/pwittchen/money-transfer-api/master.svg?style=flat-square&label=coverage)](https://codecov.io/gh/pwittchen/money-transfer-api/branch/master)

HTTP server with REST API for money transfer between bank accounts

Description of the task can be found in [TASK.md](https://github.com/pwittchen/money-transfer-api/blob/master/TASK.md) file.

Contents
--------
- [Building and running application](#building-and-running-application)
- [API documentation](#api-documentation)
- [Tests](#tests)
- [Code style](#code-style)

Building and running application
--------------------------------

Please note: If we're on Windows, use `gradlew.bat` instead of `./gradlew` script

- To build application, execute: `./gradlew build`
- To start application, execute: `./gradlew run`

### Fat jar

To generate fat jar file with all dependencies, execute: `./gradlew shadowJar`

Assuming we have executed command above, to run server as a standalone fat jar, execute:

```
java -jar build/libs/app-1.0-SNAPSHOT-all.jar
```

Server will start running on port `8000`

### Sample data

To generate sample data with two accounts, type: `./gradlew sampleData`

API documentation
-----------------

- Start Docker Daemon and type `sudo docker pull redocly/redoc`
- To view API documentation, start application and run ReDoc: `./gradlew redoc`
- Now, we can open the following address in web browser: `http://localhost:9000`
- Moreover, API is also documented by tests in `RestApiIntegrationTest` class

Tests
-----

- running unit tests: `./gradlew test`
- running integration tests: `./gradlew test -Dtest.profile=integration`
- creating test coverage report: `./gradlew test jacocoTestReport` (report will be in `build/reports/jacoco/` dir)

Code style
----------

Code style used in the project is called `Square` from [Java Code Styles repository by Square](https://github.com/square/java-code-styles).
