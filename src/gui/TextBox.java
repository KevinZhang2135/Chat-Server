package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.geom.RoundRectangle2D;
import java.text.AttributedString;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class TextBox extends JPanel {
    private static final Dimension SCREEN_MARGIN = new Dimension(50, 0); // Margins of the screen
    private static final Dimension BOX_MARGIN = new Dimension(20, 10); // Margins of text box

    // Maximum width of the text box
    private static final int MAX_WIDTH = Window.SIZE.width - SCREEN_MARGIN.width * 2;

    // Maximum width of the text
    private static final int MAX_TEXT_WIDTH = MAX_WIDTH - BOX_MARGIN.width * 2;

    // Radius of the rounded text box corners
    private static final int BORDER_RADIUS = (int) (MAX_WIDTH * 0.02);

    // Colors
    private static final Color BOX_COLOR = new Color(0x282a2d);
    private static final Color TEXT_COLOR = new Color(0xe3e2e5);

    // Colors used for the name of message senders
    private static enum SenderColors {
        PINK(0xf791b8), ORANGE(0xfeb580), SAND(0xd6cb75), LIME(0xc9d87b), TEAL(0x75c4bb), INDIGO(
                0x8ea4e2), PERIWINKLE(0xaea1f3);

        public final Color color;

        private SenderColors(int hex) {
            color = new Color(hex);
        }
    };

    private static int count = 0;

    private String username, sender;

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
    public TextBox(String username, String sender, String message) {
        this.username = username;
        this.sender = sender;

        // Formats message as sans-serif in 16-pt font
        // HTML tags are used to enforce text wrapping
        add(text = new JLabel(
                String.format("<html><p WIDTH=%s>%s</p></html>", MAX_TEXT_WIDTH, message)));

        text.setForeground(TEXT_COLOR);
        text.setFont(new Font("SansSerif", Font.PLAIN, 16));

        // When the width of the box exceeds the maximum, it expands vertically
        Dimension boxSize =
                new Dimension(MAX_WIDTH, text.getPreferredSize().height + 2 * BOX_MARGIN.height);

        // Aligns the box to the right of the screen if the sender is the user
        Point boxPos = new Point(SCREEN_MARGIN.width, 50);
        if (sender.equals(username))
            boxPos.x = Window.SIZE.width - SCREEN_MARGIN.width - boxSize.width;

        box = new RoundRectangle2D.Double(boxPos.x, 0, boxSize.width, boxSize.height, BORDER_RADIUS,
                BORDER_RADIUS);

        System.out.println(this);
        System.out.println(count++);
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
        g2.setColor(BOX_COLOR);
        g2.fill(box);

    }
}

