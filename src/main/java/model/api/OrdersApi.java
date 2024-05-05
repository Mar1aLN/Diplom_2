package model.api;

import constants.Urls;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import model.body.OrderCreateRequest;

import static io.restassured.RestAssured.given;

public class OrdersApi {
    private static final String orderCreateUrl = Urls.STELLAR_BURGERS_URL + Urls.ORDERS_HANDLE;

    private static final String getOrdersUrl = Urls.STELLAR_BURGERS_URL + Urls.ORDERS_HANDLE;

    @Step("Создание заказа")
    public static Response create(OrderCreateRequest orderCreateRequest, String token) {
        return given()
                .auth()
                .oauth2(token)
                .header("Content-type", "application/json")
                .body(orderCreateRequest)
                .post(orderCreateUrl);
    }

    @Step("Получение списка заказов")
    public static Response getList(String token) {
        return given()
                .auth()
                .oauth2(token)
                .header("Content-type", "application/json")
                .get(getOrdersUrl);
    }
}
