import constants.ResponseMessages;
import constants.StatusCodes;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import model.api.UserApi;
import model.body.LoginRequestBody;
import model.body.RegisterRequestBody;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import service.RandomString;

import static org.hamcrest.Matchers.equalTo;

public class LoginTest {
    private final String email = RandomString.getAlphaNumericString(5) + "@email.org";
    private final String password = "12345";
    private final String username = "Мария";

    @Before
    @Step("Создание пользователя для теста логина")
    public void before() {
        UserApi.register(new RegisterRequestBody(email, password, username));
    }

    @Test
    @DisplayName("Позитивный тест логина")
    public void testCorrectLogin() {
        UserApi.login(new LoginRequestBody(email, password))
                .then()
                .assertThat()
                .statusCode(StatusCodes.LOGIN_OK)
                .assertThat().body("success", equalTo(true));
    }

    @Test
    @DisplayName("Попытка логина с ошибочным почтовым адресом")
    public void testLoginWrongEmail() {
        UserApi.login(new LoginRequestBody("ERROR@mail.ru", password))
                .then()
                .assertThat()
                .statusCode(StatusCodes.LOGIN_EMAIL_OR_PASSWORD_INCORRECT)
                .assertThat().body("success", equalTo(false))
                .assertThat().body("message", equalTo(ResponseMessages.EMAIL_OR_PASSWORD_INCORRECT));
    }

    @Test
    @DisplayName("Попытка логина с ошибочным паролем")
    public void testLoginWrongPassword() {
        UserApi.login(new LoginRequestBody(email, "aaaaa"))
                .then()
                .assertThat()
                .statusCode(StatusCodes.LOGIN_EMAIL_OR_PASSWORD_INCORRECT)
                .assertThat().body("success", equalTo(false))
                .assertThat().body("message", equalTo(ResponseMessages.EMAIL_OR_PASSWORD_INCORRECT));
    }

    @After
    @Step("Удаление пользователя после теста")
    public void after() {
        UserApi.tryLoginAndDelete(new LoginRequestBody(email, password));
    }
}
