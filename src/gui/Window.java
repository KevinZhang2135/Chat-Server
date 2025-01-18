package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.net.InetAddress;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

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
    private JScrollPane scrollingInbox;

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

        this.username = username;
        this.hostAddress = hostAddress;
        this.port = port;

        setTitle(username); // setTitle(username + "@" + hostAddress.toString());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Displays items in a single column
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        setResizable(false);

        inbox = new Inbox(username);
        scrollingInbox = new JScrollPane(inbox);
        
        // Scroll
        JScrollBar verticalScroll = scrollingInbox.getVerticalScrollBar();
        verticalScroll.setPreferredSize(new Dimension(0, 0));
        verticalScroll.setUnitIncrement(16);

        scrollingInbox.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        // Border, margin, and background
        scrollingInbox.setBorder(new EmptyBorder(0, 0, 0, 0));

        scrollingInbox.setPreferredSize(SIZE);
        scrollingInbox.setBackground(Color.GREEN);

        add(scrollingInbox);
        add(input = new Input((message) -> addMessage(message)));

        pack(); // Resizes to set screen size
        setLocationRelativeTo(null); // Displays window in the center of the screen
        setVisible(true);
    }

    /**
     * Creates a new inbox message with the specified sender and message and displays it in the
     * inbox.
     * 
     * @param message The specified message
     */
    public void addMessage(String message) {
        if (inbox == null)
            return;

        inbox.addMessage(username, message);
    }

    public static void main(String[] args) {
        Window window = new Window("Test Long Username that May be Problem", null, 3000);
        SwingUtilities.invokeLater(() -> {
            for (int i = 1; i <= 12; i++) {
                // window.addMessage("message" + i);
            }
        });
    }

}
