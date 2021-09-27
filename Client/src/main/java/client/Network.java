package client;

import clientServer.Command;
import clientServer.CommandType;
import clientServer.commands.*;

import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Network {

    private static final int SERVER_PORT = 8189;
    private static final String SERVER_HOST = "localhost";

    private static Network INSTANCE;

    private final String host;
    private final int port;
    private Socket socket;
    private ObjectInputStream socketInput;
    private ObjectOutputStream socketOutput;
    private ExecutorService executorService;
    private boolean connected;
    private boolean authOk;
    private String username;
    private String message;
    private String login;
    private String password;
    private final ArrayList<String> usersList = new ArrayList<>();


    public static Network getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Network();
        }
        return INSTANCE;
    }

    private Network(String host, int port) {
        this.host = host;
        this.port = port;
    }

    private Network() {
        this(SERVER_HOST, SERVER_PORT);
    }

    public void connect() {
        try {
            socket = new Socket(host, port);
            socketOutput = new ObjectOutputStream(socket.getOutputStream());
            socketInput = new ObjectInputStream(socket.getInputStream());
            executorService = Executors.newSingleThreadExecutor();
            this.startReadMessageProcess();
            connected = true;
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to establish connection");
        }
    }

    public boolean isConnected() {
        return connected;
    }

    public boolean isAuthOk() {
        return authOk;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMessage(){
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String[] getUsersList() {
        String[] array = new String[usersList.size()];
        this.usersList.toArray(array);
        return array;
    }

    private void startReadMessageProcess() {
        executorService.execute(() -> {
            System.out.println(Thread.currentThread().getName());
            while (true) {
                try {
                    if (Thread.currentThread().isInterrupted()) {
                        break;
                    }
                    Command command = readCommand();
                    if (command == null) {
                        continue;
                    }
                    if (command.getType() == CommandType.AUTH_OK) {
                        AuthOkCommandData data = (AuthOkCommandData) command.getData();
                        authOk = true;
                        username = data.getUsername();
                    }
                    if (command.getType() == CommandType.UPDATE_USERS_LIST) {
                        UpdateUsersListCommandData data = (UpdateUsersListCommandData) command.getData();
                        usersList.removeAll(usersList);
                        usersList.addAll(data.getUsers());
                    }
                    if (command.getType() == CommandType.CLIENT_MESSAGE){
                        ClientMessageCommandData data = (ClientMessageCommandData) command.getData();
                        message = data.getSender() + ": " + data.getMessage();
                        System.out.println(message);
                    }
                    if (command.getType() == CommandType.ERROR){
                        ErrorCommandData data = (ErrorCommandData) command.getData();
                        System.out.println(data.getErrorMessage());
                        EventQueue.invokeLater(() -> new ErrorFrame(data.getErrorMessage()));
                    }
                } catch (IOException e) {
                    System.err.println("Failed to read message from server");
                    close();
                    break;
                }
            }
        });
    }

    private Command readCommand() throws IOException {
        Command command = null;
        try {
            command = (Command) socketInput.readObject();
        } catch (ClassNotFoundException e) {
            System.err.println("Failed to read ClientServer.Command class");
            e.printStackTrace();
        }
        return command;
    }

    public void sendPrivateMessage(String recipient, String message) throws IOException {
        sendCommand(Command.privateMessageCommand(recipient, message));
    }

    public void sendMessage(String message) throws IOException {
        sendCommand(Command.publicMessageCommand(message));
    }

    public void sendAuthMessage(String login, String password) throws IOException {
        sendCommand(Command.authCommand(login, password));
    }

    public void sendNewUsername(String newUsername, String oldUsername) throws IOException {
        sendCommand(Command.updateDatabaseCommand(newUsername, oldUsername));
    }

    private void sendCommand(Command command) throws IOException {
        try {
            socketOutput.writeObject(command);
        } catch (IOException e) {
            System.err.println("Failed to send message to server");
            throw e;
        }
    }

    public void close() {
        try {
            connected = false;
            authOk = false;
            socket.close();
            executorService.shutdown();
            System.out.println("Executor closed");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}