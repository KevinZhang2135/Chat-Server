package gui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.RoundRectangle2D;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class Inbox extends JPanel {
    private String username;

    /**
     * 
     */
    public class TextBox extends JPanel {
        private String sender;
        private JLabel text;
        private RoundRectangle2D box;

        /**
         * Initializes and displays a text box as a rounded rectangle with a message and name of the
         * sender. If the sender is the user, it is aligned on the right of the screen.
         * 
         * @param username The name of the user
         * @param sender The sender of the message
         * @param message The message content
         */
        public TextBox(String sender, String message) {
            this.sender = sender;

            // Formats message as sans-serif in 16-pt font HTML tags are used to enforce text
            // wrapping
            add(text = new JLabel(
                    String.format("<html><p WIDTH=%s>%s</p></html>", Window.TEXT_WIDTH, message)));

            text.setForeground(Window.TEXT_COLOR);
            text.setFont(new Font("SansSerif", Font.PLAIN, 16));

            // When the width of the box exceeds the maximum, it expands vertically
            Dimension boxSize = new Dimension(Window.TEXT_BOX_WIDTH,
                    text.getPreferredSize().height + 2 * Window.BOX_MARGIN.height);

            box = new RoundRectangle2D.Double(0, 0, boxSize.width, boxSize.height,
                    Window.BORDER_RADIUS, Window.BORDER_RADIUS);

            // Prevents BoxLayout from resizing
            setMaximumSize(boxSize);
        }

        /**
         * Draws the box and its associated text onto its parent component
         * 
         * @param g The graphics drawer used to render shapes or text on its parent
         */
        @Override
        public void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;

            // Draws box
            g2.setColor(Window.BOX_COLOR);
            g2.fill(box);

        }
    }

    /**
     * Initializes an inbox containing all the text messages in the group chat
     * 
     * @param username The name of the user
     */
    public Inbox(String username) {
        this.username = username;

        // Sets size and inserts padding
        setPreferredSize(Window.SIZE);
        setBorder(new EmptyBorder(Window.SCREEN_MARGIN.height, Window.SCREEN_MARGIN.width,
                Window.SCREEN_MARGIN.height, Window.SCREEN_MARGIN.width));

        // Displays items in a single column
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        setBackground(Window.BACKGROUND_COLOR);


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


        add(new TextBox("Sender1", shortMessage));
        add(new TextBox("Sender2", longMessage));
        // add(new TextBox("Sender3", shortMessage));
        // add(new TextBox("Sender4", shortMessage));
        // add(new TextBox("Sender5", shortMessage));
        // add(new TextBox("Sender6", shortMessage));

    }
}
