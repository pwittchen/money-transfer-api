# money-transfer-api

[![Build Status](https://img.shields.io/travis/pwittchen/money-transfer-api.svg?branch=master&style=flat-square)](https://travis-ci.org/pwittchen/money-transfer-api) [![codecov](https://img.shields.io/codecov/c/github/pwittchen/money-transfer-api/master.svg?style=flat-square&label=coverage)](https://codecov.io/gh/pwittchen/money-transfer-api/branch/master)

HTTP server with REST API for money transfer between bank accounts

Description of the task can be found in [TASK.md](https://github.com/pwittchen/money-transfer-api/blob/master/TASK.md) file.

Contents
--------
- [Tech stack](#tech-stack)
- [Building and running application](#building-and-running-application)
- [Endpoints](#endpoints)
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

To generate sample data, type:

```
./generate_sample_data.sh
```

Endpoints
---------

### API

Documentation of all endpoints for Open API/Swagger

```
GET /api
```

### Health check

```
GET /health
```

Exemplary curl request:

```
curl -X GET http://localhost:8000/health
```

### Accounts

#### Creating account

```
POST /account
```

form params: `name`, `surname`, `currency` , `money`

Exemplary curl request:

```
curl -X POST \
  http://localhost:8000/account \
  -F name=John \
  -F surname=Doe \
  -F currency=EUR \
  -F money=100.00
```

#### Deleting account

```
DELETE /account/{id}
```

path param: `id`

Exemplary curl request:

```
curl -X DELETE http://localhost:8000/account/f1ba2431-8aae-495b-bffe-0c76ea4357e7
```

#### Getting one account

```
GET /account/{id}
```

path param: `id`

Exemplary curl request:

```
curl -X GET http://localhost:8000/account/03732e1a-0c5b-4818-86f7-e6adca4d0ed8
```

#### Getting all accounts

```
GET /account
```

Exemplary curl request:

```
curl -X GET http://localhost:8000/account
```

### Transactions

#### Committing transaction

```
POST /transaction
```

form params: `from`, `to`, `currency`, `money`

Exemplary curl request:

```
curl -X POST \
  http://localhost:8000/transaction \
  -F from=25b9dae9-abac-42c5-9c21-67e96033c7c3 \
  -F to=620e3ec8-a352-49cb-be06-52704321a657 \
  -F currency=EUR \
  -F money=10.00
```

#### Getting one transaction

```
GET /transaction/{id}
```

path param: `id`

Exemplary curl request:

```
curl -X GET http://localhost:8000/transaction/5eaadd76-8faf-48c7-bc72-5cdec35a385e
```

#### Getting all transactions

```
GET /transaction
```

Exemplary curl request:

```
curl -X GET http://localhost:8000/transaction
```

Tests
-----

In order to execute **unit tests**, run the following command:

```
./gradlew test
```

In order to execute **integration tests** written with REST Assured, execute:

```
./gradlew test -Dtest.profile=integration
```

In order to generate test coverage report, run the following command:

```
./gradlew test jacocoTestReport
```

Generated test report can be found in `build/reports/jacoco/` directory

Code style
----------

Code style used in the project is called `Square` from [Java Code Styles repository by Square](https://github.com/square/java-code-styles).
