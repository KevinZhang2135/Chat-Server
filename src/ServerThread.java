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

                // When the user enters the exit string
                // Removes thread from server pool and terminates
                if (outputString == null) {
                    serverThreadPool.remove(this);
                    break;
                }

                outputString = outputString.strip().toLowerCase();
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

            // If the address is not the sender
            if (!thread.socket.equals(socket))
                thread.output.println(outputString);
        }
    }

    public Socket getSocket() {
        return socket;
    }

    public ArrayList<ServerThread> getServerThreadPool() {
        return serverThreadPool;
    }

    public PrintWriter getOutput() {
        return output;
    }
}
