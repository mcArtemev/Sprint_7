package Courier;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.hamcrest.MatcherAssert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.*;

public class LoginCourierTest {
    TestFixture testFixture = new TestFixture();
    Response createdCourier;
    String login;
    String password;
    String firstName;

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
    }
    @After
    public void tearDown() {
        if (createdCourier.statusCode()==201) {
            testFixture.deleteCourier(testFixture.getCourierId(testFixture.loginCourier(login, password)));
        }
    }

    @Test
    @DisplayName("Try to login new courier with valid data")
    @Description("Test /api/v1/courier/login. Return 200 and courierId")
    public void loginCourierWithValidDataExpectedStatus200() {
        login = "loginCourier";
        password = "1234";
        firstName = "loginCourier";
        CourierSerializer courierJsonData = new CourierSerializer(login, password, firstName);
        CourierSerializer courierLoginData = new CourierSerializer("loginCourier", "1234");

        createdCourier = testFixture.createNewCourier(courierJsonData);
        Response loginCourierRes = testFixture.loginCourier(courierLoginData.getLogin(), courierLoginData.getPassword());

        loginCourierRes.then().assertThat().statusCode(200);
        MatcherAssert.assertThat(loginCourierRes.as(CourierLoginDeserializer.class).getId(), is(notNullValue()));
    }
    @Test
    @DisplayName("Try to login new courier with invalid data")
    @Description("Test /api/v1/courier/login with invalid login. Return 404 and message: Учетная запись не найдена")
    public void loginCourierWithInvalidLoginExpectedStatus404() {
        login = "loginCourier";
        password = "1234";
        firstName = "loginCourie";
        CourierSerializer courierJsonData = new CourierSerializer(login, password, firstName);
        CourierSerializer courierLoginData = new CourierSerializer("invalidCourierLogin", "1234");

        createdCourier = testFixture.createNewCourier(courierJsonData);
        Response response = testFixture.loginCourier(courierLoginData.getLogin(), courierLoginData.getPassword());

        response.then().assertThat().statusCode(404);
        MatcherAssert.assertThat(response.as(CourierLoginDeserializer.class).getMessage(), equalTo("Учетная запись не найдена"));
    }
    @Test
    @DisplayName("Try to login new courier with invalid password")
    @Description("Test /api/v1/courier/login with invalid password. Return 404 and message: Учетная запись не найдена")
    public void loginCourierWithInvalidPasswordExpectedStatus404() {
        login = "loginCourier";
        password = "1234";
        firstName = "loginCourie";
        CourierSerializer courierJsonData = new CourierSerializer(login, password, firstName);
        CourierSerializer courierLoginData = new CourierSerializer("loginCourier", "4321");

        createdCourier = testFixture.createNewCourier(courierJsonData);
        Response response = testFixture.loginCourier(courierLoginData.getLogin(), courierLoginData.getPassword());

        response.then().assertThat().statusCode(404);
        MatcherAssert.assertThat(response.as(CourierLoginDeserializer.class).getMessage(), equalTo("Учетная запись не найдена"));
    }
    @Test
    @DisplayName("Try to login new courier without login")
    @Description("Test /api/v1/courier/login without login. Return 400 and message: Недостаточно данных для входа")
    public void loginCourierWithoutLoginExpectedStatus400() {
        login = "loginCourier";
        password = "1234";
        firstName = "loginCourie";
        CourierSerializer courierJsonData = new CourierSerializer("loginCourier", "1234", "loginCourier");
        CourierSerializer courierLoginData = new CourierSerializer(null, "1234");

        createdCourier = testFixture.createNewCourier(courierJsonData);
        Response response = testFixture.loginCourier(courierLoginData.getLogin(), courierLoginData.getPassword());

        response.then().assertThat().statusCode(400);
        MatcherAssert.assertThat(response.as(CourierLoginDeserializer.class).getMessage(), equalTo("Недостаточно данных для входа"));
    }
    @Test
    @DisplayName("Try to login new courier without password")
    @Description("Test /api/v1/courier/login without password. Return 400 and message: Недостаточно данных для входа")
    public void loginCourierWithoutPasswordExpectedStatus400() {
        login = "loginCourier";
        password = "1234";
        firstName = "loginCourie";
        CourierSerializer courierJsonData = new CourierSerializer("loginCourier", "1234", "loginCourier");
        CourierSerializer courierLoginData = new CourierSerializer("loginCourier", null);

        createdCourier = testFixture.createNewCourier(courierJsonData);
        Response response = testFixture.loginCourier(courierLoginData.getLogin(), courierLoginData.getPassword());

        response.then().assertThat().statusCode(400);
        MatcherAssert.assertThat(response.as(CourierLoginDeserializer.class).getMessage(), equalTo("Недостаточно данных для входа"));
    }
}
