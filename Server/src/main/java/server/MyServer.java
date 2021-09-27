package server;

import clientServer.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyServer {
    private static final Logger logger = LoggerFactory.getLogger(MyServer.class);
    private final List<ClientHandler> clients = new ArrayList<>();
    private DatabaseService databaseService;
    private ExecutorService executorService;

    public void start(int port) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            logger.info("Server has been started");
            databaseService = new DatabaseService();
            executorService = Executors.newCachedThreadPool();
            while (true) {
                waitAndProcessNewClientConnection(serverSocket);
            }
        } catch (IOException e) {
            logger.error("Failed to bind port " + port);
            e.printStackTrace();
        }
        finally {
            databaseService.closeConnection();
            executorService.shutdown();
        }
    }

    private void waitAndProcessNewClientConnection(ServerSocket serverSocket) throws IOException {
        logger.info("Waiting for new client connection...");
        Socket clientSocket = serverSocket.accept();
        logger.info("Client has been connected");
        ClientHandler clientHandler = new ClientHandler(this, clientSocket);
        clientHandler.handle();
    }

    public synchronized void broadcastMessage(String message, ClientHandler sender) throws IOException {
        for (ClientHandler client : clients) {
            if (client != sender) {
                client.sendCommand(Command.clientMessageCommand(sender.getUsername(), message));
            }
        }
    }

    public synchronized void subscribe(ClientHandler clientHandler) throws IOException {
        if (!clients.contains(clientHandler))
            clients.add(clientHandler);
        notifyClientsUsersListUpdated();
        System.out.println(clients);
    }

    public synchronized void unsubscribe(ClientHandler clientHandler) throws IOException {
        clients.remove(clientHandler);
        notifyClientsUsersListUpdated();
        System.out.println(clients);
    }

    public DatabaseService getDatabaseService() {
        return databaseService;
    }

    public synchronized boolean isUsernameBusy(String username) {
        for (ClientHandler client : clients) {
            if (client.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    public synchronized void sendPrivateMessage(ClientHandler sender, String recipient, String privateMessage) throws IOException {
        for (ClientHandler client : clients) {
            if (client != sender && client.getUsername().equals(recipient)) {
                client.sendCommand(Command.clientMessageCommand(sender.getUsername(), privateMessage));
                break;
            }
        }
    }

    private void notifyClientsUsersListUpdated() throws IOException {
        List<String> users = new ArrayList<>();
        for (ClientHandler client : clients) {
            users.add(client.getUsername());
        }

        for (ClientHandler client : clients) {
            client.sendCommand(Command.updateUsersListCommand(users));
        }

    }

    public ExecutorService getExecutorService() {
        return executorService;
    }
}
