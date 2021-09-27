package client;

import clientServer.Command;

public interface ReadCommandListener {

    void processReceivedCommand(Command command);
}
