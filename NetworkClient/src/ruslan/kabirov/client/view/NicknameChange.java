package ruslan.kabirov.client.view;

import ruslan.kabirov.client.controller.ClientController;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class NicknameChange extends JFrame {
    private JPanel mainPanel;
    private JButton OKButton;
    private JButton cancelButton;
    private JTextField textField1;

    private ClientController controller;

    public NicknameChange(ClientController controller) {
        this.controller = controller;
        setTitle(controller.getUsername());
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setSize(275, 100);
        setLocationRelativeTo(null);
        setContentPane(mainPanel);

        cancelButton.addActionListener(e -> NicknameChange.super.dispose());
        OKButton.addActionListener(e -> onOK());
    }

    public void onOK() {
        controller.changeNickname(textField1.getText());
    }


}
