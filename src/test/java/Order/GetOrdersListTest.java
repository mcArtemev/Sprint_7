package Order;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class GetOrdersListTest {
    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
    }

    @Test
    @DisplayName("Try to get orders list")
    @Description("Test /api/v1/orders to get orders list. Return 200 and orders list")
    public void getOrdersListExpectedStatus200() {
        Response response = given()
                .header("Content-type", "application/json")
                .when()
                .get("/api/v1/orders");
                response.then().assertThat().statusCode(200);
                response.then().assertThat().body("orders", not(empty()));
    }
}
