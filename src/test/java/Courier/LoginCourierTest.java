package Courier;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.hamcrest.MatcherAssert;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;

public class LoginCourierTest {
    TestFixture testFixture = new TestFixture();

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
    }

    @Test
    @DisplayName("Try to login new courier with valid data")
    @Description("Test /api/v1/courier/login. Return 200 and courierId")
    public void loginCourierWithValidDataExpectedStatus200() {
        CourierSerializer courierJsonData = new CourierSerializer("loginCourier", "1234", "loginCourier");
        CourierSerializer courierLoginData = new CourierSerializer("loginCourier", "1234");
        try {
            testFixture.createNewCourier(courierJsonData);
            Response response = given()
                    .header("Content-type", "application/json")
                    .body(courierLoginData)
                    .when()
                    .post("/api/v1/courier/login");
            response.then().assertThat().statusCode(200);
            MatcherAssert.assertThat(response.as(CourierLoginDeserializer.class).getId(), is(notNullValue()));
        } finally {
            //Delete new courier after test
            testFixture.deleteCourier(testFixture.getCourierId(courierJsonData.getLogin(),courierJsonData.getPassword()));
        }
    }
    @Test
    @DisplayName("Try to login new courier with invalid data")
    @Description("Test /api/v1/courier/login with invalid login. Return 404 and message: Учетная запись не найдена")
    public void loginCourierWithInvalidLoginExpectedStatus404() {
        CourierSerializer courierJsonData = new CourierSerializer("loginCourier", "1234", "loginCourier");
        CourierSerializer courierLoginData = new CourierSerializer("invalidCourierLogin", "1234");
        try {
            testFixture.createNewCourier(courierJsonData);
            Response response = given()
                    .header("Content-type", "application/json")
                    .body(courierLoginData)
                    .when()
                    .post("/api/v1/courier/login");
            response.then().assertThat().statusCode(404);
            MatcherAssert.assertThat(response.as(CourierLoginDeserializer.class).getMessage(), equalTo("Учетная запись не найдена"));
        } finally {
            //Delete new courier after test
            testFixture.deleteCourier(testFixture.getCourierId(courierJsonData.getLogin(),courierJsonData.getPassword()));
        }
    }
    @Test
    @DisplayName("Try to login new courier with invalid password")
    @Description("Test /api/v1/courier/login with invalid password. Return 404 and message: Учетная запись не найдена")
    public void loginCourierWithInvalidPasswordExpectedStatus404() {
        CourierSerializer courierJsonData = new CourierSerializer("loginCourier", "1234", "loginCourier");
        CourierSerializer courierLoginData = new CourierSerializer("loginCourier", "4321");
        try {
            testFixture.createNewCourier(courierJsonData);
            Response response = given()
                    .header("Content-type", "application/json")
                    .body(courierLoginData)
                    .when()
                    .post("/api/v1/courier/login");
            response.then().assertThat().statusCode(404);
            MatcherAssert.assertThat(response.as(CourierLoginDeserializer.class).getMessage(), equalTo("Учетная запись не найдена"));
        } finally {
            //Delete new courier after test
            testFixture.deleteCourier(testFixture.getCourierId(courierJsonData.getLogin(),courierJsonData.getPassword()));
        }
    }
    @Test
    @DisplayName("Try to login new courier without login")
    @Description("Test /api/v1/courier/login without login. Return 400 and message: Недостаточно данных для входа")
    public void loginCourierWithoutLoginExpectedStatus400() {
        CourierSerializer courierJsonData = new CourierSerializer("loginCourier", "1234", "loginCourier");
        CourierSerializer courierLoginData = new CourierSerializer(null, "1234");
        try {
            testFixture.createNewCourier(courierJsonData);
            Response response = given()
                    .header("Content-type", "application/json")
                    .body(courierLoginData)
                    .when()
                    .post("/api/v1/courier/login");
            response.then().assertThat().statusCode(400);
            MatcherAssert.assertThat(response.as(CourierLoginDeserializer.class).getMessage(), equalTo("Недостаточно данных для входа"));
        } finally {
            //Delete new courier after test
            testFixture.deleteCourier(testFixture.getCourierId(courierJsonData.getLogin(),courierJsonData.getPassword()));
        }
    }
    @Test
    @DisplayName("Try to login new courier without password")
    @Description("Test /api/v1/courier/login without password. Return 400 and message: Недостаточно данных для входа")
    public void loginCourierWithoutPasswordExpectedStatus400() {
        CourierSerializer courierJsonData = new CourierSerializer("loginCourier", "1234", "loginCourier");
        CourierSerializer courierLoginData = new CourierSerializer("loginCourier", null);
        try {
            testFixture.createNewCourier(courierJsonData);
            Response response = given()
                    .header("Content-type", "application/json")
                    .body(courierLoginData)
                    .when()
                    .post("/api/v1/courier/login");
            response.then().assertThat().statusCode(400);
            MatcherAssert.assertThat(response.as(CourierLoginDeserializer.class).getMessage(), equalTo("Недостаточно данных для входа"));
        } finally {
            //Delete new courier after test
            testFixture.deleteCourier(testFixture.getCourierId(courierJsonData.getLogin(),courierJsonData.getPassword()));
        }
    }
}
