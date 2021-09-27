package clientServer.commands;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

public class UpdateUsersListCommandData implements Serializable {

    @Serial
    private static final long serialVersionUID = -2803506189313020778L;
    private final List<String> users;

    public UpdateUsersListCommandData(List<String> users) {
        this.users = users;
    }

    public List<String> getUsers() {
        return users;
    }
}
