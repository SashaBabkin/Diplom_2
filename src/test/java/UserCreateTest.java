import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;

public class UserCreateTest {

    UserData userData = UserGenerate.getRandomUser();
    UserData userLogin = new UserData(userData.getEmail(), userData.getPassword());

    UserApi userApi = new UserApi();

    @Test
    @DisplayName("Create User with valid data")
    @Description("Positive test of creating User using valid data")
    public void userCreatedWithValidDataTest() {
        Response response = userApi.sendPostRequestToCreateUser(userData);
        userApi.checkResponseAfterCreatingUser(response);

    }

    @Test
    @DisplayName("Error trying to create two identical users")
    @Description("Negative test of no opportunity creating two users with identical data")
    public void notPossibleToCreateTwoIdenticalUsersTest() {
        userApi.sendPostRequestToCreateUser(userData);
        Response response = userApi.sendPostRequestToCreateUser(userData);
        userApi.checkResponseAfterCreatingTwoIdenticalUsers(response);
    }

    @Test
    @DisplayName("Error trying to create User with email missing")
    @Description("Negative test of no opportunity creating User with email missing")
    public void notPossibleToCreateCourierWithEmailMissingTest() {
        UserData userWithoutEmail = new UserData("", "1234567", "Sasha");
        Response response = userApi.sendPostRequestToCreateUser(userWithoutEmail);
        userApi.checkResponseAfterCreatingUserWithRequiredFieldsMissing(response);
    }

    @Test
    @DisplayName("Error trying to create User with password missing")
    @Description("Negative test of no opportunity creating User with password missing")
    public void notPossibleToCreateCourierWithPasswordMissingTest() {
        UserData userWithoutPassword = new UserData("sashaTest123@test.ru", "", "Sasha");
        Response response = userApi.sendPostRequestToCreateUser(userWithoutPassword);
        userApi.checkResponseAfterCreatingUserWithRequiredFieldsMissing(response);
    }

    @Test
    @DisplayName("Error trying to create User with name missing")
    @Description("Negative test of no opportunity creating User with name missing")
    public void notPossibleToCreateCourierWithNameMissingTest() {
        UserData userWithoutName = new UserData("sashaTest123@test.ru", "1234567", "");
        Response response = userApi.sendPostRequestToCreateUser(userWithoutName);
        userApi.checkResponseAfterCreatingUserWithRequiredFieldsMissing(response);
    }

    @After
    public void deleteUser() {
        Response response = userApi.sendPostRequestToLoginUser(userLogin);
        if (response.then().extract().statusCode() == 200) {
            String authToken = response.then().extract().body().path("accessToken");
            userApi.deleteUser(authToken);
        }

    }


}
