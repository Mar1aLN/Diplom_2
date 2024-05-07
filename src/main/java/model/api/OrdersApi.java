package model.api;

import constants.Urls;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import model.body.OrderCreateRequest;

import static io.restassured.RestAssured.given;

public class OrdersApi {
    private static final String ORDER_CREATE_URL = Urls.STELLAR_BURGERS_URL + Urls.ORDERS_HANDLE;

    private static final String GET_ORDERS_URL = Urls.STELLAR_BURGERS_URL + Urls.ORDERS_HANDLE;

    @Step("Создание заказа")
    public static Response create(OrderCreateRequest orderCreateRequest, String token) {
        return given()
                .auth()
                .oauth2(token)
                .header("Content-type", "application/json")
                .body(orderCreateRequest)
                .post(ORDER_CREATE_URL);
    }

    @Step("Получение списка заказов")
    public static Response getList(String token) {
        return given()
                .auth()
                .oauth2(token)
                .header("Content-type", "application/json")
                .get(GET_ORDERS_URL);
    }
}
