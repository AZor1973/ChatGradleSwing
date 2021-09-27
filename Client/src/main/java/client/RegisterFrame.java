package client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;

public class RegisterFrame extends JFrame {
    private String login;
    private char[] password;
    private String username;
    private final Network network = Network.getInstance();

    public RegisterFrame() {
        setBounds(400, 100, 400, 250);
        setResizable(false);
        setLayout(null);
        setTitle("Enter login and password");

        JLabel userLabel = new JLabel("User (Nick):");
        userLabel.setFont(new Font(null, Font.PLAIN, 16));
        userLabel.setBounds(45, 35, 90, 30);
        JLabel loginLabel = new JLabel("Login:");
        loginLabel.setFont(new Font(null, Font.PLAIN, 16));
        loginLabel.setBounds(45, 90, 90, 30);
        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(new Font(null, Font.PLAIN, 16));
        passLabel.setBounds(45, 145, 90, 30);

        JTextField userField = new JTextField();
        userField.setFont(new Font(null, Font.PLAIN, 16));
        userField.setBounds(140, 35, 200, 30);

        JTextField loginField = new JTextField();
        loginField.setFont(new Font(null, Font.PLAIN, 16));
        loginField.setBounds(140, 90, 200, 30);

        JPasswordField passField = new JPasswordField();
        passField.setFont(new Font(null, Font.PLAIN, 16));
        passField.setBounds(140, 145, 200, 30);

        userField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    loginField.requestFocusInWindow();
                }
                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    passField.requestFocusInWindow();
                }
            }
        });

        userField.addActionListener(e -> {
            if (!userField.getText().isEmpty()) {
                loginField.requestFocusInWindow();
                username = userField.getText();
            }
        });

        loginField.addActionListener(e -> {
            if (!loginField.getText().isEmpty()) {
                passField.requestFocusInWindow();
                login = loginField.getText();
            }
        });

        loginField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    passField.requestFocusInWindow();
                }
                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    userField.requestFocusInWindow();
                }
            }
        });

        passField.addActionListener(e -> {
            if (passField.getPassword().length != 0) {
                password = passField.getPassword();
                try {
                    network.addNewUser(username, login, String.valueOf(password));
                    setVisible(false);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        passField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    userField.requestFocusInWindow();
                }
                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    loginField.requestFocusInWindow();
                }
            }
        });

        add(userLabel);
        add(loginLabel);
        add(passLabel);
        add(userField);
        add(loginField);
        add(passField);

        setVisible(true);
    }
}
