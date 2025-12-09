This is a basic command-line chat application developed in Java using the java.net.Socket and java.net.ServerSocket classes. 
It provides a simple, two-way communication channel between a single server instance and a single client.

The application uses multi-threading to handle sending and receiving messages concurrently,
ensuring that incoming messages are displayed immediately without interrupting the user's ability to type a response.

Features:
- Two-Way Communication: Allows real-time message exchange between the Server and one Client.
- Non-Blocking I/O: Utilizes separate threads for reading incoming network data, preventing the console from freezing while waiting for user input.
- Console Synchronization: Implements console handling to ensure incoming messages properly interrupt and redraw the command prompt.
