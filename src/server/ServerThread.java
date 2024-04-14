package server;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ServerThread extends Thread {
    private Socket socket;
    private ArrayList<ServerThread> serverThreadPool;
    private PrintWriter output;

    public ServerThread(Socket socket, ArrayList<ServerThread> serverThreads) {
        this.socket = socket;
        this.serverThreadPool = serverThreads;
    }

    @Override
    public void run() {
        // Reads input from client
        try (BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            // Returns output to client
            output = new PrintWriter(socket.getOutputStream(), true);

            // Continuously listens for client input
            while (true) {
                String outputString = input.readLine();
                if (outputString != null)
                    outputString = outputString.strip().toLowerCase();

                if (outputString.equals(Server.EXIT_STRING)) {
                    break;
                }

                printToAllClients(outputString);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Displays client input to all clients on the chat server
     * @param outputString
     */
    private void printToAllClients(String outputString) {
        for (ServerThread thread : serverThreadPool) {
            thread.output.println(outputString);
        }
    }
}
