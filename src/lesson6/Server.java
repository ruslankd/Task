package lesson6;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {

    static final int PORT = 9090;
    static Socket clientSocket = null;
    static BufferedReader in;
    static PrintWriter out;
    static ServerSocket serverSocket;

    public static void main(String[] args) {

        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Server is running, waiting for connection ...");
            clientSocket = serverSocket.accept();
            System.out.println("The client is connected.");

            Thread readThread = new Thread(Server::getMessageFromClientAndPrint);
            Thread writeThread = new Thread(Server::sendMessageToClient);

            readThread.start();
            writeThread.start();

            readThread.join();
            writeThread.join();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }



    }

    public static void getMessageFromClientAndPrint() {
        String message;
        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            while (true) {
                message = in.readLine();
                if (message.equals("/end")) {
                    clientSocket.close();
                    System.out.println(System.lineSeparator() + "Client disconnected.");
                    System.out.print("Server: ");
                    in.close();
                    break;
                }
                System.out.println(System.lineSeparator() + "Client: " + message);
                System.out.print("Server: ");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void sendMessageToClient() {
        Scanner scanner = new Scanner(System.in);
        String message;
        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);

            do {
                System.out.print("Server: ");
                message = scanner.nextLine();
                out.println(message);
            } while (!message.equals("quit") || !clientSocket.isClosed());
            clientSocket.close();
            serverSocket.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
