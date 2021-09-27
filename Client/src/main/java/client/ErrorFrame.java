package client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.nio.charset.Charset;

public class ErrorFrame extends JFrame {
    public ErrorFrame(String errorMessage) {
        setBounds(400, 150, 400, 150);
        setResizable(false);
        setTitle("Error");
        JLabel label = new JLabel();
        label.setText(errorMessage);
        label.setFont(new Font(null, Font.PLAIN, 16));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
               if (e.getKeyCode() == KeyEvent.VK_ENTER){
                   setVisible(false);
               }
            }
        });
        add(label, BorderLayout.CENTER);
        setVisible(true);
    }
}
