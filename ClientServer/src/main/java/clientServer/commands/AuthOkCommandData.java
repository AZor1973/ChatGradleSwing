package clientServer.commands;

import java.io.Serial;
import java.io.Serializable;

public class AuthOkCommandData implements Serializable {

    @Serial
    private static final long serialVersionUID = -5932323515963186160L;
    private final String username;

    public AuthOkCommandData(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
