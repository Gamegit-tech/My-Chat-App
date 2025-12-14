import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {

    private static HashMap<String, PrintWriter> clients = new HashMap<>();

    public static void main(String[] args) {

        int port = 5000;
        System.out.println("SERVER: Running on port " + port);

        try (ServerSocket serverSocket = new ServerSocket(port)) {

            while (true) {
                Socket socket = serverSocket.accept();

                // Create handler thread for each new client
                ClientHandler handler = new ClientHandler(socket);
                handler.start();
            }

        } catch (IOException e) {
            System.out.println("SERVER ERROR: " + e.getMessage());
        }
    }

    // Broadcast message to all users
    public static synchronized void broadcast(String message, String sender) {
        for (String user : clients.keySet()) {
            if (!user.equals(sender)) {
                clients.get(user).println(message);
            }
        }
    }

    // Send private message
    public static synchronized void privateMessage(String receiver, String message) {
        PrintWriter writer = clients.get(receiver);
        if (writer != null) {
            writer.println(message);
        }
    }

    // Add new client
    public static synchronized void addClient(String username, PrintWriter writer) {
        clients.put(username, writer);
    }

    // Remove disconnected client
    public static synchronized void removeClient(String username) {
        clients.remove(username);
    }
}

class ClientHandler extends Thread {
    private Socket socket;
    private String username;
    private BufferedReader reader;
    private PrintWriter writer;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);

            // Assign username
            writer.println("Enter a username:");
            username = reader.readLine();

            ChatServer.addClient(username, writer);
            ChatServer.broadcast("[SERVER] " + username + " has joined the chat!", username);

            writer.println("Welcome " + username + "! You can chat now.");
            writer.println("Private chat: @username message");

            String message;

            while ((message = reader.readLine()) != null) {

                if (message.equalsIgnoreCase("exit")) {
                    break;
                }

                // Private message format: @username message
                if (message.startsWith("@")) {
                    int spaceIndex = message.indexOf(" ");
                    if (spaceIndex != -1) {
                        String targetUser = message.substring(1, spaceIndex);
                        String privateMsg = message.substring(spaceIndex + 1);
                        ChatServer.privateMessage(targetUser,
                                "[PRIVATE from " + username + "] " + privateMsg);
                    } else {
                        writer.println("Invalid private message format.");
                    }
                } else {
                    // Public message
                    ChatServer.broadcast(username + ": " + message, username);
                }
            }

        } catch (IOException e) {
            System.out.println("Client disconnected.");
        } finally {
            try {
                ChatServer.removeClient(username);
                ChatServer.broadcast("[SERVER] " + username + " left the chat.", username);
                socket.close();
            } catch (IOException e) {
            }
        }
    }
}
