import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ChangeUserDataTest {

    private String email = "testSasha1@yandex.ru";
    private String newEmail = "testSasha2@yandex.ru";
    private String password = "1234567";
    private String newPassword = "1234561";
    private String name = "Sasha";
    private String newName = "Pasha";

    UserApi userApi = new UserApi();

    @Before
    public void createUser() {
        UserData userData = new UserData(email, password, name);
        userApi.sendPostRequestToCreateUser(userData);
    }

    @Test
    @DisplayName("Change User's email with authorization")
    @Description("Positive test of updating User's email")
    public void updateUserEmailTest() {
        UserData userData = new UserData(email, password);
        UserData newUserdata = new UserData(newEmail, password, name);
        Response response = userApi.sendPostRequestToLoginUser(userData);
        String authToken = response.then().extract().body().path("accessToken");
        Response response1 = userApi.sendPatchRequestToUpdateUserData(newUserdata, authToken);
        userApi.checkResponseAfterUpdateUserData(response1);
    }

    @Test
    @DisplayName("Change User's password with authorization")
    @Description("Positive test of updating User's password")
    public void updateUserPasswordTest() {
        UserData userData = new UserData(email, password);
        UserData newUserdata = new UserData(email, newPassword, name);
        Response response = userApi.sendPostRequestToLoginUser(userData);
        String authToken = response.then().extract().body().path("accessToken");
        Response response1 = userApi.sendPatchRequestToUpdateUserData(newUserdata, authToken);
        userApi.checkResponseAfterUpdateUserData(response1);
    }

    @Test
    @DisplayName("Change User's name with authorization")
    @Description("Positive test of updating User's name")
    public void updateUserNameTest() {
        UserData userData = new UserData(email, password);
        UserData newUserdata = new UserData(email, password, newName);
        Response response = userApi.sendPostRequestToLoginUser(userData);
        String authToken = response.then().extract().body().path("accessToken");
        Response response1 = userApi.sendPatchRequestToUpdateUserData(newUserdata, authToken);
        userApi.checkResponseAfterUpdateUserData(response1);
    }

    @Test
    @DisplayName("Error trying to update User's email without authorization")
    @Description("Negative test of failing to update User's email without authorization")
    public void errorToUpdateEmailNoAuthTest() {
        UserData userData = new UserData(email, password);
        UserData newUserdata = new UserData(newEmail, password, name);
        userApi.sendPostRequestToLoginUser(userData);
        Response response = userApi.sendPatchRequestToUpdateUserData(newUserdata, "");
        userApi.checkResponseAfterUpdateUserDataWithoutAuthorization(response);
    }

    @Test
    @DisplayName("Error trying to update User's password without authorization")
    @Description("Negative test of failing to update User's email without authorization")
    public void errorToUpdatePasswordNoAuthTest() {
        UserData userData = new UserData(email, password);
        UserData newUserdata = new UserData(email, newPassword, name);
        userApi.sendPostRequestToLoginUser(userData);
        Response response = userApi.sendPatchRequestToUpdateUserData(newUserdata, "");
        userApi.checkResponseAfterUpdateUserDataWithoutAuthorization(response);
    }

    @Test
    @DisplayName("Error trying to update User's name without authorization")
    @Description("Negative test of failing to update User's name without authorization")
    public void errorToUpdateNameNoAuthTest() {
        UserData userData = new UserData(email, password);
        UserData newUserdata = new UserData(email, password, newName);
        userApi.sendPostRequestToLoginUser(userData);
        Response response = userApi.sendPatchRequestToUpdateUserData(newUserdata, "");
        userApi.checkResponseAfterUpdateUserDataWithoutAuthorization(response);
    }

    @Test
    @DisplayName("Error trying to update User's email if such email already exists")
    @Description("Negative test of failing to update User's email if such email already exists")
    public void errorToUpdateEmailIfAlreadyExistsTest() {
        UserData userData1 = new UserData(newEmail, password, name);
        userApi.sendPostRequestToCreateUser(userData1);
        UserData userData = new UserData(email, password);
        UserData newUserdata = new UserData(newEmail, password, name);
        Response response = userApi.sendPostRequestToLoginUser(userData);
        String authToken = response.then().extract().body().path("accessToken");
        Response response1 = userApi.sendPatchRequestToUpdateUserData(newUserdata, authToken);
        userApi.checkResponseAfterUpdateUserDataWithUsedEmail(response1);
    }

    @After
    public void deleteUser() {
        UserData userData = new UserData(email, password);
        Response response = userApi.sendPostRequestToLoginUser(userData);
        if (response.then().extract().statusCode() == 200) {
            String authToken = response.then().extract().body().path("accessToken");
            userApi.deleteUser(authToken);
        }
        UserData userData1 = new UserData(newEmail, password);
        Response response1 = userApi.sendPostRequestToLoginUser(userData1);
        if (response1.then().extract().statusCode() == 200) {
            String authToken = response1.then().extract().body().path("accessToken");
            userApi.deleteUser(authToken);
        }
        UserData userData2 = new UserData(email, newPassword);
        Response response2 = userApi.sendPostRequestToLoginUser(userData2);
        if (response2.then().extract().statusCode() == 200) {
            String authToken = response2.then().extract().body().path("accessToken");
            userApi.deleteUser(authToken);
        }

    }

}
