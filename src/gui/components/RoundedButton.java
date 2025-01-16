package gui.components;

import java.awt.AWTEvent;
import java.awt.AWTEventMulticaster;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

/*
 * A rounded button for user interaction.
 * 
 */
public class RoundedButton extends Component {
    private ActionListener actionListener; // Post action events to listeners
    private boolean pressed = false; // true if the button is indented.

    private String label; // The button's text
    private Image image; // An image to be rendered atop the button

    /**
     * Constructs a RoundedButton with no label.
     */
    public RoundedButton() {
        this("");
    }

    /**
     * Constructs a RoundedButton with the specified label.
     *
     * @param label The label of the button
     */
    public RoundedButton(String label) {
        this.label = label;
        enableEvents(AWTEvent.MOUSE_EVENT_MASK);
    }

    /**
     * Gets the label.
     *
     * @see setLabel
     */
    public String getLabel() {
        return label;
    }

    /**
     * Sets the label.
     * 
     * @param label The new label of the button
     *
     * @see getLabel
     */
    public void setLabel(String label) {
        this.label = label;
        invalidate();
        repaint();
    }

    /**
     * Sets the image.
     * 
     * @param image The new image of the button
     */
    public void setImage(Image image) {
        this.image = image;
        repaint();
    }

    @Override
    public void paint(Graphics g) {
        // Paint the interior of the button
        g.setColor(pressed ? getBackground().darker() : getBackground());
        g.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, getWidth(), getWidth());

        // Draw the label centered in the button
        Font font = getFont();
        if (font != null) {
            FontMetrics fm = getFontMetrics(getFont());
            g.setColor(getForeground());
            g.drawString(label, getWidth() / 2 - fm.stringWidth(label) / 2,
                    getHeight() / 2 + fm.getMaxDescent());
        }

        // Draw the image center in the button
        if (image != null) {
            g.drawImage(image, getWidth() / 2 - image.getWidth(null) / 2,
                    getHeight() / 2 - image.getHeight(null) / 2, null);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        Font f = getFont();
        if (f != null) {
            FontMetrics fm = getFontMetrics(getFont());
            int max = Math.max(fm.stringWidth(label) + 40, fm.getHeight() + 40);
            return new Dimension(max, max);

        } else {
            return new Dimension(100, 100);
        }
    }

    /**
     * Adds the specified action listener to receive action events from this button.
     *
     * @param listener The action listener
     */
    public void addActionListener(ActionListener listener) {
        actionListener = AWTEventMulticaster.add(actionListener, listener);
        enableEvents(AWTEvent.MOUSE_EVENT_MASK);
    }

    /**
     * Removes the specified action listener so it no longer receives action events from this
     * button.
     *
     * @param listener The action listener
     */
    public void removeActionListener(ActionListener listener) {
        actionListener = AWTEventMulticaster.remove(actionListener, listener);
    }

    /**
     * Determine if click was inside round button.
     */
    @Override
    public boolean contains(int x, int y) {
        int mx = getSize().width / 2;
        int my = getSize().height / 2;

        return (((mx - x) * (mx - x) + (my - y) * (my - y)) <= mx * mx);
    }

    /**
     * Paints the button and distribute an action event to all listeners.
     */
    @Override
    public void processMouseEvent(MouseEvent e) {
        switch (e.getID()) {
            case MouseEvent.MOUSE_PRESSED -> {
                pressed = true;

                // Repaint might flicker a bit. To avoid this, you can use
                // double buffering (see the Gauge example).
                repaint();
            }

            case MouseEvent.MOUSE_RELEASED -> {
                if (actionListener != null) {
                    actionListener.actionPerformed(
                            new ActionEvent(this, ActionEvent.ACTION_PERFORMED, label));
                }

                // render myself normal again
                if (pressed == true) {
                    pressed = false;

                    // Repaint might flicker a bit. To avoid this, you can use
                    // double buffering (see the Gauge example).
                    repaint();
                }

            }

            case MouseEvent.MOUSE_ENTERED -> {
            }

            case MouseEvent.MOUSE_EXITED -> {
                if (pressed == true) {
                    // Cancel; don't send action event.
                    pressed = false;

                    // Repaint might flicker a bit. To avoid this, you can use
                    // double buffering (see the Gauge example).
                    repaint();
                }
            }
        }

        super.processMouseEvent(e);
    }
}
