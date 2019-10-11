package integration;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pwittchen.money.transfer.api.Application;
import com.pwittchen.money.transfer.api.model.Account;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import java.util.List;
import org.eclipse.jetty.http.HttpStatus;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.junit.BeforeClass;
import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;
import static io.restassured.RestAssured.delete;
import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;

public class RestApiIntegrationTest {

  @BeforeClass public static void setUp() {
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

  @Test public void shouldCreateAccount() {
    given()
        .param("owner", "testOwner")
        .and().param("currency", "EUR")
        .and().param("money", "10.00")
        .when().post("/account")
        .then()
        .statusCode(HttpStatus.OK_200);
  }

  @Test public void shouldNotCreateAccountWhenCurrencyIsInvalid() {
    given()
        .param("owner", "testOwner")
        .and()
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

  @Test public void shouldNotCreateAccountWhenMoneyIsInvalid() {
    given()
        .param("owner", "testOwner")
        .and().param("currency", "EUR")
        .and().param("money", "INVALID")
        .when().post("/account")
        .then().body(equalTo("\"Invalid money format\""))
        .statusCode(HttpStatus.BAD_REQUEST_400);
  }

  @Test public void shouldNotCreateAccountWhenOwnerIsEmpty() {
    given()
        .param("owner", "")
        .and().param("currency", "EUR")
        .and().param("money", "10.00")
        .when().post("/account")
        .then().body(equalTo("\"Account owner is empty\""))
        .statusCode(HttpStatus.BAD_REQUEST_400);
  }

  @Test public void shouldTryToDeleteEmptyAccount() {
    delete("/account").then().statusCode(HttpStatus.NOT_FOUND_404);
  }

  @Test public void shouldGetAllAccounts() {
    String number = given()
        .param("owner", "testOwner")
        .and().param("currency", "EUR")
        .and().param("money", "10.00")
        .when().post("/account")
        .then()
        .extract().path("value.number");

    get("/account")
        .then()
        .body(containsString(number))
        .statusCode(HttpStatus.OK_200);
  }

  @Test public void shouldCommitTransaction() {
    String numberOne = given()
        .param("owner", "testOwner1")
        .and().param("currency", "EUR")
        .and().param("money", "100.00")
        .when().post("/account")
        .then()
        .extract().path("value.number");

    String numberTwo = given()
        .param("owner", "testOwner2")
        .and().param("currency", "EUR")
        .and().param("money", "50.00")
        .when().post("/account")
        .then()
        .extract().path("value.number");

    given()
        .param("from", numberOne)
        .and().param("to", numberTwo)
        .and().param("currency", "EUR")
        .and().param("money", "10.00")
        .when()
        .post("/transaction")
        .then()
        .statusCode(HttpStatus.OK_200);

    Response response = get("/account");

    List<Account> accounts = new Gson().fromJson(
        response.asString(), new TypeToken<List<Account>>() {
        }.getType()
    );

    accounts
        .stream()
        .filter(account -> account.number().equals(numberOne))
        .findFirst()
        .ifPresent(
            account -> assertThat(account.money()).isEqualTo(Money.of(CurrencyUnit.EUR, 90))
        );

    accounts
        .stream()
        .filter(account -> account.number().equals(numberTwo))
        .findFirst()
        .ifPresent(
            account -> assertThat(account.money()).isEqualTo(Money.of(CurrencyUnit.EUR, 60))
        );
  }

  @Test public void shouldTryToCommitTransactionFromInvalidAccount() {
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

  @Test public void shouldTryToCommitTransactionToInvalidAccount() {
    String senderAccountNumber = given()
        .param("owner", "testOwner1")
        .and().param("currency", "EUR")
        .and().param("money", "100.00")
        .when().post("/account")
        .then()
        .extract().path("value.number");

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

  @Test public void shouldGetAllTransactions() {
    String numberOne = given()
        .param("owner", "testOwner1")
        .and().param("currency", "EUR")
        .and().param("money", "100.00")
        .when().post("/account")
        .then().extract().path("value.number");

    String numberTwo = given()
        .param("owner", "testOwner2")
        .and().param("currency", "EUR")
        .and().param("money", "50.00")
        .when().post("/account")
        .then().extract().path("value.number");

    String id = given()
        .param("from", numberOne)
        .and().param("to", numberTwo)
        .and().param("currency", "EUR")
        .and().param("money", "10.00")
        .when()
        .post("/transaction")
        .then().extract().path("id");

    get("/transaction")
        .then().body(containsString(id))
        .statusCode(HttpStatus.OK_200);
  }

  @Test public void shouldGetNotFoundStatusForInvalidEndpoint() {
    get("/invalid").then().statusCode(HttpStatus.NOT_FOUND_404);
  }
}