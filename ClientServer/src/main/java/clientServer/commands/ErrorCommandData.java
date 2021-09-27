package clientServer.commands;

import java.io.Serial;
import java.io.Serializable;

public class ErrorCommandData implements Serializable {

    @Serial
    private static final long serialVersionUID = 8880269547472797440L;
    private final String errorMessage;

    public ErrorCommandData(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
