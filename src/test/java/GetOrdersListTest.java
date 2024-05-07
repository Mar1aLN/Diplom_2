import constants.IngredientTypes;
import constants.StatusCodes;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import model.api.IngredientsApi;
import model.api.OrdersApi;
import model.api.UserApi;
import model.body.LoginRequestBody;
import model.body.OrderCreateRequest;
import model.body.RegisterRequestBody;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import service.RandomString;

import java.util.Arrays;

import static org.hamcrest.Matchers.equalTo;

public class GetOrdersListTest {
    private final String email = RandomString.getAlphaNumericString(5) + "@email.org";

    private final String password = "12345";

    private final String username = "Мария";

    private String loginToken;

    @Before
    @Step("Создание пользователя перед проверкой получения заказов пользователя")
    public void before() {
        UserApi.tryLoginAndDelete(new LoginRequestBody(email, password));
        UserApi.register(new RegisterRequestBody(email, password, username));
        loginToken = UserApi.loginAndGetToken(new LoginRequestBody(email, password));
        Assert.assertNotNull(loginToken);
    }

    @Test
    @DisplayName("Получение списка заказов текущего пользователя")
    public void getOrdersOkTest() {
        OrdersApi.getList(loginToken)
                .then()
                .assertThat()
                .statusCode(StatusCodes.ORDERS_GET_OK)
                .assertThat().body("success", equalTo(true))
                .assertThat().body("orders.size()", equalTo(0));

        OrdersApi.create(new OrderCreateRequest(Arrays.asList(IngredientsApi.getFirstAvailableIngredient(IngredientTypes.BUN).get_id(), IngredientsApi.getFirstAvailableIngredient(IngredientTypes.MAIN).get_id())), loginToken);

        OrdersApi.create(new OrderCreateRequest(Arrays.asList(IngredientsApi.getFirstAvailableIngredient(IngredientTypes.BUN).get_id(), IngredientsApi.getFirstAvailableIngredient(IngredientTypes.MAIN).get_id())), loginToken);

        OrdersApi.getList(loginToken)
                .then()
                .assertThat()
                .statusCode(StatusCodes.ORDERS_GET_OK)
                .assertThat().body("success", equalTo(true))
                .assertThat().body("orders.size()", equalTo(2));
    }

    @Test
    @DisplayName("Попытка получения заказов без токена аутентификации")
    public void getOrdersNoAuthTest() {
        OrdersApi.getList("")
                .then()
                .assertThat()
                .statusCode(StatusCodes.ORDERS_GET_NO_AUTH)
                .assertThat().body("success", equalTo(false))
                .assertThat().body("message", equalTo("You should be authorised"));
    }

    @After
    public void after() {
        UserApi.tryLoginAndDelete(new LoginRequestBody(email, password));
    }
}
