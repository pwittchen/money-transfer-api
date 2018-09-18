package integration;

import com.pwittchen.money.transfer.api.Application;
import io.restassured.RestAssured;
import org.junit.BeforeClass;
import org.junit.Test;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;

/**
 * This test class contains integration tests for the REST API
 */
public class ApplicationTest {

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
  public void shouldRespondWithForbiddenStatus() {
    given()
        .when().get("/")
        .then().statusCode(403);
  }

  @Test
  public void shouldInvokeHealthCheck() {
    given()
        .when().get("/health")
        .then().body(containsString("OK")).statusCode(200);
  }

  @Test
  public void shouldCreateAccount() {
    given()
        .param("name", "testName")
        .and().param("surname", "testSurname")
        .and().param("currency", "EUR")
        .and().param("money", "10.00")
        .when().post("/account")
        .then().body("message", equalTo("account created")).statusCode(200);
  }

  @Test
  public void shouldNotCreateAccountWhenCurrencyIsInvalid() {
    given()
        .param("name", "testName")
        .and().param("surname", "testSurname")
        .and().param("currency", "INVALID")
        .and().param("money", "10.00")
        .when().post("/account")
        .then().body("message", equalTo("Invalid money format")).statusCode(200);
  }

  @Test
  public void shouldNotCreateAccountWhenMoneyIsInvalid() {
    given()
        .param("name", "testName")
        .and().param("surname", "testSurname")
        .and().param("currency", "EUR")
        .and().param("money", "INVALID")
        .when().post("/account")
        .then().body("message", equalTo("Invalid money format")).statusCode(200);
  }

  @Test
  public void shouldNotCreateAccountWhenUserNameIsEmpty() {
    given()
        .param("name", "")
        .and().param("surname", "testSurname")
        .and().param("currency", "EUR")
        .and().param("money", "10.00")
        .when().post("/account")
        .then().body("message", equalTo("User name is empty")).statusCode(200);
  }

  @Test
  public void shouldNotCreateAccountWhenUserSurnameIsEmpty() {
    given()
        .param("name", "testName")
        .and().param("surname", "")
        .and().param("currency", "EUR")
        .and().param("money", "10.00")
        .when().post("/account")
        .then().body("message", equalTo("User surname is empty")).statusCode(200);
  }

  @Test
  public void shouldDeleteAccount() {
    String number = given()
        .param("name", "testName")
        .and().param("surname", "testSurname")
        .and().param("currency", "EUR")
        .and().param("money", "10.00")
        .when().post("/account")
        .then().extract().path("object.number");

    given()
        .formParam("number", number)
        .when()
        .delete("/account")
        .then()
        .body("message", equalTo(
            String.format("account with number %s deleted", number)
        ))
        .statusCode(200);
  }

  @Test
  public void shouldTryToDeleteEmptyAccount() {
    given()
        .param("number", "")
        .when().delete("/account")
        .then().body(
        "message", equalTo("Empty account number")).statusCode(200);
  }

  @Test
  public void shouldGetOneAccount() {
    String number = given()
        .param("name", "testName")
        .and().param("surname", "testSurname")
        .and().param("currency", "EUR")
        .and().param("money", "10.00")
        .when().post("/account")
        .then().extract().path("object.number");

    get("/account/".concat(number)).then().body("number", equalTo(number));
  }

  @Test
  public void shouldNotGetOneAccountIfItDoesNotExist() {
    get("/account/invalid")
        .then().body("message", equalTo("account with id invalid does not exist"));
  }

  @Test
  public void shouldGetAllAccounts() {
    String number = given()
        .param("name", "testName")
        .and().param("surname", "testSurname")
        .and().param("currency", "EUR")
        .and().param("money", "10.00")
        .when().post("/account")
        .then().extract().path("object.number");

    get("/account")
        .then().body(number.concat(".number"), equalTo(number)).statusCode(200);
  }

  @Test
  public void shouldCommitTransaction() {
    String numberOne = given()
        .param("name", "testName1")
        .and().param("surname", "testSurname1")
        .and().param("currency", "EUR")
        .and().param("money", "100.00")
        .when().post("/account")
        .then().extract().path("object.number");

    String numberTwo = given()
        .param("name", "testName2")
        .and().param("surname", "testSurname2")
        .and().param("currency", "EUR")
        .and().param("money", "50.00")
        .when().post("/account")
        .then().extract().path("object.number");

    given()
        .param("from", numberOne)
        .and().param("to", numberTwo)
        .and().param("currency", "EUR")
        .and().param("money", "10.00")
        .when()
        .post("/transaction")
        .then()
        .body(
            "message",
            equalTo("transaction committed")
        )
        .statusCode(200);
  }

  @Test
  public void shouldTryToCommitTransactionForInvalidAccounts() {
    given()
        .param("from", "senderNo")
        .and().param("to", "receiverNo")
        .and().param("currency", "EUR")
        .and().param("money", "10.00")
        .when()
        .post("/transaction")
        .then()
        .body(
            "message",
            equalTo("Trying to transfer money from or to account, which does not exist")
        )
        .statusCode(200);
  }

  @Test
  public void shouldTryToGetOneTransaction() {
    get("/transaction/invalid")
        .then()
        .body("message", equalTo("transaction with id invalid does not exist"));
  }

  @Test
  public void shouldGetAllTransactions() {
    get("/transaction").then().statusCode(200);
  }

  @Test
  public void shouldGetNotFoundStatusForInvalidEndpoint() {
    get("/invalid").then().statusCode(404);
  }
}