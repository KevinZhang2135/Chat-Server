

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.RoundRectangle2D;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class Inbox extends JPanel {
    public static final Dimension BOX_MARGIN = new Dimension(0, 10);
    public static final Dimension TEXT_BOX_MARGIN = new Dimension(20, 10);

    // Maximum width of the text box
    public static final int TEXT_BOX_WIDTH = Client.SIZE.width - Client.SCREEN_MARGIN.width * 2;

    // Maximum width of the text label
    public static final int TEXT_WIDTH = TEXT_BOX_WIDTH - TEXT_BOX_MARGIN.width * 2;

    // Radius of the rounded text box corners
    public static final int BORDER_RADIUS = (int) (TEXT_BOX_WIDTH * 0.06);

    private String username;

    /**
     * A username header displayed for the sender of each message.
     */
    public class UserLabel extends JPanel {
        // Colors used for the name of message senders
        public static enum SenderColors {
            PINK(0xf791b8), ORANGE(0xfeb580), SAND(0xd6cb75), LIME(0xc9d87b), TEAL(
                    0x75c4bb), INDIGO(0x8ea4e2), PERIWINKLE(0xaea1f3);

            public final Color color;

            private SenderColors(int hex) {
                color = new Color(hex);
            }
        };

        /**
         * Initializes a label for the sender of a message. If the sender is the user, it is aligned
         * on the right of the screen and the username is replaced with "You". Throws
         * {@code IllegalArgumentException} if the sender is null.
         * 
         * @param sender The specified sender username
         */
        public UserLabel(String sender) {
            if (sender == null)
                throw new IllegalArgumentException();

            // If the sender is the user, the header is replaced with "You" and aligned left
            int alignment = SwingConstants.LEFT;
            if (username.equals(sender)) {
                alignment = SwingConstants.RIGHT;
                sender = "You";
            }

            JLabel text = new JLabel(sender, alignment);
            text.setBorder(new EmptyBorder(0, 0, 0, 0));
            text.setFont(Client.SANS_SERIF_16);

            text.setPreferredSize(new Dimension(TEXT_BOX_WIDTH, text.getPreferredSize().height));
            text.setForeground(getSenderColor(sender));

            setBackground(Client.CLEAR); // Transparent background
            add(text);
        }

        /**
         * Returns the preferred size as maximum size for the label to prevent resizing by the box layout.
         * 
         * @return The preferred size of the label
         *
         */
        @Override
        public Dimension getMaximumSize() {
            return getPreferredSize();
        }

        /**
         * Returns a color using the hash code of the specified sender to create somewhat random
         * colors for each user.<br/>
         * 
         * Because the color of the sender's username is chosen via hashing, it is approximately
         * random, and multiple calls using the same sender will return the same color.
         * 
         * @param sender The specified username of the sender
         * @return The color to be used for the username of the sender
         */
        private static Color getSenderColor(String sender) {
            SenderColors[] colors = SenderColors.values();
            int index = Math.abs(sender.hashCode()) % colors.length;

            return colors[index].color;
        }
    }

    /**
     * A text box in the inbox displayed for each message.
     */
    public class TextBox extends JPanel {
        public static final Color BOX_COLOR = new Color(0x282a2d);
        public static final Color TEXT_COLOR = new Color(0xe3e2e5);

        private RoundRectangle2D box;

        /**
         * Initializes and displays a text box as a rounded rectangle with a message. Throws
         * {@code IllegalArgumentException} if the message is null.
         * 
         * @param message The message content
         */
        public TextBox(String message) {
            if (message == null)
                throw new IllegalArgumentException();

            // Allows the text to wrap across multiple lines if necessary
            String html = "<html><body style='width: %s'>%s</body></html>";
            JLabel text = new JLabel(String.format(html, TEXT_WIDTH, message));

            text.setForeground(TEXT_COLOR);
            text.setFont(Client.SANS_SERIF_16);

            // When the width of the box exceeds the maximum, it expands vertically
            Dimension boxSize = new Dimension(TEXT_BOX_WIDTH,
                    text.getPreferredSize().height + 2 * TEXT_BOX_MARGIN.height);

            box = new RoundRectangle2D.Double(0, 0, boxSize.width, boxSize.height, BORDER_RADIUS,
                    BORDER_RADIUS);

            setPreferredSize(boxSize);
            setBackground(Client.CLEAR); // Transparent background

            add(text);
        }


        /**
         * Returns the preferred size as maximum size for the text box to prevent resizing by the
         * box layout.
         * 
         * @return The preferred size of the text box
         *
         */
        @Override
        public Dimension getMaximumSize() {
            return getPreferredSize();
        }

        /**
         * Draws the box and its associated text onto its parent component
         * 
         * @param g The graphics drawer used to render shapes or text on its parent
         */
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;

            // Draws box
            g2.setColor(BOX_COLOR);
            g2.fill(box);
        }
    }

    /**
     * Initializes an inbox containing all the text messages in the group chat. Throws
     * {@code IllegalArgumentException} if the username is null.
     * 
     * @param username The name of the user
     */
    public Inbox(String username) {
        if (username == null)
            throw new IllegalArgumentException();

        this.username = username;

        // Displays items in a single column
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Sets size and inserts padding
        setBorder(new EmptyBorder(Client.SCREEN_MARGIN.height, Client.SCREEN_MARGIN.width,
                Client.SCREEN_MARGIN.height, Client.SCREEN_MARGIN.width));

        setBackground(Client.BACKGROUND_COLOR);
    }

    /**
     * Creates a new inbox message with the specified sender and message and displays it in the
     * inbox. Throws {@code IllegalArgumentException} either if the sender or message is null.
     * 
     * @param message The specified message
     */
    public void addMessage(Message message) {
        if (message == null)
            throw new IllegalArgumentException();

        add(new UserLabel(message.username));
        add(new TextBox(message.message));

        // Inserts margins between messages
        add(Box.createRigidArea(BOX_MARGIN));

        revalidate(); // Updates to reveal changes
        repaint(); // Redraw over old image
    }
}
