import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.Date;
import java.util.Scanner;

public class Client {
    private final String username;
    private final InetAddress hostAddress;
    private final int port;

    private class ClientThread extends Thread {
        private final Socket socket;
        private boolean isRunning;

        /**
         * Initializes a client thread to communicate with a corresponding server thread via the
         * specified socket
         * 
         * @param socket The server socket to listen for
         */
        public ClientThread(Socket socket) {
            this.socket = socket;
            isRunning = true;
        }

        /**
         * Continuously listens for messages from the server and displays it them for the user
         */
        @Override
        public void run() {
            // Reads group chat from server
            try (BufferedReader serverInput =
                    new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

                while (isRunning) {
                    String response = serverInput.readLine();
                    if (isRunning && response != null) {
                        System.out.println("\r" + response);
                        System.out.print("You: ");
                    }
                }

            } catch (SocketException e) {
                System.out.println("Exited string entered");

            } catch (Exception e) {
                System.out.println("Safely caught in thread");
                e.printStackTrace();
            }
        }

        /**
         * Terminates the thread and ceases to receive messages from the server
         */
        public void kill() {
            isRunning = false;
        }
    }

    /**
     * 
     * Initializes the client with the specified username, host address, and port
     *
     * @param username The specified username of the client
     * @param hostAddress The host address of the server
     * @param port The port used by the server
     */
    public Client(String username, InetAddress hostAddress, int port) {
        this.username = username;
        this.hostAddress = hostAddress;
        this.port = port;
    }

    /**
     * <p>
     * Connects to chat server socket and begins exchanging messages.
     * </p>
     * 
     * <p>
     * A client thread receives and displays incoming messages from other users while outbound
     * messages from the user are handled by the server thread to be boardcasted.
     * </p>
     */
    public void connectToServer() {
        Scanner scanner = new Scanner(System.in);
        try (Socket socket = new Socket(hostAddress, port)) {
            // Output to server
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);

            // Sets up thread to receive messages from other users
            ClientThread clientThread = new ClientThread(socket);
            clientThread.start();

            // Listens for user input
            String message;
            while (clientThread.isAlive()) {
                System.out.print("You: ");
                message = scanner.nextLine();

                // User leaves server
                if (message.equals(Server.EXIT_STRING)) {
                    clientThread.kill();

                } else {
                    String timestamp = new Date().toString();
                    message = String.format("%s (%s): %s ", username, timestamp, message);
                    output.println(message);
                }


            }

            // Displays leave message to other users
            output.println(username + " has left.");

        } catch (IOException e) {
            e.printStackTrace();
        }

        catch (Exception e) {
            System.out.println("Safely caught in client");
            e.printStackTrace();

        } finally {
            scanner.close();
        }
    }

    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Arguments required: <username> <multicast-host> <port-number>");
            return;
        }

        // Extracts username, host address, and port number, from args
        String username = args[0];
        InetAddress hostAddress;
        int port;

        try {
            hostAddress = InetAddress.getByName(args[1]);
            port = Integer.parseInt(args[2]);

        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        // Creates and connects client to server
        Client client = new Client(username, hostAddress, port);
        client.connectToServer();
    }
}
