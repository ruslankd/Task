package ruslan.kabirov.client.controller;

import ruslan.kabirov.Command;
import ruslan.kabirov.client.model.NetworkService;
import ruslan.kabirov.client.view.AuthDialog;
import ruslan.kabirov.client.view.ClientChat;
import ruslan.kabirov.client.view.NicknameChange;

import javax.swing.*;
import java.io.IOException;
import java.util.List;

public class ClientController {

    private static final String ALL_USERS_LIST_ITEM = "All";
    private final NetworkService networkService;
    private final AuthDialog authDialog;
    private final ClientChat clientChat;
    private final NicknameChange nicknameChangeDialog;
    private String nickname;

    public ClientController(String serverHost, int serverPort) {
        this.networkService = new NetworkService(serverHost, serverPort, this);
        this.authDialog = new AuthDialog(this);
        this.clientChat = new ClientChat(this);
        this.nicknameChangeDialog = new NicknameChange(this);
    }

    public void runApplication() throws IOException {
        connectToServer();
        runAuthProcess();
    }

    private void runAuthProcess() {
        networkService.setSuccessfulAuthEvent(nickname -> {
            ClientController.this.setUserName(nickname);
            ClientController.this.openChat();
        });
        authDialog.setVisible(true);

    }

    private void openChat() {
        authDialog.dispose();
        networkService.setMessageHandler(clientChat::appendMessage);
        clientChat.setVisible(true);
    }

    private void setUserName(String nickname) {
        SwingUtilities.invokeLater(() -> clientChat.setTitle(nickname));
        this.nickname = nickname;
    }

    private void connectToServer() throws IOException {
        try {
            networkService.connect();
        } catch (IOException e) {
            System.err.println("Failed to establish server connection");
            throw e;
        }
    }

    public void sendAuthMessage(String login, String pass) throws IOException {
        sendCommand(Command.authCommand(login, pass));
    }

    public void sendMessage(String message) {
        sendCommand(Command.broadcastMessageCommand(message));
    }

    public void sendPrivateMessage(String username, String message) {
        sendCommand(Command.privateMessageCommand(username, message));
    }

    public void sendCommand(Command command) {
        try {
            networkService.sendCommand(command);
        } catch (IOException e) {
            showErrorMessage(e.getMessage());
        }
    }

    public void shutdown() {
        if (authDialog.isEnabled()) {
            authDialog.setVisible(false);
            authDialog.dispose();
            System.exit(0);
        }
        networkService.close();
    }

    public String getUsername() {
        return nickname;
    }

    public void showErrorMessage(String errorMessage) {
        if (clientChat.isEnabled()) {
            clientChat.showError(errorMessage);
        } else if (authDialog.isEnabled()) {
            authDialog.showError(errorMessage);
        }
        System.err.println(errorMessage);
    }

    public void updateUsersList(List<String> users) {
        users.remove(nickname);
        users.add(0, ALL_USERS_LIST_ITEM);
        clientChat.updateUsers(users);
    }

    public void openChangeNicknameDialog() {
        nicknameChangeDialog.setVisible(true);
    }

    public void changeNickname(String newNickname) {
        networkService.changeNickname(newNickname);
        SwingUtilities.invokeLater(() -> clientChat.setTitle(newNickname));
        this.nickname = newNickname;
        nicknameChangeDialog.dispose();
    }
}
