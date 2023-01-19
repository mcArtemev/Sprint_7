package Order;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.hamcrest.MatcherAssert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;


@RunWith(Parameterized.class)
public class CreateNewOrderTest {
    private final String firstName;
    private final String lastName;
    private final String address;
    private final String metroStation;
    private final String phone;
    private final int rentTime;
    private final String deliveryDate;
    private final String comment;
    private final String[] color;

    TestFixture testFixture = new TestFixture();

    public CreateNewOrderTest(
            String firstName,
            String lastName,
            String address,
            String metroStation,
            String phone,
            int rentTime,
            String deliveryDate,
            String comment,
            String[] color) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.metroStation = metroStation;
        this.phone = phone;
        this.rentTime = rentTime;
        this.deliveryDate = deliveryDate;
        this.comment = comment;
        this.color = color;
    }
    @Parameterized.Parameters
    public static Object[][] getOrderData() {
        return new Object[][] {
                { "testName","testLastname","Test street 1","MetroTest","89999999999",7,"2022-01-01","Test comment", new String[]{"BLACK"}},
                { "testName","testLastname","Test street 1","MetroTest","89999999999",7,"2022-01-01","Test comment", new String[]{"GREY"}},
                { "testName","testLastname","Test street 1","MetroTest","89999999999",7,"2022-01-01","Test comment", new String[]{"BLACK", "GREY"}},
                { "testName","testLastname","Test street 1","MetroTest","89999999999",7,"2022-01-01","Test comment", new String[]{}},
        };
    }
        @Before
        public void setUp() {
            RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
        }

        @Test
        @DisplayName("Try to create new order with variable color")
        @Description("Test /api/v1/orders with variable colors. Return 201 and track")
        public void createOrderWithVariableColorExpectedStatus201() {
        CreateOrderSerializer orderDataJson = new CreateOrderSerializer(
                firstName,
                lastName,
                address,
                metroStation,
                phone,
                rentTime,
                deliveryDate,
                comment,
                color);
            Response response = given()
                    .header("Content-type", "application/json")
                    .body(orderDataJson)
                    .when()
                    .post("/api/v1/orders");
            response.then().assertThat().statusCode(201);
            MatcherAssert.assertThat(response.as(CreateOrderDeserializer.class).getTrack(), is(notNullValue()));

            testFixture.deleteOrder(response.as(CreateOrderDeserializer.class).getTrack());
        }
}
