package gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

    /**
     * A rounded JTextField that allows user input.
     * 
     * Credit to Harry Joy at https://stackoverflow.com/a/8515677
     */
    public class RoundTextField extends JTextField {
        // The radius of the field is defined as the width * radiusMultipler.
        private static double radiusMultipler = 0.06;
        private Shape shape;

        /**
         * Initializes and displays a text field with rounded corners with the specified preferred
         * size.
         * 
         * @param size The preferred size of the text field
         */
        public RoundTextField() {
            super();

            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            // Radius of the rounded box corners
            int radius = (int) (getWidth() * radiusMultipler);
            g.setColor(getBackground());
            g.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);

            super.paintComponent(g);
        }

        // No border is drawn
        @Override
        protected void paintBorder(Graphics g) {}

        @Override
        public boolean contains(int x, int y) {
            if (shape == null || !shape.getBounds().equals(getBounds())) {
                int radius = (int) (getWidth() * radiusMultipler);
                shape = new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, radius,
                        radius);
            }

            return shape.contains(x, y);
        }
    }

