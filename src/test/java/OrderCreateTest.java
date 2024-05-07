import constants.IngredientTypes;
import constants.StatusCodes;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import model.api.IngredientsApi;
import model.api.OrdersApi;
import model.api.UserApi;
import model.body.LoginRequestBody;
import model.body.OrderCreateRequest;
import model.body.RegisterRequestBody;
import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import service.RandomString;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.*;

@RunWith(Parameterized.class)
public class OrderCreateTest {
    private final String email = RandomString.getAlphaNumericString(5) + "@email.org";

    private final String password = "12345";

    private final String username = "Мария";

    private final boolean isTokenUsed;

    private final List<String> ingredients;

    private final int expectedStatusCode;

    private final Matcher<Boolean> successMatcher;

    private final Matcher<String> numberMatcher;

    private final String comment;

    private String loginToken;

    public OrderCreateTest(boolean isTokenUsed, List<String> ingredients, int expectedStatusCode, Matcher<Boolean> successMatcher, Matcher<String> numberMatcher, String comment) {
        this.isTokenUsed = isTokenUsed;
        this.ingredients = ingredients;
        this.expectedStatusCode = expectedStatusCode;
        this.successMatcher = successMatcher;
        this.numberMatcher = numberMatcher;
        this.comment = comment;
    }

    @Parameterized.Parameters(name = "{5}")
    public static Object[][] parameters() {
        return new Object[][]{
                {true
                        , Arrays.asList(IngredientsApi.getFirstAvailableIngredient(IngredientTypes.BUN).get_id(), IngredientsApi.getFirstAvailableIngredient(IngredientTypes.MAIN).get_id())
                        , StatusCodes.ORDER_CREATE_OK
                        , equalTo(true)
                        , notNullValue()
                        , "Позитивная проверка создания заказа: указаны ингредиенты, указан токен"
                },
                {false
                        , Arrays.asList(IngredientsApi.getFirstAvailableIngredient(IngredientTypes.BUN).get_id(), IngredientsApi.getFirstAvailableIngredient(IngredientTypes.MAIN).get_id())
                        , StatusCodes.ORDER_CREATE_NO_AUTH
                        , equalTo(false)
                        , emptyOrNullString()
                        , "Негативная проверка создания заказа: указаны ингредиенты, не указан токен"
                },
                {false
                        , new ArrayList<String>()
                        , StatusCodes.ORDER_CREATE_MISSING_PARAMS
                        , equalTo(false)
                        , emptyOrNullString()
                        , "Негативная проверка создания заказа: не указаны ингредиенты, не указан токен"
                },
                {false
                        , Collections.singletonList("12345")
                        , StatusCodes.ORDER_CREATE_WRONG_HASH
                        , anything()
                        , anything()
                        , "Негативная проверка создания заказа: указаны некорректные хэши ингредиентов, не указан токен"
                },
                {true
                        , Collections.singletonList("12345")
                        , StatusCodes.ORDER_CREATE_WRONG_HASH
                        , anything()
                        , anything()
                        , "Негативная проверка создания заказа: указаны некорректные хэши ингредиентов, указан токен"
                },
        };

    }

    @Before
    @Step("Создание пользователя перед проверкой заказа")
    public void before() {
        UserApi.register(new RegisterRequestBody(email, password, username));
        loginToken = UserApi.loginAndGetToken(new LoginRequestBody(email, password));
        Assert.assertNotNull(loginToken);
    }

    @Test()
    public void orderCreateTest() {
        Allure.getLifecycle().updateTestCase(testResult -> testResult.setName(comment));
        OrdersApi.create(new OrderCreateRequest(ingredients), isTokenUsed ? loginToken : "")
                .then()
                .assertThat()
                .statusCode(expectedStatusCode)
                .assertThat().body("success", successMatcher)
                .assertThat().body("order.number", numberMatcher);
    }

    @After
    public void after() {
        UserApi.tryLoginAndDelete(new LoginRequestBody(email, password));
    }

}
