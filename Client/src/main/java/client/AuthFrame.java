package client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class AuthFrame extends JFrame {
    private String login;
    private char[] password;
    private final Network network = Network.getInstance();

    public AuthFrame() {
        setBounds(400, 100, 400, 250);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        setLayout(null);
        setTitle("Enter login and password");

        JTextField loginField = new JTextField();
        loginField.setFont(new Font(null, Font.PLAIN, 16));
        loginField.setBounds(45, 45, 300, 30);

        JPasswordField passField = new JPasswordField();
        passField.setFont(new Font(null, Font.PLAIN, 16));
        passField.setBounds(45, 90, 300, 30);

        loginField.addActionListener(e -> {
            if (!loginField.getText().isEmpty()) {
                passField.requestFocusInWindow();
                login = loginField.getText();
            }
        });

        loginField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_UP) {
                    passField.requestFocusInWindow();
                }
            }
        });

        passField.addActionListener(e -> {
            if (passField.getPassword().length != 0) {
                password = passField.getPassword();
                executeAuth(login, password);
                changeFrames();
            }
        });

        passField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_UP) {
                    loginField.requestFocusInWindow();
                }
            }
        });

        JButton okButton = new JButton("OK");
        okButton.setBounds(100, 145, 70, 30);

        okButton.addActionListener(e -> {
            if (passField.getPassword().length != 0) {
                executeAuth(login, password);
                changeFrames();
            }
        });

        JButton regButton = new JButton("Register");
        regButton.setBounds(190, 145, 100, 30);
        regButton.addActionListener(e -> EventQueue.invokeLater(RegisterFrame::new));

        add(loginField);
        add(passField);
        add(okButton);
        add(regButton);

        setVisible(true);
    }

    private void executeAuth(String login, char[] password) {
        String pass = String.valueOf(password);
        try {
            network.sendAuthMessage(login, pass);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void changeFrames() {
        java.util.Timer timer = new Timer(true);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                repaintThis();
                if (network.isAuthOk()) {
                    network.setLogin(login);
                    network.setPassword(String.valueOf(password));
                    setVisible(false);
                    EventQueue.invokeLater(ChatFrame::getInstance);
                    timer.cancel();
                }
            }
        }, 0, TimeUnit.SECONDS.toMillis(1));
    }

    private void repaintThis() {
        this.repaint();
    }
}
