package clientServer.commands;

import java.io.Serial;
import java.io.Serializable;

public class ClientMessageCommandData implements Serializable {

    @Serial
    private static final long serialVersionUID = 1669929777800146767L;
    private final String sender;
    private final String message;

    public ClientMessageCommandData(String sender, String message) {
        this.sender = sender;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String getSender() {
        return sender;
    }
}