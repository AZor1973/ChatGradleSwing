package clientServer.commands;

import java.io.Serial;
import java.io.Serializable;

public class PublicMessageCommandData implements Serializable {

    @Serial
    private static final long serialVersionUID = -4407339719000760554L;
    private final String message;

    public PublicMessageCommandData(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}
