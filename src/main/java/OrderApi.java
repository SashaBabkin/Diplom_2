import io.qameta.allure.Step;
import io.restassured.response.Response;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class OrderApi extends BaseHttpClient {

    //Ручка получения данных об ингредиентах
    private final String GET_INGREDIENTS = "/api/ingredients";
    //Ручка создания заказа
    private final String CREATE_ORDER = "/api/orders";
    //Ручка получения заказа конкретного пользователя
    private final String GET_USERS_ORDERS = "/api/orders";

    //Запрос на получение данных об ингредиентах
    @Step("Seng GET-request to /api/ingredients to get ingredients list")
    public Response sendGetRequestToGetIngredientsList() {
        Response response =
                given()
                        .spec(baseRequestSpec)
                        .when()
                        .get(GET_INGREDIENTS);
        return response;
    }

    //Получение id ингредиентов
    @Step("Get ingredients ids")
    public List<String> getIngredients(Response response) {
        return response.then().extract().jsonPath().getList("data._id");
    }

    //Создание заказа
    @Step("Seng POST-request to /api/orders to create order")
    public Response sendPostRequestToCreateOrder(OrderData orderData, String accessToken) {
        Response response =
                given()
                        .spec(baseRequestSpec)
                        .header("Authorization", accessToken)
                        .body(orderData)
                        .when()
                        .post(CREATE_ORDER);
        return response;
    }

    //Проверка ответа после создания заказа
    @Step("Check Response after creating Order")
    public void checkResponseAfterCreatingOrder(Response response) {
        response.then().assertThat().body("success", equalTo(true));
    }

    //Проверка ответа после отправки запроса на создание заказа без ингредиентов
    @Step("Check Response after sending request to create an Order without ingredients")
    public void checkResponseAfterCreatingOrderWithNoIngredients(Response response) {
        response.then().assertThat().statusCode(400).and().body("message", equalTo("Ingredient ids must be provided"));
    }

    //Проверка ответа после отправки запроса на создание заказа c невалидным хешем ингредиента
    @Step("Check Response after sending request to create an Order with not valid ingredients-hash")
    public void checkResponseAfterCreatingOrderWithNoValidIngredients(Response response) {
        response.then().assertThat().statusCode(500);
    }

    //Получение заказов конкретного пользователя
    @Step("Seng GET-request to /api/orders to get User's list of orders")
    public Response sendGetRequestToGetUsersOrders(String accessToken) {
        Response response =
                given()
                        .spec(baseRequestSpec)
                        .header("Authorization", accessToken)
                        .when()
                        .get(GET_USERS_ORDERS);
        return response;
    }

    //Проверка ответа после отправки запроса на получение списка заказов авторизированного пользователя
    @Step("Check Response after getting Order's list of authorized User")
    public void checkResponseAfterGettingUserOrdersAuth(Response response) {
        response.then().assertThat().body("success", equalTo(true));
    }

    //Проверка ответа после отправки запроса на получение списка заказов неавторизированного пользователя
    @Step("Check Response after getting Order's list of not-authorized User")
    public void checkResponseAfterGettingUserOrdersNotAuth(Response response) {
        response.then().assertThat().body("message", equalTo("You should be authorised"));
    }

}
