import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ChangeUserDataTest {

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
    @DisplayName("Change User's email with authorization")
    @Description("Positive test of updating User's email")
    public void updateUserEmailTest() {

        userApi.sendPostRequestToLoginUser(userLogin);
        UserData newUserdata = new UserData("newemailtest111@test.ru", null, null);
        Response response = userApi.sendPatchRequestToUpdateUserData(newUserdata, authToken);
        userApi.checkResponseAfterUpdateUserData(response);
        userApi.checkResponseBodyAfterUpdateUserEmail(response, "newemailtest111@test.ru");
    }

    @Test
    @DisplayName("Change User's name with authorization")
    @Description("Positive test of updating User's name")
    public void updateUserNameTest() {
        userApi.sendPostRequestToLoginUser(userLogin);
        UserData newUserdata = new UserData(null, null, "Pasha");
        Response response = userApi.sendPatchRequestToUpdateUserData(newUserdata, authToken);
        userApi.checkResponseAfterUpdateUserData(response);
        userApi.checkResponseBodyAfterUpdateUserName(response, "Pasha");
    }

    @Test
    @DisplayName("Error trying to update User's email without authorization")
    @Description("Negative test of failing to update User's email without authorization")
    public void errorToUpdateEmailNoAuthTest() {
        userApi.sendPostRequestToLoginUser(userLogin);
        UserData newUserdata = new UserData("newemailtest111@test.ru", null, null);
        Response response = userApi.sendPatchRequestToUpdateUserData(newUserdata, "");
        userApi.checkResponseAfterUpdateUserDataWithoutAuthorization(response);
    }

    @Test
    @DisplayName("Error trying to update User's name without authorization")
    @Description("Negative test of failing to update User's name without authorization")
    public void errorToUpdateNameNoAuthTest() {
        userApi.sendPostRequestToLoginUser(userLogin);
        UserData newUserdata = new UserData(null, null, "Pasha");
        Response response = userApi.sendPatchRequestToUpdateUserData(newUserdata, "");
        userApi.checkResponseAfterUpdateUserDataWithoutAuthorization(response);
    }

    @Test
    @DisplayName("Error trying to update User's email if such email already exists")
    @Description("Negative test of failing to update User's email if such email already exists")
    public void errorToUpdateEmailIfAlreadyExistsTest() {
        UserData newUser = new UserData("newUserTest@test.ru", "1234567", "Sasha");
        UserData newUserLogin = new UserData(newUser.getEmail(), newUser.getPassword());
        Response response = userApi.sendPostRequestToCreateUser(newUser);
        userApi.sendPostRequestToLoginUser(userLogin);
        UserData newUserEmail = new UserData(newUser.getEmail(), null, null);
        Response response1 = userApi.sendPatchRequestToUpdateUserData(newUserEmail, authToken);
        String newUserToken = response.then().extract().body().path("accessToken");
        userApi.sendPostRequestToLoginUser(newUserLogin);
        userApi.deleteUser(newUserToken);
        userApi.checkResponseAfterUpdateUserDataWithUsedEmail(response1);
    }

    @After
    public void deleteUser() {

        userApi.deleteUser(authToken);

    }

}
