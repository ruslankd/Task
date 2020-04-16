package lesson6;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;

public class Client {
    static final String SERVER_ADDR = "localhost";
    static final int CONNECTION_TIMEOUT = 10000;
    static Socket socket = null;
    static BufferedReader in;
    static PrintWriter out;
    static Scanner scanner;

    public static void main(String[] args) {

        try {
            socket = new Socket();
            socket.connect(new InetSocketAddress(SERVER_ADDR, Server.PORT), CONNECTION_TIMEOUT);
            System.out.println("Connected to the server.");

            Thread readThread = new Thread(Client::getMessageFromServerAndPrint);
            Thread writeThread = new Thread(Client::sendMessageToServer);

            readThread.start();
            writeThread.start();

            readThread.join();
            writeThread.join();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void sendMessageToServer() {
        scanner = new Scanner(System.in);
        String message;
        try {
            out = new PrintWriter(socket.getOutputStream(), true);

            do {
                System.out.print("Client: ");
                message = scanner.nextLine();
                out.println(message);
            } while (!message.equals("/end"));
            socket.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void getMessageFromServerAndPrint() {
        String message;
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            while (true) {
                message = in.readLine();
                System.out.println(System.lineSeparator() + "Server: " + message);
                System.out.print("Client: ");
            }
        } catch (SocketException e) {
            System.out.println("Disconnected.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
