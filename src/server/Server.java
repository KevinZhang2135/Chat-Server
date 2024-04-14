package server;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Server {
    public static final String EXIT_STRING = "exit";

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

        } catch (UnknownHostException e){
            e.printStackTrace();
        }
    }
}
