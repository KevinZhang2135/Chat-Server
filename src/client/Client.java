package client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import server.Server;

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
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);

            ClientThread clientThread = new ClientThread(socket);
            clientThread.start();

            // Gets username
            System.out.print("Enter name: ");
            String clientName = scanner.nextLine();

            // Listens for user input
            String message;
            while (true) {
                System.out.print(String.format("\"%s\": ", clientName));
                message = scanner.nextLine();
                
                // User leaves server
                if (message.equals(Server.EXIT_STRING)) {
                    output.println(String.format("\"%s\" has left.", clientName));
                    break;
                }

                output.println(String.format("Message : %s", message));

                
            }

            scanner.close();

        } catch (IOException e) {
            e.printStackTrace();

        }

    }
}
