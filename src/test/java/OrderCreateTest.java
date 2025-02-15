import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class OrderCreateTest {

    OrderApi orderApi = new OrderApi();
    UserApi userApi = new UserApi();

    UserData userData = UserGenerate.getRandomUser();
    UserData userLogin = new UserData(userData.getEmail(), userData.getPassword());

    String authToken;


    @Before
    public void createUser() {

        Response response = userApi.sendPostRequestToCreateUser(userData);
        authToken = response.then().extract().body().path("accessToken");

    }

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
        //Создание заказа
        OrderData orderData = new OrderData(ingredientsInOrder);
        Response response2 = orderApi.sendPostRequestToCreateOrder(orderData, authToken);
        orderApi.checkResponseAfterCreatingOrder(response2);

    }

    @Test
    @DisplayName("Create Order with no ingredients added")
    @Description("Negative test of creating Order with no ingredients added")
    public void errorOrderNoIngredientsTest() {
        List<String> ingredientsInOrder = new ArrayList<>();
        //Создание заказа
        OrderData orderData = new OrderData(ingredientsInOrder);
        Response response = orderApi.sendPostRequestToCreateOrder(orderData, authToken);
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
        Response response = orderApi.sendPostRequestToCreateOrder(orderData, authToken);
        orderApi.checkResponseAfterCreatingOrderWithNoValidIngredients(response);
    }

    @After
    public void deleteUser() {
        userApi.deleteUser(authToken);
    }
}
