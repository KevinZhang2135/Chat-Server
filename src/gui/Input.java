package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class Input extends JPanel {
    /* Dimension and size constants */
    // Maximum size of the box
    public static final Dimension BOX_SIZE =
            new Dimension(Window.INPUT_SIZE.width - Window.SCREEN_MARGIN.width * 2,
                    Window.INPUT_SIZE.height - Window.SCREEN_MARGIN.height * 2);

    /* Colors */
    public static final Color FORM_COLOR = new Color(0x282a2d);
    public static final Color TEXT_COLOR = new Color(0xe3e2e5);

    private String username;
    private RoundTextField textField;

    public Input(String username) {
        super();
        this.username = username;

        textField = new RoundTextField();
        textField.setPreferredSize(BOX_SIZE);
        textField.setBorder(new EmptyBorder(Window.SCREEN_MARGIN.height, Window.SCREEN_MARGIN.width,
                Window.SCREEN_MARGIN.height, Window.SCREEN_MARGIN.width));

        textField.setFont(Window.SANS_SERIF_16);
        textField.setCaretColor(TEXT_COLOR);

        textField.setForeground(TEXT_COLOR);
        textField.setBackground(FORM_COLOR);

        // Sets size and inserts padding
        setBorder(new EmptyBorder(Window.SCREEN_MARGIN.height, Window.SCREEN_MARGIN.width,
                Window.SCREEN_MARGIN.height, Window.SCREEN_MARGIN.width));

        setMaximumSize(Window.INPUT_SIZE);
        setBackground(Window.BACKGROUND_COLOR);

        add(textField);
    }

    /**
     * Clears all characters in the text field
     */
    public void clear() {
        if (textField == null)
            return;

        textField.setText("");
    }
}
