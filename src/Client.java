

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

/**
 * The main display of the texting application based on Google Messages. It consists of a scrollable
 * log of previous messages in the group chat with a sticky bottom input to send new messages.
 */
public class Client extends JFrame {
    /* Dimension and size constants */
    public static final Dimension SIZE = new Dimension(360, 800);
    public static final Dimension SCREEN_MARGIN = new Dimension(20, 10);
    public static final Dimension INPUT_SIZE = new Dimension(SIZE.width, 80);

    /* Colors */
    public static final Color BACKGROUND_COLOR = new Color(0x101516);
    public static final Color CLEAR = new Color(0x00000000, true);

    /* Fonts */
    public final static Font SANS_SERIF_16 = new Font("SansSerif", Font.PLAIN, 16);

    private Inbox inbox;
    private Input input;

    private String username;

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
            try (BufferedReader serverDispatcher =
                    new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

                while (isRunning) {
                    String message = serverDispatcher.readLine(); // Displays message from server
                    if (message != null)
                        addMessage(username, message);
                }

            } catch (SocketException e) {
                System.out.println("Exited string entered.");

            } catch (Exception e) {
                System.out.println("Exception caught in client thread.");
                e.printStackTrace();
            }
        }
    }

    /**
     * Initializes and opens the graphics interface for texting. Does not allow usernames beyond 40
     * characters.
     * 
     * @param username The specified username of the client
     */
    public Client(String username) {
        // Prevents excessively long usernames
        if (username.length() > 40)
            throw new IllegalArgumentException();

        this.username = username;

        setTitle(username); // setTitle(username + "@" + hostAddress.toString());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Displays items in a single column
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        setResizable(false);

        // Creates scrolling view for inbox
        JScrollPane scrollingInbox = new JScrollPane(inbox = new Inbox(username));

        // Configuring scroll
        JScrollBar verticalScroll = scrollingInbox.getVerticalScrollBar();
        verticalScroll.setPreferredSize(new Dimension(0, 0)); // Hides vertical scroll
        verticalScroll.setUnitIncrement(16);

        // Disables horizontal scroll
        scrollingInbox.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        // Border and margin
        scrollingInbox.setBorder(new EmptyBorder(0, 0, 0, 0));
        scrollingInbox.setPreferredSize(SIZE);

        add(scrollingInbox);
        add(input = new Input());

        pack(); // Resizes to set screen size
        setLocationRelativeTo(null); // Displays window in the center of the screen
        setVisible(true);
    }

    /**
     * Connects to chat server socket and begins exchanging messages.
     * 
     * @param hostAddress The host address of the server
     * @param port The port used by the server
     */
    public void connectToServer(InetAddress hostAddress, int port) {
        try (Socket socket = new Socket(hostAddress, port)) {
            // Output to server
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);

            // Sets up thread to receive messages from other users
            ClientThread clientThread = new ClientThread(socket);
            clientThread.start();

            // Listens for user input and posts it to the server
            input.setButtonCallback(output::println);

            // Continuously listens until the thread is dead
            while (clientThread.isAlive()) {}

        } catch (IOException e) {
            e.printStackTrace();
            
        } catch (Exception e) {
            System.out.println("Exception caught in client.");
            e.printStackTrace();
        }
    }

    /**
     * Creates a new inbox message with the specified sender and message and displays it in the
     * inbox.
     * 
     * @param username The specified username of the sender
     * @param message The specified message
     */
    public void addMessage(String username, String message) {
        if (inbox == null)
            return;

        inbox.addMessage(username, message);
    }

    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println(args.length);
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

        Client window = new Client(username);
        window.connectToServer(hostAddress, port);
    }

}
