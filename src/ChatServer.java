
import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ChatServer {
    public static void main(String[] args) {
        int port = 5000;

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("SERVER: Listening on port " + port);

            Socket clientSocket = serverSocket.accept();
            System.out.println("SERVER: Client connected from " + clientSocket.getInetAddress());

            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
            Scanner serverScanner = new Scanner(System.in);

            writer.println("SERVER: Connection established. Start chatting! (type 'exit' to quit)");

            // Thread to continuously read messages from client
            Thread readThread = new Thread(() -> {
                try {
                    String clientMessage;
                    while ((clientMessage = reader.readLine()) != null) {
                        if (clientMessage.equalsIgnoreCase("exit")) {
                            System.out.println("CLIENT: disconnected.");
                            break;
                        }
                        // FIX: Use \r to return to the start of the line and clear it
                        System.out.print("\rCLIENT: " + clientMessage + "\n");
                        System.out.print("Server: "); // Reprint the prompt (assuming you add a prompt below)
                    }
                } catch (IOException e) {
                    System.out.println("SERVER: Connection closed.");
                }
            });

            readThread.start();

            // Main thread handles sending messages
            while (true) {
                // ADDED: A consistent prompt for the server
                System.out.print("Server: ");
                String serverMessage = serverScanner.nextLine();
                writer.println("SERVER: " + serverMessage);
                if (serverMessage.equalsIgnoreCase("exit")) {
                    break;
                }
            }

            clientSocket.close();
            System.exit(0);
        } catch (IOException ex) {
            System.err.println("SERVER Error: " + ex.getMessage());
        }
    }
}