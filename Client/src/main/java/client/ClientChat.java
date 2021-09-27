package client;

import java.awt.*;

public class ClientChat {
    public static void main(String[] args) {
        EventQueue.invokeLater(AuthFrame::new);
        Network.getInstance().connect();
    }
}
