package Courier;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.hamcrest.MatcherAssert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;

public class CreateNewCourierTest {
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
    @DisplayName("Try to create new courier with valid data")
    @Description("Test /api/v1/courier return 201 and statusText ok: true")
    public void createNewCourierWithValidDataExpectedStatus201() {
        login = "newCourier";
        password = "1234";
        firstName = "newCourier";
        CourierSerializer courierJsonData = new CourierSerializer(login, password, firstName);

        createdCourier = testFixture.createNewCourier(courierJsonData);

        createdCourier.then().assertThat().statusCode(201);
        MatcherAssert.assertThat(createdCourier.as(CourierCreateDeserializer.class).getOk(), equalTo(true));
    }

    @Test
    @DisplayName("Try to create new courier with existing login")
    @Description("Test /api/v1/courier with dublicate courier data. Return 409 and message: Этот логин уже используется")
    public void createNewCourierWithExistingCourierLoginExpectedStatus409() {
        login = "dublicateCourier";
        password = "1234";
        firstName = "dublicateCourier";
        CourierSerializer courierJsonData = new CourierSerializer(login, password, firstName);

        createdCourier = testFixture.createNewCourier(courierJsonData);
        Response dublicateRes = testFixture.createNewCourier(courierJsonData);

        dublicateRes.then().assertThat().statusCode(409);
        MatcherAssert.assertThat(dublicateRes.as(CourierCreateDeserializer.class).getMessage(), equalTo("Этот логин уже используется"));
    }
    @Test
    @DisplayName("Try to create new courier without login")
    @Description("Test /api/v1/courier without login. Return 400 and message: Недостаточно данных для создания учетной записи")
    public void createNewCourierWithoutRequiredFieldLoginExpectedStatus400(){
        login = null;
        password = "1234";
        firstName = "newCourierWithoutLogin";
        CourierSerializer courierJsonData = new CourierSerializer(login, password, firstName);

        createdCourier = testFixture.createNewCourier(courierJsonData);

        createdCourier.then().assertThat().statusCode(400);
        MatcherAssert.assertThat(createdCourier.as(CourierCreateDeserializer.class).getMessage(), equalTo("Недостаточно данных для создания учетной записи"));
    }
    @Test
    @DisplayName("Try to create new courier without password")
    @Description("Test /api/v1/courier without password. Return 400 and message: Недостаточно данных для создания учетной записи")
    public void createNewCourierWithoutRequiredFieldPasswordExpectedStatus400(){
        login = "newCourierWithoutPassword";
        password = null;
        firstName = "newCourierWithoutPassword";
        CourierSerializer courierJsonData = new CourierSerializer(login, password, firstName);

        createdCourier = testFixture.createNewCourier(courierJsonData);

        createdCourier.then().assertThat().statusCode(400);
        MatcherAssert.assertThat(createdCourier.as(CourierCreateDeserializer.class).getMessage(), equalTo("Недостаточно данных для создания учетной записи"));
    }
}