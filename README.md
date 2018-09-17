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

Server will start running on port `8000`

Endpoints
---------

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
DELETE /account
```

form params: `number`

Exemplary curl request:

```
curl -X DELETE \
  http://localhost:8000/account \
  -F id=f1ba2431-8aae-495b-bffe-0c76ea4357e7
```

#### Getting one account

```
GET /account/{number}
```

path params: `number`

Exemplary curl request:

```
curl -X GET \
  http://localhost:8000/account/03732e1a-0c5b-4818-86f7-e6adca4d0ed8
```

#### Getting all accounts

```
GET /account
```

Exemplary curl request:

```
curl -X GET \
  http://localhost:8000/account
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

path params: `id`

Exemplary curl request:

```
curl -X GET \
  http://localhost:8000/transaction/5eaadd76-8faf-48c7-bc72-5cdec35a385e
```

#### Getting all transactions

```
GET /transaction
```

Exemplary curl request:

```
curl -X GET \
  http://localhost:8000/transaction
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