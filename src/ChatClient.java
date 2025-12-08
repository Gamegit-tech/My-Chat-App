
import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ChatClient {
    public static void main(String[] args) {
        String hostname = "localhost";
        int port = 5000;

        try (Socket socket = new Socket(hostname, port)) {

            BufferedReader serverReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter serverWriter = new PrintWriter(socket.getOutputStream(), true);
            Scanner userScanner = new Scanner(System.in);

            // Thread to continuously read messages from server
            Thread readThread = new Thread(() -> {
                try {
                    String serverMessage;
                    while ((serverMessage = serverReader.readLine()) != null) {
                        // FIX: Use \r to return to the start of the line and clear it
                        // This allows incoming messages to interrupt user typing
                        System.out.print("\r" + serverMessage + "\n");
                        System.out.print("You: "); // Reprint the prompt
                        if (serverMessage.contains("Closing connection")) {
                            break;
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Connection closed by server.");
                }
            });

            readThread.start();

            // Main thread handles sending user messages
            while (true) {
                System.out.print("You: ");
                String userInput = userScanner.nextLine();
                serverWriter.println(userInput);
                if (userInput.equalsIgnoreCase("exit")) {
                    break;
                }
            }

            socket.close();
            System.exit(0);
        } catch (UnknownHostException ex) {
            System.err.println("Client Error: Server not found.");
        } catch (IOException ex) {
            System.err.println("Client I/O Error: " + ex.getMessage());
        }
    }
}