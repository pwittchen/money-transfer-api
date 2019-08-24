package integration;

import com.pwittchen.money.transfer.api.Application;
import io.restassured.RestAssured;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.BeforeClass;
import org.junit.Test;

import static io.restassured.RestAssured.delete;
import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;

public class RestApiIntegrationTest {

  @BeforeClass
  public static void setUp() {
    configureHost();
    configurePort();
    configureBasePath();
    startServer();
  }

  private static void configureHost() {
    String baseHost = System.getProperty("server.host");
    if (baseHost == null) {
      baseHost = "http://localhost";
    }
    RestAssured.baseURI = baseHost;
  }

  private static void configurePort() {
    String port = System.getProperty("server.port");
    if (port == null) {
      RestAssured.port = Integer.parseInt("8000");
    } else {
      RestAssured.port = Integer.parseInt(port);
    }
  }

  private static void configureBasePath() {
    String basePath = System.getProperty("server.base");
    if (basePath == null) {
      basePath = "/";
    }
    RestAssured.basePath = basePath;
  }

  private static void startServer() {
    Application.main(new String[] {});
  }

  @Test
  public void shouldCreateAccount() {
    given()
        .param("name", "testName")
        .and().param("surname", "testSurname")
        .and().param("currency", "EUR")
        .and().param("money", "10.00")
        .when().post("/account")
        .then()
        .statusCode(HttpStatus.OK_200);
  }

  @Test
  public void shouldNotCreateAccountWhenCurrencyIsInvalid() {
    given()
        .param("name", "testName")
        .and()
        .param("surname", "testSurname")
        .and()
        .param("currency", "INVALID")
        .and()
        .param("money", "10.00")
        .when()
        .post("/account")
        .then()
        .body(equalTo("\"Invalid money format\""))
        .statusCode(HttpStatus.BAD_REQUEST_400);
  }

  @Test
  public void shouldNotCreateAccountWhenMoneyIsInvalid() {
    given()
        .param("name", "testName")
        .and().param("surname", "testSurname")
        .and().param("currency", "EUR")
        .and().param("money", "INVALID")
        .when().post("/account")
        .then().body(equalTo("\"Invalid money format\""))
        .statusCode(HttpStatus.BAD_REQUEST_400);
  }

  @Test
  public void shouldNotCreateAccountWhenUserNameIsEmpty() {
    given()
        .param("name", "")
        .and().param("surname", "testSurname")
        .and().param("currency", "EUR")
        .and().param("money", "10.00")
        .when().post("/account")
        .then().body(equalTo("\"User name is empty\""))
        .statusCode(HttpStatus.BAD_REQUEST_400);
  }

  @Test
  public void shouldNotCreateAccountWhenUserSurnameIsEmpty() {
    given()
        .param("name", "testName")
        .and()
        .param("surname", "")
        .and()
        .param("currency", "EUR")
        .and()
        .param("money", "10.00")
        .when()
        .post("/account")
        .then()
        .body(equalTo("\"User surname is empty\""))
        .statusCode(HttpStatus.BAD_REQUEST_400);
  }

  @Test
  public void shouldDeleteAccount() {
    String number = given()
        .param("name", "testName")
        .and().param("surname", "testSurname")
        .and().param("currency", "EUR")
        .and().param("money", "10.00")
        .when().post("/account")
        .then().extract().path("value.number");

    delete("/account/".concat(number))
        .then()
        .body(equalTo("\"account with number " + number + " deleted\""))
        .statusCode(HttpStatus.OK_200);
  }

  @Test
  public void shouldTryToDeleteInvalidAccount() {
    delete("/account/invalid")
        .then().body(equalTo("\"Account with number invalid does not exist\""))
        .statusCode(HttpStatus.BAD_REQUEST_400);
  }

  @Test
  public void shouldTryToDeleteEmptyAccount() {
    delete("/account").then().statusCode(HttpStatus.NOT_FOUND_404);
  }

  @Test
  public void shouldGetOneAccount() {
    String number = given()
        .param("name", "testName")
        .and().param("surname", "testSurname")
        .and().param("currency", "EUR")
        .and().param("money", "10.00")
        .when().post("/account")
        .then().extract().path("value.number");

    get("/account/".concat(number)).then().statusCode(HttpStatus.OK_200);
  }

  @Test
  public void shouldNotGetOneAccountIfItDoesNotExist() {
    get("/account/invalid")
        .then().body(equalTo("\"account with id invalid does not exist\""))
        .statusCode(HttpStatus.NOT_FOUND_404);
  }

  @Test
  public void shouldGetAllAccounts() {
    String number = given()
        .param("name", "testName")
        .and().param("surname", "testSurname")
        .and().param("currency", "EUR")
        .and().param("money", "10.00")
        .when().post("/account")
        .then().extract().path("value.number");

    get("/account")
        .then().body(containsString(number))
        .statusCode(HttpStatus.OK_200);
  }

  @Test
  public void shouldCommitTransaction() {
    String numberOne = given()
        .param("name", "testName1")
        .and().param("surname", "testSurname1")
        .and().param("currency", "EUR")
        .and().param("money", "100.00")
        .when().post("/account")
        .then().extract().path("value.number");

    String numberTwo = given()
        .param("name", "testName2")
        .and().param("surname", "testSurname2")
        .and().param("currency", "EUR")
        .and().param("money", "50.00")
        .when().post("/account")
        .then().extract().path("value.number");

    given()
        .param("from", numberOne)
        .and().param("to", numberTwo)
        .and().param("currency", "EUR")
        .and().param("money", "10.00")
        .when()
        .post("/transaction")
        .then()
        .statusCode(HttpStatus.OK_200);
  }

  @Test
  public void shouldTryToCommitTransactionFromInvalidAccount() {
    given()
        .param("from", "invalidSenderNo")
        .and().param("to", "invalidReceiverNo")
        .and().param("currency", "EUR")
        .and().param("money", "10.00")
        .when()
        .post("/transaction")
        .then()
        .body(equalTo("\"Account with number invalidSenderNo does not exist\""))
        .statusCode(HttpStatus.BAD_REQUEST_400);
  }

  @Test
  public void shouldTryToCommitTransactionToInvalidAccount() {
    String senderAccountNumber = given()
        .param("name", "testName1")
        .and().param("surname", "testSurname1")
        .and().param("currency", "EUR")
        .and().param("money", "100.00")
        .when().post("/account")
        .then().extract().path("value.number");

    given()
        .param("from", senderAccountNumber)
        .and().param("to", "invalidReceiverNo")
        .and().param("currency", "EUR")
        .and().param("money", "10.00")
        .when()
        .post("/transaction")
        .then()
        .body(equalTo("\"Account with number invalidReceiverNo does not exist\""))
        .statusCode(HttpStatus.BAD_REQUEST_400);
  }

  @Test
  public void shouldGetOneTransaction() {
    String numberOne = given()
        .param("name", "testName1")
        .and().param("surname", "testSurname1")
        .and().param("currency", "EUR")
        .and().param("money", "100.00")
        .when().post("/account")
        .then().extract().path("value.number");

    String numberTwo = given()
        .param("name", "testName2")
        .and().param("surname", "testSurname2")
        .and().param("currency", "EUR")
        .and().param("money", "50.00")
        .when().post("/account")
        .then().extract().path("value.number");

    String transactionId = given()
        .param("from", numberOne)
        .and().param("to", numberTwo)
        .and().param("currency", "EUR")
        .and().param("money", "10.00")
        .when()
        .post("/transaction")
        .then()
        .extract().path("id");

    get("/transaction/".concat(transactionId))
        .then()
        .body("id", equalTo(transactionId));
  }

  @Test
  public void shouldTryToGetOneTransactionForInvalidId() {
    get("/transaction/invalid")
        .then()
        .body(equalTo("\"transaction with id invalid does not exist\""));
  }

  @Test
  public void shouldGetAllTransactions() {
    get("/transaction").then().statusCode(HttpStatus.OK_200);
  }

  @Test
  public void shouldGetNotFoundStatusForInvalidEndpoint() {
    get("/invalid").then().statusCode(HttpStatus.NOT_FOUND_404);
  }
}