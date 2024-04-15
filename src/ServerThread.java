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
            // Gets output from client
            output = new PrintWriter(socket.getOutputStream(), true);

            // Continuously listens for client input
            while (true) {
                String outputString = input.readLine();
                if (outputString != null) {
                    outputString = outputString.strip().toLowerCase();

                    if (outputString.equals(Server.EXIT_STRING))
                        break;
                }

                printToClients(outputString, socket);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Displays client input to all other clients on the chat server
     * @param outputString
     */
    private void printToClients(String outputString, Socket socket) {
        for (int i = 0; i < serverThreadPool.size(); i++) {
            ServerThread thread = serverThreadPool.get(i);

            // Prunes closed sockets
            if (thread.output == null) {
                serverThreadPool.remove(i);
                i--;

                continue;
            }

            // If the address is not the sender
            if (!thread.socket.equals(socket))
                thread.output.println(outputString);
        }
    }
}
