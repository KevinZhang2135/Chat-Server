import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Arguments required: <multicast-host> <port-number>");
            return;
        }

        // Extracts host address and port number form args
        InetAddress hostAddress;
        int port = Integer.parseInt(args[1]);
        try {
            hostAddress = InetAddress.getByName(args[0]);

        } catch (UnknownHostException e) {
            e.printStackTrace();
            return;
        }

        // Runs thread
        try (Socket socket = new Socket(hostAddress, port)) {
            // Reads text input from local machine
            Scanner scanner = new Scanner(System.in);

            // Gets username
            System.out.print("Enter name: ");
            String username = scanner.nextLine();
            
            // Sets up thread
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
            ClientThread clientThread = new ClientThread(socket);
            clientThread.start();

            // Listens for user input
            String message;
            while (true) {
                System.out.print("You: ");
                message = scanner.nextLine();
                
                // User leaves server
                if (message.equals(Server.EXIT_STRING)) {
                    output.println(username + " has left.");
                    break;
                }

                output.println(String.format("%s (%s): %s ", username, new Date().toString(), message));
            }

            scanner.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void backspace(int times) {
        for (int i = 0; i < times; i++) {
            System.out.print("\b \b");
        }
    }
}
