import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class GetOrderListTest {

    OrderApi orderApi = new OrderApi();

    @Test
    @DisplayName("Get orders' list of authorized user")
    @Description("Positive test of getting orders' list of authorized user")
    public void getOrdersListOfAuthUserTest() {
        //Получение списка ингредиентов
        Response response = orderApi.sendGetRequestToGetIngredientsList();
        List<String> allIngredients = orderApi.getIngredients(response);
        //Выбор ингредиентов для заказа
        List<String> ingredientsInOrder = new ArrayList<>();
        ingredientsInOrder.add(allIngredients.get(0));
        ingredientsInOrder.add(allIngredients.get(1));
        //Создание пользователя
        UserData userData = new UserData("testSasha1@yandex.ru", "1234567", "Sasha");
        UserApi userApi = new UserApi();
        Response response1 = userApi.sendPostRequestToCreateUser(userData);
        //Получение Токена созданного Пользователя
        String authToken = response1.then().extract().body().path("accessToken");
        //Создание заказа
        OrderData orderData = new OrderData(ingredientsInOrder);
        Response response2 = orderApi.sendPostRequestToCreateOrder(orderData, authToken);
        orderApi.checkResponseAfterCreatingOrder(response2);
        //Запрос на получение списка заказов Пользователя
        Response response3 = orderApi.sendGetRequestToGetUsersOrders(authToken);
        //Проверка ответа после отправки запроса на получение списка заказов Пользователя
        orderApi.checkResponseAfterGettingUserOrdersAuth(response3);
        //Удаление пользователя
        userApi.deleteUser(authToken);
    }

    @Test
    @DisplayName("Get orders' list of not authorized user")
    @Description("Negative test of getting orders' list of authorized user")
    public void getOrdersListOfNotAuthUserTest() {
        //Запрос на получение списка заказов Пользователя
        Response response = orderApi.sendGetRequestToGetUsersOrders("");
        //Проверка ответа после отправки запроса на получение списка заказов Пользователя
        orderApi.checkResponseAfterGettingUserOrdersNotAuth(response);

    }

}
