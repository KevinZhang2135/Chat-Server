import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    public static final String EXIT_STRING = "exit";

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Arguments required: <port-number>");
            return;
        }

        // Extracts port number form args
        int port = Integer.parseInt(args[0]);

        // Runs server
        ArrayList<ServerThread> serverThreadPool = new ArrayList<>();
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server running on " + InetAddress.getLocalHost());

            // Continuously listens for new socket connections
            while (true) {
                Socket socket = serverSocket.accept(); // blocks until new connection is made
                ServerThread serverThread = new ServerThread(socket, serverThreadPool);

                serverThreadPool.add(serverThread);
                serverThread.start();
                
                System.out.println("Number of clients: " + serverThreadPool.size());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
