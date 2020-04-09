package Lesson4;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class Controller {

    @FXML
    public Button send;

    @FXML
    private TextArea chatArea;

    @FXML
    private TextField textField;

    @FXML
    private void handleButtonAction() {
        chatArea.appendText(textField.getText() + System.lineSeparator());
        textField.clear();
    }

}
