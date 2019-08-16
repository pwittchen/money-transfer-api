# money-transfer-api

[![Build Status](https://img.shields.io/travis/pwittchen/money-transfer-api.svg?branch=master&style=flat-square)](https://travis-ci.org/pwittchen/money-transfer-api) [![codecov](https://img.shields.io/codecov/c/github/pwittchen/money-transfer-api/master.svg?style=flat-square&label=coverage)](https://codecov.io/gh/pwittchen/money-transfer-api/branch/master)

HTTP server with REST API for money transfer between bank accounts

Description of the task can be found in [TASK.md](https://github.com/pwittchen/money-transfer-api/blob/master/TASK.md) file.

Contents
--------
- [Building and running the application](#building-and-running-the-application)
- [API documentation](#api-documentation)
- [Tests](#tests)
- [Code style](#code-style)

Building and running the application
------------------------------------

Please note: If we're on Windows, use `gradlew.bat` instead of `./gradlew` script

- To build an application, execute: `./gradlew build`
- To start an application on port `8000`, execute: `./gradlew run`
- To build an application as a "fat jar", execute: `./gradlew shadowJar`
- To start an application as a "fat jar" on port `8000`, execute: `java -jar build/libs/app-1.0-SNAPSHOT-all.jar`
- To generate a sample data in the running application, execute: `./gradlew sampleData` (if you are Windows user, install `curl` and run contents from `sampledata.sh` script manually)

API documentation
-----------------

- Start Docker Daemon
- Pull ReDoc image: `sudo docker pull redocly/redoc`
- Start application
- Run ReDoc: `./gradlew redoc` (if you're Windows user, run contents from the `redoc.sh` script manually)
- Open website with documentation in the web browser: `http://localhost:9000`
- Moreover, API is also documented by tests in `RestApiIntegrationTest` class

Tests
-----

- running unit tests: `./gradlew test`
- running integration tests: `./gradlew test -Dtest.profile=integration`
- creating test coverage report: `./gradlew test jacocoTestReport` (report will be in `build/reports/jacoco/` dir)

Code style
----------

Code style used in the project is called `Square` from [Java Code Styles repository by Square](https://github.com/square/java-code-styles).
