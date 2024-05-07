package model.body;

import java.util.List;

public class Order {
    private List<Ingredient> ingredients;

    private Integer number;

    private float price;

    public Order() {
    }

    public Order(Integer number, float price, List<Ingredient> ingredients) {
        this.number = number;
        this.price = price;
        this.ingredients = ingredients;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}
