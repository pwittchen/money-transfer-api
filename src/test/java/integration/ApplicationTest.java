package integration;

import com.pwittchen.money.transfer.api.Application;
import io.restassured.RestAssured;
import org.junit.BeforeClass;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;

/**
 * This test class contains integration test for the REST API
 */
public class ApplicationTest {

  @BeforeClass
  public static void setup() {
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
      RestAssured.port = Integer.valueOf(8000);
    } else {
      RestAssured.port = Integer.valueOf(port);
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
  public void shouldResponseWithForbiddenStatus() {
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
    //TODO: implement
  }

  @Test
  public void shouldNotCreateAccountWhenInputDataIsInvalid() {
    //TODO: implement
  }

  @Test
  public void shouldDeleteAccount() {
    //TODO: implement
  }

  @Test
  public void shouldNotDeleteAccountIfItDoesNotExist() {
    //TODO: implement
  }

  @Test
  public void shouldGetOneAccount() {
    //TODO: implement
  }

  @Test
  public void shouldNotGetOneAccountIfItDoesNotExist() {
    //TODO: implement
  }

  @Test
  public void shouldGetAllAccounts() {
    //TODO: implement
  }

  @Test
  public void shouldCommitTransaction() {
    //TODO: implement
  }

  @Test
  public void shouldNotCommitTransactionWhenInputDataIsInvalid() {
    //TODO: implement
  }

  @Test
  public void shouldGetOneTransaction() {
    //TODO: implement
  }

  @Test
  public void shouldNotGetOneTransactionIfItDoesNotExist() {
    //TODO: implement
  }

  @Test
  public void shouldGetManyTransactions() {
    //TODO: implement
  }
}