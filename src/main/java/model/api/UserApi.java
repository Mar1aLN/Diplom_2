package model.api;

import constants.Urls;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import model.body.EditUserRequestBody;
import model.body.LoginRequestBody;
import model.body.RegisterRequestBody;

import static io.restassured.RestAssured.given;

public class UserApi {
    private static final String REGISTER_URL = Urls.STELLAR_BURGERS_URL + Urls.REGISTER_HANDLE;

    private static final String LOGIN_URL = Urls.STELLAR_BURGERS_URL + Urls.LOGIN_HANDLE;

    private static final String DELETE_URL = Urls.STELLAR_BURGERS_URL + Urls.USER_HANDLE;

    private static final String EDIT_USER_URL = Urls.STELLAR_BURGERS_URL + Urls.USER_HANDLE;

    @Step("Регистрация пользователя")
    public static Response register(RegisterRequestBody registerRequestBody) {
        return given()
                .header("Content-type", "application/json")
                .body(registerRequestBody)
                .post(REGISTER_URL);
    }

    @Step("Логин пользователя")
    public static Response login(LoginRequestBody loginRequestBody) {
        return given()
                .header("Content-type", "application/json")
                .body(loginRequestBody)
                .post(LOGIN_URL);
    }

    @Step("Удаление пользователя")
    public static Response delete(String token) {
        return given()
                .auth()
                .oauth2(token)
                .header("Content-type", "application/json")
                .delete(DELETE_URL);
    }

    @Step("Попытка логина пользователя, при усехе - удаление пользователя")
    public static Response tryLoginAndDelete(LoginRequestBody loginRequestBody) {
        String token = loginAndGetToken(loginRequestBody);

        if (token != null) {
            Response res = UserApi.delete(token);
            return res;
        } else {
            return null;
        }
    }

    @Step("Изменение данных пользователя")
    public static Response editUser(EditUserRequestBody editUserRequestBody, String token) {
        return given()
                .auth()
                .oauth2(token)
                .header("Content-type", "application/json")
                .body(editUserRequestBody)
                .patch(EDIT_USER_URL);
    }

    @Step("Логин и получение токена")
    public static String loginAndGetToken(LoginRequestBody loginRequestBody) {
        String token = login(loginRequestBody)
                .path("accessToken");
        return trimToken(token);
    }

    @Step("Отсечение 'BEARER ' из начала токена")
    public static String trimToken(String token) {
        if (token == null) {
            return null;
        } else {
            return token.substring(7);
        }
    }

}
