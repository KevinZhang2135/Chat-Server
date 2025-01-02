package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.net.InetAddress;
import javax.swing.JFrame;

/**
 * The main display of the texting application based on Google Messages. It consists of a
 * scrollable log of previous messages in the group chat with a sticky bottom input to send new
 * messages.
 */
public class Window extends JFrame {
    /* Dimension and size constants */
    public static final Dimension SIZE = new Dimension(360, 800);

    public static final Dimension SCREEN_MARGIN = new Dimension(50, 10); // Margins of the screen
    public static final Dimension BOX_MARGIN = new Dimension(20, 10); // Margins of text box

    // Maximum width of the text box
    public static final int TEXT_BOX_WIDTH = SIZE.width - SCREEN_MARGIN.width * 2;

    // Maximum width of the text label
    public static final int TEXT_WIDTH = TEXT_BOX_WIDTH - BOX_MARGIN.width * 2;

    // Radius of the rounded text box corners
    public static final int BORDER_RADIUS = (int) (TEXT_BOX_WIDTH * 0.02);

    /* Colors */
    public static final Color BACKGROUND_COLOR = new Color(0x101516);
    public static final Color BOX_COLOR = new Color(0x282a2d);
    public static final Color TEXT_COLOR = new Color(0xe3e2e5);

    // Colors used for the name of message senders
    public static enum SenderColors {
        PINK(0xf791b8), ORANGE(0xfeb580), SAND(0xd6cb75), LIME(0xc9d87b), TEAL(0x75c4bb), INDIGO(
                0x8ea4e2), PERIWINKLE(0xaea1f3);

        public final Color color;

        private SenderColors(int hex) {
            color = new Color(hex);
        }
    };

    private final String username;
    private final InetAddress hostAddress;
    private final int port;


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
        new Window("Username", null, 3000);
    }
}
