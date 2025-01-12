package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class Input extends JPanel {
    /* Dimension and size constants */
    // Maximum width of the box
    public static final int BOX_WIDTH = Window.SIZE.width - Window.SCREEN_MARGIN.width * 2;

    // Radius of the rounded box corners
    public static final int BORDER_RADIUS = (int) (BOX_WIDTH * 0.06);

    /* Colors */
    public static final Color BOX_COLOR = new Color(0x282a2d);
    public static final Color TEXT_COLOR = new Color(0xe3e2e5);

    private String username;
    private RoundRectangle2D box;

    public Input(String username) {
        super();
        this.username = username;

        // When the width of the box exceeds the maximum, it expands vertically
        Dimension boxSize = new Dimension(BOX_WIDTH, 100);

        box = new RoundRectangle2D.Double(0, 0, boxSize.width, boxSize.height, BORDER_RADIUS,
                BORDER_RADIUS);

        // Displays items in a single column
        // setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Sets size and inserts padding
        setBorder(new EmptyBorder(Window.SCREEN_MARGIN.height, Window.SCREEN_MARGIN.width,
                Window.SCREEN_MARGIN.height, Window.SCREEN_MARGIN.width));

        setBackground(Color.RED);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // Draws box
        g2.setColor(BOX_COLOR);
        g2.fill(box);
    }
}
