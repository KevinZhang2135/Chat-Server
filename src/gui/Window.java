package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.net.InetAddress;
import javax.swing.JFrame;

public class Window extends JFrame {
    public static final Dimension SIZE = new Dimension(600, 1000);
    public static final int GUTTER_WIDTH = 50; // Left and right margin of the screen

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
        setResizable(false);

        setBackground(new Color(0x101516));
        getContentPane().setPreferredSize(SIZE);

        String longMessage = """
                Lorem ipsum dolor sit amet, consectetur adipiscing elit,
                sed do eiusmod tempor incididunt ut labore et dolore
                magna aliqua. Ut enim ad minim veniam, quis nostrud
                exercitation ullamco laboris nisi ut aliquip ex ea
                commodo consequat. Duis aute irure dolor in
                reprehenderit in voluptate velit esse cillum dolore eu
                fugiat nulla pariatur. Excepteur sint occaecat cupidatat
                non proident, sunt in culpa qui officia deserunt mollit
                anim id est laborum""";

        String shortMessage = "Lorem ipsum dolor sit amet";
        
        add(new TextBox(username, "Sender", longMessage));

        // Displays window in the center of the screen
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        new Window("Username", null, 3000);
    }
}
