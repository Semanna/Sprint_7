import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import model.Courier;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;

public class CreateCourierTests {

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
    @DisplayName("Создание курьера")
    @Description("Курьер создан")
    public void shouldCreateCourier() {
        Courier courier = Courier.random();

        create(courier)
                .then()
                .assertThat()
                .statusCode(201)
                .body("ok", equalTo(true));
    }

    @Test
    @DisplayName("Неуспешное создание курьера, который уже есть в системе")
    public void shouldNotCreateSameCourier() {
        Courier courier = Courier.random();

        create(courier)
                .then()
                .assertThat().statusCode(201);

        create(courier)
                .then()
                .assertThat().statusCode(409)
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }

    @Test
    @DisplayName("Неуспешное создание курьера без логина")
    public void shouldNotCreateCourierWithoutLogin() {
        Courier courier = Courier.random();

        courier.setLogin(null);

        create(courier)
                .then()
                .assertThat().statusCode(400)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Неуспешное создание курьера без пароля")
    public void shouldNotCreateCourierWithoutPassword() {
        Courier courier = Courier.random();

        courier.setPassword(null);

        create(courier)
                .then()
                .assertThat().statusCode(400)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    private Response create(Courier courier) {
        Response response = CourierSteps.create(courier);

        if (response.getStatusCode() == 201) {
            courierToDelete = courier;
        }
        return response;
    }
}
