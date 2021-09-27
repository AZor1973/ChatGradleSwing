package client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class ChatFrame extends JFrame {
    private static ChatFrame instance;
    private String recipient;
    private String selectedListElement;
    private final Network network = Network.getInstance();

    private ChatFrame() {
        setBounds(400, 100, 600, 400);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        JTextArea chatArea = new JTextArea();
        chatArea.setFont(new Font(null, Font.PLAIN, 16));
        JScrollPane chatPane = new JScrollPane(chatArea);

        JTextArea messageArea = new JTextArea("Input message ...");
        messageArea.setFont(new Font(null, Font.PLAIN, 16));
        messageArea.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        messageArea.setLineWrap(true);
        messageArea.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                messageArea.setText("");
            }
        });
        messageArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    e.consume();
                    if (e.isShiftDown()) {
                        messageArea.append(System.lineSeparator());
                    } else {
                        chatArea.append(timeStamp() + "\n");
                        chatArea.append("Me: " + messageArea.getText() + "\n");
                        try {
                            if (recipient == null || recipient.equals("Mailing")) {
                                network.sendMessage(messageArea.getText());
                            } else {
                                network.sendPrivateMessage(recipient, messageArea.getText());
                            }
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                        messageArea.setText("");
                    }
                }
            }
        });

        DefaultListModel<String> l = new DefaultListModel<>();
        JList<String> list = new JList<>(l);
        JScrollPane listPane = new JScrollPane(list);
        list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        list.addListSelectionListener(e -> {
            selectedListElement = list.getSelectedValue();
            if (selectedListElement != null && !selectedListElement.equals(getTitle())) {
                recipient = selectedListElement;
            } else {
                if (selectedListElement != null) {
                    recipient = "Mailing";
                }
            }
            if (!chatArea.getText().endsWith("To " + recipient + ": \n")) {
                chatArea.append("To " + recipient + ": \n");
            }
        });

        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Options");
        JMenuItem reconnect = new JMenuItem("Reconnect");
        JMenuItem newNick = new JMenuItem("New nick");
        menu.add(reconnect);
        menu.add(newNick);
        menuBar.add(menu);
        reconnect.addActionListener(e -> reconnectServer());
        newNick.addActionListener(e -> EventQueue.invokeLater(ChangeNickFrame::new));

        add(menuBar, BorderLayout.NORTH);
        add(listPane, BorderLayout.WEST);
        add(chatPane, BorderLayout.CENTER);
        add(messageArea, BorderLayout.SOUTH);
        Timer timer = new Timer(true);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                setTitle(network.getUsername());
                list.removeAll();
                list.setListData(network.getUsersList());
                if (network.getMessage() != null) {
                    chatArea.append(timeStamp() + "\n");
                    chatArea.append(network.getMessage() + "\n");
                    network.setMessage(null);
                }
            }
        }, 0, TimeUnit.SECONDS.toMillis(1));

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                network.close();
            }
        });

        setVisible(true);
    }

    public static void getInstance() {
        if (instance == null) {
            instance = new ChatFrame();
        }
    }

    private void reconnectServer() {
        if (!network.isConnected()) {
            network.connect();
            try {
                network.sendAuthMessage(network.getLogin(), network.getPassword());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String timeStamp() {
        return LocalDateTime.now().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT));
    }
}
