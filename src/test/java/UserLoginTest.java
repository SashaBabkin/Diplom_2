import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class UserLoginTest {

    private String email = "testSasha1@yandex.ru";
    private String password = "1234567";
    private String name = "Sasha";
    private String wrongEmail = "wrongEmail@wrong.ru";
    private String wrongPassword = "wrong666";

    UserApi userApi = new UserApi();

    @Before
    public void createUser() {
        UserData userData = new UserData(email, password, name);
        userApi.sendPostRequestToCreateUser(userData);
    }

    @Test
    @DisplayName("Login User with valid data")
    @Description("Positive test of logging in User using valid data")
    public void loginUserWithValidDataTest() {
        UserData userData = new UserData(email, password);
        Response response = userApi.sendPostRequestToLoginUser(userData);
        userApi.checkResponseAfterUserLogin(response);
    }

    @Test
    @DisplayName("Error trying to login in User with wrong email")
    @Description("Negative test of failing to logging in with wrong email")
    public void errorWhenEmailWrongTest() {
        UserData userData = new UserData(wrongEmail, password);
        Response response = userApi.sendPostRequestToLoginUser(userData);
        userApi.checkResponseAfterLoggingInUserWithIncorrectData(response);
    }

    @Test
    @DisplayName("Error trying to login in User with wrong password")
    @Description("Negative test of failing to logging in with wrong password")
    public void errorWhenPasswordWrongTest() {
        UserData userData = new UserData(email, wrongPassword);
        Response response = userApi.sendPostRequestToLoginUser(userData);
        userApi.checkResponseAfterLoggingInUserWithIncorrectData(response);
    }


    @After
    public void deleteUser() {
        UserData userData = new UserData(email, password);
        Response response = userApi.sendPostRequestToLoginUser(userData);
        if (response.then().extract().statusCode() == 200) {
            String authToken = response.then().extract().body().path("accessToken");
            userApi.deleteUser(authToken);
        }

    }

}
