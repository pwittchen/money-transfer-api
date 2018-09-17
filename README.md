# money-transfer-api
HTTP server with REST API for money transfer between bank accounts

Contents
--------
- [Tech stack](#tech-stack)
- [Building and running application](#building-and-running-application)
- [Endpoints](#endpoints)
- [Tests](#tests)
- [Code style](#code-style)
- [Static code analysis](#static-code-analysis)

Tech stack
----------

Tech stack used in this project is as follows:

- **Application**: Java 8, [Gradle](https://gradle.org/), [Javalin](https://javalin.io), [Slf4J](https://www.slf4j.org/), [Dagger](https://github.com/google/dagger), [Joda Money](http://www.joda.org/joda-money/), [Gson](https://github.com/google/gson)
- **Tests**: [JUnit](https://junit.org/), [Truth](https://github.com/google/truth), [Mockito](https://github.com/mockito/mockito), [REST Assured](https://github.com/rest-assured/rest-assured)

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

Endpoints
---------

### Accounts

#### Creating account

...

#### Deleting account

...

#### Getting one account

...

#### Getting all accounts

### Transactions

#### Committing transaction

...

### Getting one transaction

...

### Getting all transactions

...

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

Generated test report can be found in `build/reports/jacoco/` directory

Code style
----------

Code style used in the project is called `Square` from [Java Code Styles repository by Square](https://github.com/square/java-code-styles).

Static code analysis
--------------------

Static code analysis runs CheckStyle, PMD and FindBugs. It can be executed with command:

```
./gradlew check
```