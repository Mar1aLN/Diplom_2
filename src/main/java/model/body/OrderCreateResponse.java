package model.body;

public class OrderCreateResponse {
    private String name;

    private boolean success;

    private Order order;

    private float price;

    public OrderCreateResponse() {
    }

    public OrderCreateResponse(String name, boolean success, Order order, Integer number, float price) {
        this.name = name;
        this.success = success;
        this.order = order;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }


    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}
