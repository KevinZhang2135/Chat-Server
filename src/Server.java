import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

/**
 * The server listens for and accepts new client connections to the server socket and creates a
 * server dispatcher for each of them to handle messages.
 */
public class Server {
    public static final String EXIT_STRING = "exit";
    // public static final int NUM_THREADS = 10; // TODO

    private int port;
    private ArrayList<ServerDispatcher> serverThreads;

    /**
     * The dispatcher handles new messages from clients and forwards them to all clients connected
     * to the server.
     */
    private class ServerDispatcher extends Thread {
        private Socket socket;
        private PrintWriter output;

        /**
         * Initializes a thread at a specified socket to listen for client messages
         * 
         * @param socket The specified socket to listen for
         */
        public ServerDispatcher(Socket socket) {
            this.socket = socket;
        }

        /**
         * Continuously listens from its client and broadcasts messages to all other clients
         */
        @Override
        public void run() {
            // Reads input from client
            try (BufferedReader input =
                    new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                // Gets output from client
                output = new PrintWriter(socket.getOutputStream(), true);

                // Continuously listens for client input
                while (true) {
                    String outputString = input.readLine();

                    // Removes thread from server pool when the user enters the exit
                    // string
                    if (outputString == null) {
                        serverThreads.remove(this);
                        break;
                    }

                    outputString = outputString.strip().toLowerCase();
                    printToClients(outputString, socket);
                }

            } catch (SocketException e) {
                // Removes itself if there is a socket exception
                socket = null;
                serverThreads.remove(this);

            } catch (Exception e) {
                System.out.println("Exception caught in server thread.");
                e.printStackTrace();
            }
        }

        /**
         * Displays client input to all other clients on the chat server
         * 
         * @param outputString
         */
        private void printToClients(String outputString, Socket socket) {
            for (ServerDispatcher thread : serverThreads)
                thread.output.println(outputString);
        }
    }

    /**
     * Initializes a chat server with the specified port and no currently running threads
     * 
     * @param port The specified port number used by the server socket
     */
    public Server(int port) {
        this.port = port;
        serverThreads = new ArrayList<>();
    }

    /**
     * Starts listening for new clients joining the server and faciliates communication between them
     */
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            InetAddress localHost = InetAddress.getLocalHost();
            System.out.println("Server running on " + localHost);

            // Continuously listens for new socket connections from new users
            while (true) {
                Socket socket = serverSocket.accept(); // Blocks until new connection is made
                ServerDispatcher serverThread = new ServerDispatcher(socket);

                serverThreads.add(serverThread);
                serverThread.start();

                System.out.println("Number of clients: " + serverThreads.size());
                System.out.println(serverThreads + "\n");
            }

        } catch (Exception e) {
            System.out.println("Exception caught in server.");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Arguments required: <port-number>");
            return;
        }

        // Extracts port number form args
        int port = Integer.parseInt(args[0]);

        // Creates and runs server
        Server server = new Server(port);
        server.run();
    }
}
