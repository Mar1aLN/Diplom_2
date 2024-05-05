import constants.ResponseMessages;
import constants.StatusCodes;
import io.qameta.allure.junit4.DisplayName;
import model.api.UserApi;
import model.body.LoginRequestBody;
import model.body.RegisterRequestBody;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;

public class RegisterTest {
    private final String email = "Nikitina@email.org";

    private final String password = "12345";

    private final String username = "Мария";

    @Before
    public void before() {
        UserApi.tryLoginAndDelete(new LoginRequestBody(email, password));
    }

    @Test
    @DisplayName("Позитивная проверка создания пользователя")
    public void createNewUserTest() {
        UserApi.register(new RegisterRequestBody(email, password, username))
                .then()
                .assertThat()
                .statusCode(StatusCodes.REGISTER_OK)
                .assertThat().body("success", equalTo(true));
    }

    @Test
    @DisplayName("Попытка создать существующего пользователя")
    public void createExistingUserTest() {
        UserApi.register(new RegisterRequestBody(email, password, username));

        UserApi.register(new RegisterRequestBody(email, password, username))
                .then()
                .assertThat()
                .statusCode(StatusCodes.REGISTER_USER_EXISTS)
                .assertThat().body("success", equalTo(false))
                .assertThat().body("message", equalTo(ResponseMessages.REGISTER_USER_ALREADY_EXISTS));
    }

    @Test
    @DisplayName("Попытка создать пользователя без почтового адреса")
    public void createNewUserNoEmailTest() {
        RegisterRequestBody missingParamsRequestBody = new RegisterRequestBody("", password, username);

        UserApi.register(missingParamsRequestBody)
                .then()
                .assertThat()
                .statusCode(StatusCodes.REGISTER_MISSING_PARAMS)
                .assertThat().body("success", equalTo(false))
                .assertThat().body("message", equalTo(ResponseMessages.REGISTER_MISSING_PARAMS));
    }

    @Test
    @DisplayName("Попытка создать пользователя без пароля")
    public void createNewUserNoPasswordTest() {
        RegisterRequestBody missingParamsRequestBody = new RegisterRequestBody(email, "", username);

        UserApi.register(missingParamsRequestBody)
                .then()
                .assertThat()
                .statusCode(StatusCodes.REGISTER_MISSING_PARAMS)
                .assertThat().body("success", equalTo(false))
                .assertThat().body("message", equalTo(ResponseMessages.REGISTER_MISSING_PARAMS));
    }

    @Test
    @DisplayName("Попытка создать пользователя без имени")
    public void createNewUserNoNameTest() {
        RegisterRequestBody missingParamsRequestBody = new RegisterRequestBody(email, password, "");

        UserApi.register(missingParamsRequestBody)
                .then()
                .assertThat()
                .statusCode(StatusCodes.REGISTER_MISSING_PARAMS)
                .assertThat().body("success", equalTo(false))
                .assertThat().body("message", equalTo(ResponseMessages.REGISTER_MISSING_PARAMS));
    }

    @After
    public void after() {
        UserApi.tryLoginAndDelete(new LoginRequestBody(email, password));
    }
}
