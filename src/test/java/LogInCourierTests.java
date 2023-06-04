import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import model.Courier;
import model.CourierLogin;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.blankOrNullString;
import static org.hamcrest.Matchers.not;


public class LogInCourierTests {

    private Courier courierToDelete;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @After
    public void clean() {
        if (courierToDelete != null) {
            CourierSteps.delete(courierToDelete);
            courierToDelete = null;
        }
    }

    @Test
    @DisplayName("Авторизация курьера")
    @Description("Успешная авторизация")
    public void shouldLoginCourier() {
        Courier courier = Courier.random();
        CourierLogin courierLogin = CourierLogin.of(courier);

        create(courier)
                .then()
                .assertThat()
                .statusCode(201);

        CourierSteps.login(courierLogin)
                .then()
                .assertThat().statusCode(200)
                .body("id", not(blankOrNullString()));
    }

    @Test
    @DisplayName("Неуспешная авторизация без пароля")
    public void shouldNotLoginCourierWithoutPassword() {
        CourierLogin courierLogin = new CourierLogin();

        courierLogin.setPassword("");
        courierLogin.setLogin("login");

        CourierSteps.login(courierLogin)
                .then()
                .assertThat().statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Неуспешная авторизация без логина")
    public void shouldNotLoginCourierWithoutLogin() {
        CourierLogin courierLogin = new CourierLogin();

        courierLogin.setPassword("pass");

        CourierSteps.login(courierLogin)
                .then()
                .assertThat().statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Неуспешная авторизация под несуществующим пользователем")
    public void shouldNotLoginWhenCourierDoesNotExist() {
        Courier courier = Courier.random();
        CourierLogin courierLogin = CourierLogin.of(courier);

        CourierSteps.login(courierLogin)
                .then()
                .assertThat().statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    private Response create(Courier courier) {
        Response response = CourierSteps.create(courier);

        if (response.getStatusCode() == 201) {
            courierToDelete = courier;
        }
        return response;
    }
}
