package Courier;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.hamcrest.MatcherAssert;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;

public class CreateNewCourierTest {
    TestFixture testFixture = new TestFixture();

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
    }

    @Test
    @DisplayName("Try to create new courier with valid data")
    @Description("Test /api/v1/courier return 201 and statusText ok: true")
    public void createNewCourierWithValidDataExpectedStatus201() {
        CourierSerializer courierJsonData = new CourierSerializer("newCourier", "1234", "newCourier");
        try {
            Response response = testFixture.createNewCourier(courierJsonData);
            response.then().assertThat().statusCode(201);
            MatcherAssert.assertThat(response.as(CourierCreateDeserializer.class).getOk(), equalTo(true));
        } finally {
            //Delete new courier after test
            testFixture.deleteCourier(testFixture.getCourierId(courierJsonData.getLogin(),courierJsonData.getPassword()));
        }
    }
    @Test
    @DisplayName("Try to create new courier with existing login")
    @Description("Test /api/v1/courier with dublicate courier data. Return 409 and message: Этот логин уже используется")
    public void createNewCourierWithExistingCourierLoginExpectedStatus409() {
        CourierSerializer courierJsonData = new CourierSerializer("dublicateCourier", "1234", "dublicateCourier");
        try {
            testFixture.createNewCourier(courierJsonData);
            Response dublicateResponse = testFixture.createNewCourier(courierJsonData);
            MatcherAssert.assertThat(dublicateResponse.as(CourierCreateDeserializer.class).getMessage(), equalTo("Этот логин уже используется"));
        } finally {
            //Delete new courier after test
            testFixture.deleteCourier(testFixture.getCourierId(courierJsonData.getLogin(),courierJsonData.getPassword()));
        }
    }
    @Test
    @DisplayName("Try to create new courier without login")
    @Description("Test /api/v1/courier without login. Return 400 and message: Недостаточно данных для создания учетной записи")
    public void createNewCourierWithoutRequiredFieldLoginExpectedStatus400(){
        CourierSerializer courierJsonData = new CourierSerializer(null, "1234", "newCourierWithoutLogin");
        try {
            Response response = testFixture.createNewCourier(courierJsonData);
            response.then().assertThat().statusCode(400);
            MatcherAssert.assertThat(response.as(CourierCreateDeserializer.class).getMessage(), equalTo("Недостаточно данных для создания учетной записи"));
        } finally {
            //Try to delete courier if method created new courier
            try {
                //Delete new courier after test
                testFixture.deleteCourier(testFixture.getCourierId(courierJsonData.getLogin(),courierJsonData.getPassword()));
            } catch (AssertionError e) {
                return;
            }
        }
    }
    @Test
    @DisplayName("Try to create new courier without password")
    @Description("Test /api/v1/courier without password. Return 400 and message: Недостаточно данных для создания учетной записи")
    public void createNewCourierWithoutRequiredFieldPasswordExpectedStatus400(){
        CourierSerializer courierJsonData = new CourierSerializer("newCourierWithoutPassword", null, "newCourierWithoutLogin");
        try {
            Response response = testFixture.createNewCourier(courierJsonData);
            response.then().assertThat().statusCode(400);
            MatcherAssert.assertThat(response.as(CourierCreateDeserializer.class).getMessage(), equalTo("Недостаточно данных для создания учетной записи"));
        } finally {
            //Try to delete courier if method failed and created new courier
            try {
                //Delete new courier after test
                testFixture.deleteCourier(testFixture.getCourierId(courierJsonData.getLogin(), courierJsonData.getPassword()));
            } catch (AssertionError e) {
                return;
            }
        }
    }
}