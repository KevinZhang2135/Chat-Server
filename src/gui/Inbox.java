package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.RoundRectangle2D;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class Inbox extends JPanel {
    public static final Dimension BOX_MARGIN = new Dimension(0, 10);
    public static final Dimension TEXT_BOX_MARGIN = new Dimension(20, 10);

    // Maximum width of the text box
    public static final int TEXT_BOX_WIDTH = Window.SIZE.width - Window.SCREEN_MARGIN.width * 2;

    // Maximum width of the text label
    public static final int TEXT_WIDTH = TEXT_BOX_WIDTH - TEXT_BOX_MARGIN.width * 2;

    // Radius of the rounded text box corners
    public static final int BORDER_RADIUS = (int) (TEXT_BOX_WIDTH * 0.08);


    private String username;

    /**
     * A text box in the inbox displayed for each message that contains message and the name of its
     * sender.
     */
    public static class TextBox extends JPanel {
        public static final Color BOX_COLOR = new Color(0x282a2d);

        // Colors used for the name of message senders
        public static enum SenderColors {
            PINK(0xf791b8), ORANGE(0xfeb580), SAND(0xd6cb75), LIME(0xc9d87b), TEAL(
                    0x75c4bb), INDIGO(0x8ea4e2), PERIWINKLE(0xaea1f3);

            public final Color color;

            private SenderColors(int hex) {
                color = new Color(hex);
            }
        };

        private RoundRectangle2D box;

        /**
         * Initializes and displays a text box as a rounded rectangle with a message and name of its
         * sender. If the sender is the user, it is aligned on the right of the screen.
         * 
         * @param username The name of the user
         * @param sender The sender of the message
         * @param message The message content
         */
        public TextBox(String sender, String message) {
            super();
            
            String html = "<html><body style='width: %s'>%s</body></html>";
            JLabel text = new JLabel(String.format(html, TEXT_WIDTH, message));

            text.setForeground(Window.TEXT_COLOR);
            text.setFont(Window.SANS_SERIF_16);

            // When the width of the box exceeds the maximum, it expands vertically
            Dimension boxSize = new Dimension(TEXT_BOX_WIDTH,
                    text.getPreferredSize().height + 2 * TEXT_BOX_MARGIN.height);

            box = new RoundRectangle2D.Double(0, 0, boxSize.width, boxSize.height, BORDER_RADIUS,
                    BORDER_RADIUS);

            setBackground(SenderColors.INDIGO.color);
            
            add(text);

            setPreferredSize(boxSize);
            setMaximumSize(boxSize);
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
     * Initializes an inbox containing all the text messages in the group chat
     * 
     * @param username The name of the user
     */
    public Inbox(String username) {
        super();
        this.username = username;

        // Displays items in a single column
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Sets size and inserts padding
        setPreferredSize(Window.SIZE);
        setBorder(new EmptyBorder(Window.SCREEN_MARGIN.height, Window.SCREEN_MARGIN.width,
                Window.SCREEN_MARGIN.height, Window.SCREEN_MARGIN.width));

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

        add(new TextBox("Other", shortMessage));
        add(Box.createRigidArea(BOX_MARGIN));
        add(new TextBox("Other", longMessage));

        // add(new TextBox("Other2", longMessage));
        // add(new TextBox("Other2", longMessage));
        // add(new TextBox("Other2", longMessage));
        // add(new TextBox("Other2", longMessage));

    }
}
