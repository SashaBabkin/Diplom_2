import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;

public class UserCreateTest {

    private String email = "testSasha1@yandex.ru";
    private String password = "1234567";
    private String name = "Sasha";
    private String noEmail;
    private String noPassword;
    private String noName;

    UserApi userApi = new UserApi();

    @Test
    @DisplayName("Create User with valid data")
    @Description("Positive test of creating User using valid data")
    public void userCreatedWithValidDataTest() {
        UserData userData = new UserData(email, password, name);
        Response response = userApi.sendPostRequestToCreateUser(userData);
        userApi.checkResponseAfterCreatingUser(response);

    }

    @Test
    @DisplayName("Error trying to create two identical users")
    @Description("Negative test of no opportunity creating two users with identical data")
    public void notPossibleToCreateTwoIdenticalUsersTest() {
        UserData userData = new UserData(email, password, name);
        userApi.sendPostRequestToCreateUser(userData);
        Response response = userApi.sendPostRequestToCreateUser(userData);
        userApi.checkResponseAfterCreatingTwoIdenticalUsers(response);
    }

    @Test
    @DisplayName("Error trying to create User with email missing")
    @Description("Negative test of no opportunity creating User with email missing")
    public void notPossibleToCreateCourierWithEmailMissingTest() {
        UserData userData = new UserData(noEmail, password, name);
        Response response = userApi.sendPostRequestToCreateUser(userData);
        userApi.checkResponseAfterCreatingUserWithRequiredFieldsMissing(response);
    }

    @Test
    @DisplayName("Error trying to create User with password missing")
    @Description("Negative test of no opportunity creating User with password missing")
    public void notPossibleToCreateCourierWithPasswordMissingTest() {
        UserData userData = new UserData(email, noPassword, name);
        Response response = userApi.sendPostRequestToCreateUser(userData);
        userApi.checkResponseAfterCreatingUserWithRequiredFieldsMissing(response);
    }

    @Test
    @DisplayName("Error trying to create User with name missing")
    @Description("Negative test of no opportunity creating User with name missing")
    public void notPossibleToCreateCourierWithNameMissingTest() {
        UserData userData = new UserData(email, password, noName);
        Response response = userApi.sendPostRequestToCreateUser(userData);
        userApi.checkResponseAfterCreatingUserWithRequiredFieldsMissing(response);
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
