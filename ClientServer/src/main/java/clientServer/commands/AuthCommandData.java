package clientServer.commands;

import java.io.Serial;
import java.io.Serializable;

public class AuthCommandData implements Serializable {

    @Serial
    private static final long serialVersionUID = -7329410015217194425L;
    private final String login;
    private final String password;

    public AuthCommandData(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }
}
