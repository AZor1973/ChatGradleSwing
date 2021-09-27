package clientServer.commands;

import java.io.Serial;
import java.io.Serializable;

public class UpdateDatabaseCommandData implements Serializable {

    @Serial
    private static final long serialVersionUID = 8721829283925755939L;
    private final String newUsername;
    private final String oldUsername;


    public UpdateDatabaseCommandData(String newUsername, String oldUsername) {
        this.newUsername = newUsername;
        this.oldUsername = oldUsername;
    }

    public String getNewUsername() {
        return newUsername;
    }

    public String getOldUsername(){
        return oldUsername;
    }
}
