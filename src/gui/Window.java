package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.net.InetAddress;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

/**
 * The main display of the texting application based on Google Messages. It consists of a scrollable
 * log of previous messages in the group chat with a sticky bottom input to send new messages.
 */
public class Window extends JFrame {
    /* Dimension and size constants */
    public static final Dimension SIZE = new Dimension(360, 800);
    public static final Dimension SCREEN_MARGIN = new Dimension(20, 10);

    /* Colors */
    public static final Color BACKGROUND_COLOR = new Color(0x101516);
    public static final Color TEXT_COLOR = new Color(0xe3e2e5);

    private final String username;
    private final InetAddress hostAddress;
    private final int port;

    /* Fonts */
    public final static Font SANS_SERIF_16 = new Font("SansSerif", Font.PLAIN, 16);

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

        super();

        this.username = username;
        this.hostAddress = hostAddress;
        this.port = port;

        setTitle(username); // setTitle(username + "@" + hostAddress.toString());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        getContentPane().setPreferredSize(SIZE); // Enforces the screen size
        setResizable(false);

        add(new Inbox(username));

        pack(); // Resizes to set screen size
        setLocationRelativeTo(null); // Displays window in the center of the screen
        setVisible(true);
    }

    public static void main(String[] args) {
        new Window("User", null, 3000);
    }
}
