package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextLayout;
import java.awt.geom.RoundRectangle2D;
import java.text.AttributedString;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class TextBox extends JPanel {
    // Maximum width of the text box
    private static final int MAX_WIDTH = Window.SIZE.width - Window.GUTTER_WIDTH * 2;
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

    private RoundRectangle2D box;

    private String username, sender;

    private String message;
    private AttributedString attributedMessage;

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
        this.message = message;
        attributedMessage = new AttributedString(message);

        System.out.println(getPreferredSize());

        // When the width of the box exceeds the maximum, it expands vertically
        Dimension boxSize = new Dimension(message.length() * 10, 100);
        if (boxSize.width > MAX_WIDTH) {
            boxSize.height *= (int) (boxSize.width / MAX_WIDTH) + 1;
            boxSize.width = (int) MAX_WIDTH;
        }

        // Aligns the box to the right of the screen if the sender is the user
        Point boxPos = new Point((int) Window.GUTTER_WIDTH, 50);
        if (sender.equals(username))
            boxPos.x = Window.SIZE.width - Window.GUTTER_WIDTH - boxSize.width;

        box = new RoundRectangle2D.Double(boxPos.x, 0, boxSize.width, boxSize.height, BORDER_RADIUS,
                BORDER_RADIUS);

    }

    /**
     * Renders text in a wrapping, block paragraph. 
     * 
     * @param g2 The graphics drawer used to render text on its parent
     */
    public void paintText(Graphics2D g2) {
        FontRenderContext frc = g2.getFontRenderContext();
        
        // Allows text to wrap into paragraphs
        Point textPos = new Point();
        LineBreakMeasurer measurer = new LineBreakMeasurer(attributedMessage.getIterator(), frc);
        float wrappingWidth = getSize().width - 15;

        while (measurer.getPosition() < message.length()) {

            TextLayout layout = measurer.nextLayout(wrappingWidth);

            textPos.y += (layout.getAscent());
            float dx = layout.isLeftToRight() ? 0 : (wrappingWidth - layout.getAdvance());

            layout.draw(g2, textPos.x + dx, textPos.y);
            textPos.y += layout.getDescent() + layout.getLeading();
        }
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

        // Draws text message
        g2.setColor(TEXT_COLOR);
        paintText(g2);
    }
}

