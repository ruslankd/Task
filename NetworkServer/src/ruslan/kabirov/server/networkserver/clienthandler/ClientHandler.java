package ruslan.kabirov.server.networkserver.clienthandler;

import org.apache.log4j.Logger;
import ruslan.kabirov.Command;
import ruslan.kabirov.command.AuthCommand;
import ruslan.kabirov.command.BroadcastMessageCommand;
import ruslan.kabirov.command.ChangeNicknameCommand;
import ruslan.kabirov.command.PrivateMessageCommand;
import ruslan.kabirov.server.networkserver.MyServer;
import ruslan.kabirov.server.networkserver.auth.AuthService;

import java.io.*;
import java.net.Socket;
import java.sql.SQLException;

public class ClientHandler {

    private static final Logger logger = Logger.getLogger(ClientHandler.class);

    private static final int TIME_FOR_AUTHORIZATION = 120000;
    private final MyServer serverInstance;
    private final AuthService authService;
    private final Socket clientSocket;

    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private String nickname;


    public ClientHandler(Socket clientSocket, MyServer myServer) {
        this.clientSocket = clientSocket;
        this.serverInstance = myServer;
        this.authService = serverInstance.getAuthService();
    }

    public void handle() throws IOException {
        inputStream = new ObjectInputStream(clientSocket.getInputStream());
        outputStream = new ObjectOutputStream(clientSocket.getOutputStream());

        new Thread(() -> {
            try {
                authentication();
                readMessages();
            } catch (Exception e) {
                System.out.println("Connection has been failed");
            } finally {
                closeConnection();
            }
        }).start();
    }

    private void closeConnection() {
        try {
            serverInstance.unsubscribe(this);
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readMessages() throws IOException, SQLException {
        while (true) {
            Command command = readCommand();
            logger.info("Received command from " + nickname);
            if (command == null) {
                continue;
            }
            switch (command.getType()) {
                case END:
                    return;
                case BROADCAST_MESSAGE:
                    BroadcastMessageCommand data = (BroadcastMessageCommand) command.getData();
                    serverInstance.broadcastMessage(Command.messageCommand(nickname, data.getMessage()));
                    break;
                case PRIVATE_MESSAGE:
                    PrivateMessageCommand privateMessageCommand = (PrivateMessageCommand) command.getData();
                    String receiver = privateMessageCommand.getReceiver();
                    String message = privateMessageCommand.getMessage();
                    serverInstance.sendPrivateMessage(receiver, Command.messageCommand(nickname, message));
                    break;
                case CHANGE_NICKNAME:
                    ChangeNicknameCommand changeNicknameCommand = (ChangeNicknameCommand) command.getData();
                    String newNickname = changeNicknameCommand.getNewNickname();
                    authService.changeNickname(nickname, newNickname);
                    serverInstance.broadcastMessage(Command.messageCommand(nickname, "changed the nickname to \"" +
                            newNickname + "\""));
                    serverInstance.unsubscribe(this);
                    this.nickname = newNickname;
                    serverInstance.subscribe(this);
                    break;
                default:
                    String errorMessage = "Unknown type of command: " + command.getType();
                    logger.warn(errorMessage);
                    sendMessage(Command.errorCommand(errorMessage));
            }
        }
    }

    private void authentication() throws IOException, SQLException {

        Thread t = new Thread(this::trackTimeout);
        t.start();

        while (true) {

            Command command = readCommand();
            if (command == null) {
                continue;
            }
            switch (command.getType()) {
                case AUTH:
                    if (processAuthCommand(command)) {
                        t.interrupt();
                        return;
                    }
                    break;
                default:
                    String errorMessage = "Illegal command for authentication: " + command.getType();
                    logger.warn(errorMessage);
                    sendMessage(Command.errorCommand(errorMessage));
            }
        }
    }

    private void trackTimeout() {
        try {
            Thread.sleep(TIME_FOR_AUTHORIZATION);
            String errorMessage = "Authorization timed out";
            sendMessage(Command.authErrorCommand(errorMessage));
            logger.warn(errorMessage);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            System.out.println();
        }
    }

    private boolean processAuthCommand(Command command) throws IOException, SQLException {
        AuthCommand authCommand = (AuthCommand) command.getData();
        String login = authCommand.getLogin();
        String password = authCommand.getPassword();
        String nickname = authService.getNickByLoginAndPassword(login, password);
        if (nickname == null) {
            sendMessage(Command.authErrorCommand("Неверные логин/пароль!"));
        } else if (serverInstance.isNicknameBusy(nickname)) {
            sendMessage(Command.authErrorCommand("Учетная запись уже используется!"));
        } else {
            authCommand.setUsername(nickname);
            sendMessage(command);
            setNickname(nickname);
            serverInstance.broadcastMessage(Command.messageCommand(null, nickname + " Зашел в чат!"));
            serverInstance.subscribe(this);
            return true;
        }
        return false;
    }

    private Command readCommand() throws IOException {
        try {
            return (Command) inputStream.readObject();
        } catch (ClassNotFoundException e) {
            String errorMessage = "Unknown type of object from client!";
            logger.warn(errorMessage);
            e.printStackTrace();
            sendMessage(Command.errorCommand(errorMessage));
            return null;
        }
    }

    private void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }

    public void sendMessage(Command command) throws IOException {
        outputStream.writeObject(command);
    }
}
