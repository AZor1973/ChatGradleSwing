package clientServer;

public enum CommandType {
    AUTH,
    AUTH_OK,
    ERROR,
    PUBLIC_MESSAGE,
    PRIVATE_MESSAGE,
    CLIENT_MESSAGE,
    END,
    UPDATE_USERS_LIST,
    UPDATE_DATABASE
}
