import io.qameta.allure.Step;
import io.restassured.response.Response;
import model.Courier;
import model.CourierLogin;
import model.LoginResponse;

import static io.restassured.RestAssured.given;

public class CourierSteps {
    @Step("Создание курьера")
    public static Response create(Courier courier) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post("/api/v1/courier");
    }

    @Step("Логин курьера")
    public static Response login(CourierLogin courierLogin) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(courierLogin)
                .when()
                .post("/api/v1/courier/login");
    }

    @Step("Удаление курьера")
    public static void delete(Courier courier) {
        CourierLogin courierLogin = CourierLogin.of(courier);

        LoginResponse loginResponse = given()
                .header("Content-type", "application/json")
                .and()
                .body(courierLogin)
                .when()
                .post("/api/v1/courier/login")
                .then()
                .assertThat().statusCode(200)
                .extract().as(LoginResponse.class);

        given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .delete("/api/v1/courier/" + loginResponse.getId())
                .then()
                .assertThat().statusCode(200);
    }
}
