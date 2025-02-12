import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class OrderCreateTest {

    OrderApi orderApi = new OrderApi();

    @Test
    @DisplayName("Create Order with valid ingredients ids and no authorization")
    @Description("Positive test of creating Order using valid ingredients ids and no authorization")
    public void orderCreationValidIngredientsNoAuthTest() {
        //Получение списка ингредиентов
        Response response = orderApi.sendGetRequestToGetIngredientsList();
        List<String> allIngredients = orderApi.getIngredients(response);
        //Выбор ингредиентов для заказа
        List<String> ingredientsInOrder = new ArrayList<>();
        ingredientsInOrder.add(allIngredients.get(0));
        ingredientsInOrder.add(allIngredients.get(1));
        //Создание заказа
        OrderData orderData = new OrderData(ingredientsInOrder);
        Response response1 = orderApi.sendPostRequestToCreateOrder(orderData, "");
        orderApi.checkResponseAfterCreatingOrder(response1);
    }

    @Test
    @DisplayName("Create Order with valid ingredients ids and authorization")
    @Description("Positive test of creating Order using valid ingredients ids and authorization")
    public void orderCreationValidIngredientsAuthTest() {
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
        //Удаление пользователя
        userApi.deleteUser(authToken);
    }

    @Test
    @DisplayName("Create Order with no ingredients added")
    @Description("Negative test of creating Order with no ingredients added")
    public void errorOrderNoIngredientsTest() {
        List<String> ingredientsInOrder = new ArrayList<>();
        //Создание заказа
        OrderData orderData = new OrderData(ingredientsInOrder);
        Response response = orderApi.sendPostRequestToCreateOrder(orderData, "");
        orderApi.checkResponseAfterCreatingOrderWithNoIngredients(response);
    }

    @Test
    @DisplayName("Create Order with incorrect ingredients ids")
    @Description("Negative test of creating Order with incorrect ids")
    public void errorOrderIncorrectIngredientsTest() {
        List<String> ingredientsInOrder = new ArrayList<>();
        ingredientsInOrder.add("mmm");
        ingredientsInOrder.add("nnn");
        //Создание заказа
        OrderData orderData = new OrderData(ingredientsInOrder);
        Response response = orderApi.sendPostRequestToCreateOrder(orderData, "");
        orderApi.checkResponseAfterCreatingOrderWithNoValidIngredients(response);
    }
}
