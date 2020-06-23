package ruslan.kabirov.client.view;

import ruslan.kabirov.client.controller.ClientController;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

public class AuthDialog extends JFrame {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField loginText;
    private JPasswordField passwordText;
    private JLabel labelSignUp;

    private ClientController controller;

    public AuthDialog(ClientController controller) {
        this.controller = controller;
        setContentPane(contentPane);
        getRootPane().setDefaultButton(buttonOK);
        setSize(400, 250);
        setLocationRelativeTo(null);

        buttonOK.addActionListener(e -> onOK());
        buttonCancel.addActionListener(e -> onCancel());
        labelSignUp.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                onSignUp();
            }
        });

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });
    }

    private void onOK() {
        String login = loginText.getText().trim();
        String pass = new String(passwordText.getPassword()).trim();
        try {
            controller.sendAuthMessage(login, pass);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Ошибка при попытке аутентификации");
        }
    }

    private void onCancel() {
        System.exit(0);
    }

    private void onSignUp() {
        controller.registerUser();
    }

    public void showError(String errorMessage) {
        JOptionPane.showMessageDialog(this, errorMessage);
    }
}
