package ruslan.kabirov.client.view;

import ruslan.kabirov.client.controller.ClientController;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class RegisterDialog extends JFrame {
    private JPanel contentPane;
    private JTextField loginTextField;
    private JTextField nicknameTextField;
    private JLabel loginLabel;
    private JLabel nicknameLabel;
    private JTextField passTextField;
    private JLabel passLabel;
    private JTextField pass2TextField;
    private JLabel pass2Label;
    private JButton OKButton;
    private JButton cancelButton;

    private ClientController controller;

    public RegisterDialog(ClientController controller) {
        this.controller = controller;
        setContentPane(contentPane);
        getRootPane().setDefaultButton(OKButton);
        setSize(400, 250);
        setLocationRelativeTo(null);


        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        cancelButton.addActionListener(e -> RegisterDialog.super.dispose());
    }
}
