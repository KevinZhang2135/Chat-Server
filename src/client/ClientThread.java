package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientThread extends Thread{
    private Socket socket;

    public ClientThread(Socket socket) throws IOException{
        this.socket = socket;
    }

    @Override
    public void run() {
        // Reads group chat from server
        try (BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()))){
            while (true) {
                String response = input.readLine();
                System.out.println(response);
            }
         
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
