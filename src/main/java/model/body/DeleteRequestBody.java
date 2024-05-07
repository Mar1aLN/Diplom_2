package model.body;

public class DeleteRequestBody {
    private String token;

    public DeleteRequestBody() {
    }

    public DeleteRequestBody(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
