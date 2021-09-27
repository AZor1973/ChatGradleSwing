package client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;

public class ChangeNickFrame extends JFrame {
    private final Network network = Network.getInstance();

    public ChangeNickFrame() {
        setBounds(400, 150, 400, 150);
        setResizable(false);
        setTitle("Enter new nick and press Enter");
        JTextField textField = new JTextField();
        textField.setFont(new Font(null, Font.PLAIN, 16));
        textField.setHorizontalAlignment(SwingConstants.CENTER);
        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    try {
                        network.sendNewUsername(textField.getText(), network.getUsername());
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    network.setUsername(textField.getText());
                    setVisible(false);
                }
            }
        });
        add(textField, BorderLayout.CENTER);
        setVisible(true);
    }
}
