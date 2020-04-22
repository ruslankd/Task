package ruslan.kabirov.server.networkserver;


import ruslan.kabirov.server.networkserver.auth.AuthService;
import ruslan.kabirov.server.networkserver.auth.BaseAuthService;
import ruslan.kabirov.server.networkserver.clienthandler.ClientHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class MyServer {

    private final int port;
    private final List<ClientHandler> clients;
    private final AuthService authService;

    public MyServer(int port) {
        this.port = port;
        this.clients = new ArrayList<>();
        this.authService = new BaseAuthService();
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server is running");
            authService.start();


            //noinspection InfiniteLoopStatement
            while (true) {
                System.out.println("Waiting for client connection...");
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client has been connected");
                ClientHandler handler = new ClientHandler(clientSocket, this);
                try {
                    handler.handle();
                } catch (IOException e) {
                    System.err.println("Failed to handle client connection!");
                    clientSocket.close();
                }
            }

        } catch (IOException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        } finally {
            authService.stop();
        }
    }

    public AuthService getAuthService() {
        return authService;
    }

    public boolean isNicknameBusy(String nickname) {
        for (ClientHandler client : clients) {
            if (client.getNickname().equals(nickname)) {
                return true;
            }
        }
        return false;
    }

    public synchronized void broadcastMessage(String message) throws IOException {
        for (ClientHandler client : clients) {
            client.sendMessage(message);
        }
    }

    public synchronized void subscribe(ClientHandler clientHandler) {
        clients.add(clientHandler);
    }

    public synchronized void unsubscribe(ClientHandler clientHandler) {
        clients.remove(clientHandler);
    }

    public synchronized void targetMessage(String nicknameFrom, String nicknameTo, String message) throws IOException {
        for (ClientHandler client : clients) {
            if (client.getNickname().equals(nicknameTo)) {
                client.sendTargetMessage(nicknameFrom, message);
                return;
            }
        }
    }
}
