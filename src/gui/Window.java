package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.net.InetAddress;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * The main display of the texting application based on Google Messages. It consists of a scrollable
 * log of previous messages in the group chat with a sticky bottom input to send new messages.
 */
public class Window extends JFrame {
    /* Dimension and size constants */
    public static final Dimension SIZE = new Dimension(360, 800);
    public static final Dimension SCREEN_MARGIN = new Dimension(20, 10);
    public static final Dimension INPUT_SIZE = new Dimension(SIZE.width, 80);

    /* Colors */
    public static final Color BACKGROUND_COLOR = new Color(0x101516);
    public static final Color CLEAR = new Color(0x00000000, true);

    private final String username;
    private final InetAddress hostAddress;
    private final int port;

    /* Fonts */
    public final static Font SANS_SERIF_16 = new Font("SansSerif", Font.PLAIN, 16);

    private Inbox inbox;
    private Input input;

    /**
     * Inititals and opens the graphics interface for texting
     * 
     * @param username
     * @param hostAddress
     * @param port
     */
    public Window(String username, InetAddress hostAddress, int port) {
        // if (username == null || hostAddress == null)
        // throw new IllegalArgumentException();

        // Prevents excessively long usernames
        // if (username.length > 30)
        // throw new IllegalArgumentException();

        super();

        this.username = username;
        this.hostAddress = hostAddress;
        this.port = port;

        setTitle(username); // setTitle(username + "@" + hostAddress.toString());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Displays items in a single column
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        getContentPane().setPreferredSize(SIZE); // Enforces the screen size
        setResizable(false);

        getContentPane().setBackground(BACKGROUND_COLOR);

        add(inbox = new Inbox(username));
        add(input = new Input(username));

        pack(); // Resizes to set screen size
        setLocationRelativeTo(null); // Displays window in the center of the screen
        setVisible(true);
    }

    /**
     * Creates a new inbox message with the specified sender and message and displays it in the
     * inbox.
     * 
     * @param sender The specified username of the sender
     * @param message The specified message
     */
    public void addMessage(String sender, String message) {
        if (inbox == null)
            return;

        for (int i = 1; i <= 3; i++)
            inbox.addMessage(sender + i, message + i);
    }

    public static void main(String[] args) {
        Window window = new Window("User", null, 3000);
        SwingUtilities.invokeLater(() -> window.addMessage("sender", "message"));
    }

}
