

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
    public static final Dimension SIZE = new Dimension(360, 720);
    public static final Dimension SCREEN_MARGIN = new Dimension(20, 10);
    public static final Dimension INPUT_SIZE = new Dimension(SIZE.width, 80);

    /* Colors */
    public static final Color BACKGROUND_COLOR = new Color(0x101516);
    public static final Color CLEAR = new Color(0x00000000, true);

    /* Fonts */
    public final static Font SANS_SERIF_16 = new Font("SansSerif", Font.PLAIN, 16);

    private Inbox inbox;
    private Input input;

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
            try (ObjectInputStream serverDispatcher =
                    new ObjectInputStream(socket.getInputStream())) {

                while (isRunning) {
                    // Displays message from server
                    Message message = (Message) serverDispatcher.readObject();
                    if (message != null)
                        addMessage(message);
                }

            } catch (SocketException e) {
                e.printStackTrace();

            } catch (IOException | ClassNotFoundException e) {
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

        setTitle(username); // setTitle(username + "@" + hostAddress.toString());
        // setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

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
        add(input = new Input(username));

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
            ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());

            // Sets up thread to receive messages from other users
            ClientThread clientThread = new ClientThread(socket);
            clientThread.start();

            // Closes client thread on window close
            addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    clientThread.isRunning = false;
                }
            });

            // Listens for user input and posts it to the server
            input.setButtonCallback((message) -> {
                try {
                    output.writeObject(message);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            // Continuously listens until the thread is dead
            while (clientThread.isAlive()) {
                Thread.sleep(1000); // Prevents the loop from taxing too many resources
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a new inbox message with the specified sender and message and displays it in the
     * inbox.
     * 
     * @param message The specified message
     */
    public void addMessage(Message message) {
        if (inbox == null)
            return;

        inbox.addMessage(message);
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
