import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class UserLoginTest {

    UserData userData = UserGenerate.getRandomUser();
    UserData userLogin = new UserData(userData.getEmail(), userData.getPassword());

    private String wrongEmail = "wrongEmail@wrong.ru";
    private String wrongPassword = "wrong666";

    UserApi userApi = new UserApi();

    String authToken;

    @Before
    public void createUser() {
        Response response = userApi.sendPostRequestToCreateUser(userData);
        authToken = response.then().extract().body().path("accessToken");
    }

    @Test
    @DisplayName("Login User with valid data")
    @Description("Positive test of logging in User using valid data")
    public void loginUserWithValidDataTest() {
        Response response = userApi.sendPostRequestToLoginUser(userLogin);
        userApi.checkResponseAfterUserLogin(response);
    }

    @Test
    @DisplayName("Error trying to login in User with wrong email")
    @Description("Negative test of failing to logging in with wrong email")
    public void errorWhenEmailWrongTest() {
        UserData userWithWrongEmail = new UserData(wrongEmail, userData.getPassword());
        Response response = userApi.sendPostRequestToLoginUser(userWithWrongEmail);
        userApi.checkResponseAfterLoggingInUserWithIncorrectData(response);
    }

    @Test
    @DisplayName("Error trying to login in User with wrong password")
    @Description("Negative test of failing to logging in with wrong password")
    public void errorWhenPasswordWrongTest() {
        UserData userWithWrongPassword = new UserData(userData.getEmail(), wrongPassword);
        Response response = userApi.sendPostRequestToLoginUser(userWithWrongPassword);
        userApi.checkResponseAfterLoggingInUserWithIncorrectData(response);
    }


    @After
    public void deleteUser() {
        userApi.deleteUser(authToken);
    }
}
