package gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JPanel;

public class TextBox extends JPanel {
    private RoundRectangle2D box;
    private static final Color BOX_COLOR = new Color(0x282a2d);

    // Maximum width of the text box
    private static final double MAX_WIDTH = Window.SIZE.width - Window.GUTTER_WIDTH * 2;
    private static final double BORDER_RADIUS = MAX_WIDTH * 0.02;

    // Alignment of the box on the screen
    public static enum Align {
        LEFT, RIGHT
    }

    /**
     * 
     * @param sender
     * @param message
     * @param alignment
     */
    public TextBox(String sender, String message, Align alignment) {
        // When the width of the box exceeds the maximum, it expands vertically
        Dimension boxSize = new Dimension(message.length() * 10, 100);
        if (boxSize.width > MAX_WIDTH) {
            boxSize.height *= (int) (boxSize.width / MAX_WIDTH) + 1;
            boxSize.width = (int) MAX_WIDTH;
        }

        // Aligns the box to the left or right of the screen depending on alignment
        Point boxPos = new Point();
        boxPos.x = (int) switch (alignment) {
            case LEFT -> Window.GUTTER_WIDTH;
            case RIGHT -> Window.SIZE.width - Window.GUTTER_WIDTH - boxSize.width;
        };

        box = new RoundRectangle2D.Double(boxPos.x, 0, boxSize.width, boxSize.height, BORDER_RADIUS,
                BORDER_RADIUS);
    }


    /**
     * Draws the box and its associated text onto its parent component
     * 
     * @param g The graphics drawer used to render shapes on text on its parent
     */
    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        g2.setColor(BOX_COLOR);
        g2.fill(box);
    }
}

