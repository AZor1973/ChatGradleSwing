package clientServer.commands;

import java.io.Serial;
import java.io.Serializable;

public class InsertCommandData implements Serializable{

    @Serial
    private static final long serialVersionUID = 7675503794246653334L;
    private final String username;
    private final String login;
    private final String password;

    public InsertCommandData(String username, String login, String password){
        this.username = username;
        this.login = login;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }
}
