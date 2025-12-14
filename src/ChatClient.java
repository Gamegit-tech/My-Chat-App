/*This assignment uses two or more than clients by rerunning the client after running server.
   Server uses only for connecting clients.
*/




import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ChatClient {
    public static void main(String[] args) {

        String host = "localhost";

        int port = 5000;

        try {
            Socket socket = new Socket(host, port);
            BufferedReader serverReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter serverWriter = new PrintWriter(socket.getOutputStream(), true);

            Scanner scanner = new Scanner(System.in);

            // Thread to read from server
            Thread readThread = new Thread(() -> {
                try {
                    String msg;
                    while ((msg = serverReader.readLine()) != null) {
                        System.out.println(msg);
                    }
                } catch (IOException e) {
                    System.out.println("Disconnected from server.");
                }
            });

            readThread.start();

            // Write to server
            while (true) {
                String input = scanner.nextLine();
                serverWriter.println(input);

                if (input.equalsIgnoreCase("exit")) {
                    socket.close();
                    System.exit(0);
                    break;
                }
            }

        } catch (IOException e) {
            System.out.println("Connection Error: " + e.getMessage());
        }
    }
}
