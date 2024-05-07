package model.api;

import constants.Urls;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import model.body.Ingredient;
import model.body.Ingredients;

import static io.restassured.RestAssured.given;

public class IngredientsApi {
    private static final String GET_INGREDIENTS_URL = Urls.STELLAR_BURGERS_URL + Urls.INGREDIENTS_HANDLE;

    @Step("Получение списка ингредиентов")
    public static Response getIngredientsList(){
        return given()
                .get(GET_INGREDIENTS_URL);
    }

    @Step("Получение первого доступного ингредиента заданного типа")
    public static Ingredient getFirstAvailableIngredient(String type){
        Ingredients ingredients = getIngredientsList()
                .getBody()
                .as(Ingredients.class);

        for (Ingredient ingredient : ingredients.getData()){
            if(ingredient.getType().equals(type)){
                return ingredient;
            }
        }
        return null;
    }


}
