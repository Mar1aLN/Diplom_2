package constants;

public class StatusCodes {
    public static final int REGISTER_OK = 200;

    public static final int REGISTER_USER_EXISTS = 403;

    public static final int REGISTER_MISSING_PARAMS = 403;

    public static final int LOGIN_OK = 200;

    public static final int LOGIN_EMAIL_OR_PASSWORD_INCORRECT = 401;

    public static final int EDIT_USER_OK = 200;

    public static final int EDIT_USER_NO_AUTH = 401;

    public static final int ORDER_CREATE_OK = 200;

    public static final int ORDER_CREATE_NO_AUTH = 401;

    public static final int ORDER_CREATE_MISSING_PARAMS = 400;

    public static final int ORDER_CREATE_WRONG_HASH = 500;

    public static final int ORDERS_GET_OK = 200;

    public static final int ORDERS_GET_NO_AUTH = 401;
}
