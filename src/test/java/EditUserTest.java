import constants.ResponseMessages;
import constants.StatusCodes;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import model.api.UserApi;
import model.body.EditUserRequestBody;
import model.body.LoginRequestBody;
import model.body.RegisterRequestBody;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.hamcrest.Matchers.equalTo;

@RunWith(Parameterized.class)
public class EditUserTest {
    private final String email;

    private final String password;

    private final String username;

    private final String newEmail;

    private final String newPassword;

    private final String newUsername;

    private final String comment;

    private String token = null;

    public EditUserTest(String email, String password, String username, String newEmail, String newPassword, String newUsername, String comment) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.newEmail = newEmail;
        this.newPassword = newPassword;
        this.newUsername = newUsername;
        this.comment = comment;
    }

    @Parameterized.Parameters(name = "{6}")
    public static Object[][] parameters() {
        return new Object[][]{
                {"Nikitina@email.org", "12345", "Мария", "newMail@email.org", "12345", "Мария", "Изменение e-mail"},
                {"Nikitina@email.org", "12345", "Мария", "Nikitina@email.org", "qwerty", "Мария", "Изменение пароля"},
                {"Nikitina@email.org", "12345", "Мария", "Nikitina@email.org", "12345", "Маруся", "Изменение имени"},
        };
    }

    @Step("Попытка лолгина после изменения данных")
    private void tryLoginAfterEdit() {
        UserApi.login(new LoginRequestBody(newEmail, newPassword))
                .then()
                .assertThat()
                .statusCode(StatusCodes.LOGIN_OK)
                .assertThat().body("success", equalTo(true));
    }

    @Step("Удаление пользователя")
    private void tryDeleteUser() {
        //При наличии токена, используем его для удаления пользоателя
        if (token != null)
            UserApi.delete(token);

        //Проверяем наличие и пытается удалить все возможные варианты логина и пароля
        UserApi.tryLoginAndDelete(new LoginRequestBody(email, password));
        UserApi.tryLoginAndDelete(new LoginRequestBody(email, newPassword));
        UserApi.tryLoginAndDelete(new LoginRequestBody(newEmail, password));
        UserApi.tryLoginAndDelete(new LoginRequestBody(newEmail, newPassword));
    }

    //Создание пользователя для теста
    @Before
    public void before() {
        tryDeleteUser();

        //Создаем пользователя и получаем токен
        token = UserApi.trimToken(UserApi.register(new RegisterRequestBody(email, password, username))
                .path("accessToken"));


    }

    @Test
    @DisplayName("Позитивная проверка")
    public void positiveTest() {
        Allure.getLifecycle().updateTestCase(testResult -> testResult.setName("Позитивная проверка:" + comment));
        EditUserRequestBody editUserRequestBody = new EditUserRequestBody(newEmail, newPassword, newUsername);
        UserApi.editUser(editUserRequestBody, token)
                .then()
                .assertThat()
                .statusCode(StatusCodes.EDIT_USER_OK)
                .assertThat().body("success", equalTo(true))
                .assertThat().body("user.email", equalTo(editUserRequestBody.getEmail().toLowerCase()))
                .assertThat().body("user.name", equalTo(editUserRequestBody.getName()));

        tryLoginAfterEdit();
    }

    @Test
    @DisplayName("Негативная проверка: изменение данных без токена аутентификации")
    public void noAuthTest() {
        Allure.getLifecycle().updateTestCase(testResult -> testResult.setName("Негативная проверка(отсутствует токен):" + comment));
        UserApi.editUser(new EditUserRequestBody(newEmail, newPassword, newUsername), "")
                .then()
                .assertThat()
                .statusCode(StatusCodes.EDIT_USER_NO_AUTH)
                .assertThat().body("success", equalTo(false))
                .assertThat().body("message", equalTo(ResponseMessages.EDIT_USER_NO_AUTH));
    }

    @After
    public void after() {
        tryDeleteUser();
    }
}