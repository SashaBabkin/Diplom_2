import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class UserApi extends BaseHttpClient {

    //Ручка создания пользователя
    private final String CREATE_USER = "/api/auth/register";
    //Ручка авторизации пользователя
    private final String LOGIN_USER = "/api/auth/login";
    //Ручка изменения данных пользователя
    private final String USER_DATA_UPDATE = "/api/auth/user";
    //Ручка удаления пользователя
    private final String DELETE_USER = "/api/auth/user";

    //Создание пользователя
    @Step("Seng POST-request to /api/auth/register to create user")
    public Response sendPostRequestToCreateUser(UserData userData) {
        Response response =
                given()
                        .spec(baseRequestSpec)
                        .body(userData)
                        .when()
                        .post(CREATE_USER);
        return response;
    }

    //Проверка ответа после создания пользователя
    @Step("Check Response after creating User")
    public void checkResponseAfterCreatingUser(Response response) {
        response.then().assertThat().statusCode(200).and().body("success", equalTo(true));
    }

    //Проверка ответа после попытки создания двух одинаковых пользователей
    @Step("Check Response after attempt creating two identical Users")
    public void checkResponseAfterCreatingTwoIdenticalUsers(Response response) {
        response.then().assertThat().statusCode(403).and().body("message", equalTo("User already exists"));
    }

    //Проверка ответа после попытки создания пользователя, если не переданы все обязательные поля
    @Step("Check Response after attempt creating User with email, password or name missing")
    public void checkResponseAfterCreatingUserWithRequiredFieldsMissing(Response response) {
        response.then().assertThat().statusCode(403).and().body("message", equalTo("Email, password and name are required fields"));
    }

    //Авторизация пользователя
    @Step("Seng POST-request to /api/auth/login to login User")
    public Response sendPostRequestToLoginUser(UserData userData) {
        Response response =
                given()
                        .spec(baseRequestSpec)
                        .body(userData)
                        .when()
                        .post(LOGIN_USER);
        return response;
    }

    //Проверка ответа после успешной авторизации курьера
    @Step("Check Response after successful user login")
    public void checkResponseAfterUserLogin(Response response) {
        response.then().assertThat().statusCode(200).and().body("success", equalTo(true));
    }

    //Проверка ответа после попытки авторизации пользователя, если логин или пароль неверные или нет одного из полей
    @Step("Check Response after attempt logging in User with login or/and password missing or incorrect")
    public void checkResponseAfterLoggingInUserWithIncorrectData(Response response) {
        response.then().assertThat().statusCode(401).and().body("message", equalTo("email or password are incorrect"));
    }

    //Изменение данных пользователя
    @Step("Send PATCH-request to /api/auth/user to update User data")
    public Response sendPatchRequestToUpdateUserData(UserData userData, String accessToken) {
        Response response =
                given()
                        .spec(baseRequestSpec)
                        .header("Authorization", accessToken)
                        .body(userData)
                        .when()
                        .patch(USER_DATA_UPDATE);
        return response;
    }

    //Проверка ответа после изменений данных пользователя с авторизацией
    @Step("Check Response after update User Data")
    public void checkResponseAfterUpdateUserData(Response response) {
        response.then().assertThat().statusCode(200).and().body("success", equalTo(true));
    }
    //Проверка ответа после изменения почты пользователя
    @Step("Check response after update User's email")
    public void checkResponseBodyAfterUpdateUserEmail(Response response, String newEmail) {
        response.then().assertThat().body("user.email", equalTo(newEmail));
    }

    //Проверка ответа после изменения имени пользователя
    @Step("Check response after update User's Name")
    public void checkResponseBodyAfterUpdateUserName(Response response, String newName) {
        response.then().assertThat().body("user.name", equalTo(newName));
    }

    //Проверка ответа после попытки изменения данных пользователя без передачи токена авторизации
    @Step("Check Response after send request to update User data with no access token")
    public void checkResponseAfterUpdateUserDataWithoutAuthorization(Response response) {
        response.then().assertThat().statusCode(401).and().body("success", equalTo(false));
    }

    //Проверка ответа после попытки изменения данных пользователя с уже используемой почтой
    @Step("Check Response after send request to update User data with already used email")
    public void checkResponseAfterUpdateUserDataWithUsedEmail(Response response) {
        response.then().assertThat().statusCode(403).and().body("success", equalTo(false));
    }

    //Удаление пользователя
    @Step("Deleting User")
    public void deleteUser(String accessToken) {
        Response response =
                given()
                        .spec(baseRequestSpec)
                        .header("Authorization", accessToken)
                        .when()
                        .delete(DELETE_USER);
    }


}
