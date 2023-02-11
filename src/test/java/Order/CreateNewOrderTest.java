package Order;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.hamcrest.MatcherAssert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;


@RunWith(Parameterized.class)
public class CreateNewOrderTest {
    private final Object order;
    TestFixture testFixture = new TestFixture();
    Response response;

    public CreateNewOrderTest(Object order) {
        this.order = order;
    }
    @Parameterized.Parameters
    public static Object[][] getOrderData() {
        return new Object[][] {
                {new CreateOrderSerializer("testName","testLastname","Test street 1","MetroTest","89999999999",7,"2022-01-01","Test comment", new String[]{"BLACK"})},
                {new CreateOrderSerializer( "testName","testLastname","Test street 1","MetroTest","89999999999",7,"2022-01-01","Test comment", new String[]{"GREY"})},
                {new CreateOrderSerializer( "testName","testLastname","Test street 1","MetroTest","89999999999",7,"2022-01-01","Test comment", new String[]{"BLACK", "GREY"})},
                {new CreateOrderSerializer( "testName","testLastname","Test street 1","MetroTest","89999999999",7,"2022-01-01","Test comment", new String[]{})},
        };
    }
        @Before
        public void setUp() {
            RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
        }
        @After
        public void tearDown(){
            if (response.statusCode() == 201) {
                testFixture.deleteOrder(response.as(CreateOrderDeserializer.class).getTrack());
            }
        }

        @Test
        @DisplayName("Try to create new order with variable color")
        @Description("Test /api/v1/orders with variable colors. Return 201 and track")
        public void createOrderWithVariableColorExpectedStatus201() {
            response = given()
                    .header("Content-type", "application/json")
                    .body(order)
                    .when()
                    .post("/api/v1/orders");
            response.then().assertThat().statusCode(201);
            MatcherAssert.assertThat(response.as(CreateOrderDeserializer.class).getTrack(), is(notNullValue()));
        }
}
