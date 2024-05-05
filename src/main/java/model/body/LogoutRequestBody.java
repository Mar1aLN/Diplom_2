package model.body;

public class LogoutRequestBody {
    private String token;

    public LogoutRequestBody() {
    }

    public LogoutRequestBody(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
