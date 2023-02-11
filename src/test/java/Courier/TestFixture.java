package Courier;

import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class TestFixture {
    public Response loginCourier(String login, String password){
        String courierData = "{\"login\":\""+login+"\",\"password\":\""+password+"\"}";
        Response response = given()
                .header("Content-type", "application/json")
                .body(courierData)
                .when()
                .post("/api/v1/courier/login");
        return response;
    }
    public String getCourierId(Response response) {
        if (response.statusCode()==200) {
            CourierLoginDeserializer courierId = response.body().as(CourierLoginDeserializer.class);
            return courierId.getId();
        } else {
            return null;
        }
    }
    public void deleteCourier(String courierId){
        given()
                .delete("/api/v1/courier/"+courierId)
                .then()
                .statusCode(200);
    }
    public Response createNewCourier(CourierSerializer courierJsonData) {
        Response response = given()
                .header("Content-type", "application/json")
                .body(courierJsonData)
                .when()
                .post("/api/v1/courier");
        return response;
    }
}
