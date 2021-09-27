package clientServer.commands;

import java.io.Serial;
import java.io.Serializable;

public class PrivateMessageCommandData implements Serializable {

    @Serial
    private static final long serialVersionUID = -2348962852794165192L;
    private final String receiver;
    private final String message;

    public PrivateMessageCommandData(String receiver, String message) {
        this.receiver = receiver;
        this.message = message;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getMessage() {
        return message;
    }
}