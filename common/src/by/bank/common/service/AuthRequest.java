package by.bank.common.service;

public class AuthRequest extends Request{

    private String login;
    private String password;
    private String newPassword;

    public AuthRequest() {
        super(RequestType.LOGOUT);
    }

    public AuthRequest(String login, String password) {
        super(RequestType.LOGIN);
        this.login = login;
        this.password = password;
    }

    public AuthRequest(String login, String oldPassword, String newPassword) {
        super(RequestType.PASSWORD);
        this.login = login;
        this.password = oldPassword;
        this.newPassword = newPassword;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getNewPassword() {
        return newPassword;
    }
}
