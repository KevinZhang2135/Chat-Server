package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.function.Consumer;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

import javax.swing.border.EmptyBorder;
import gui.components.RoundTextField;
import gui.components.RoundedButton;

public class Input extends JPanel {
    /* Dimension and size constants */
    // Constant size of send button
    public static final int BUTTON_WIDTH =
            Window.INPUT_SIZE.height - Window.SCREEN_MARGIN.height * 2;

    // Maximum size of the box
    public static final Dimension BOX_SIZE =
            new Dimension(Window.INPUT_SIZE.width - Window.SCREEN_MARGIN.height, BUTTON_WIDTH);

    /* Colors */
    public static final Color FORM_COLOR = new Color(0x282a2d);
    public static final Color TEXT_COLOR = new Color(0xe3e2e5);

    private RoundTextField textField;

    private Consumer<String> buttonCallback;

    public Input(Consumer<String> buttonCallback) {
        this.buttonCallback = buttonCallback;

        // Creates button to send messages to other users
        RoundedButton sendButton = new RoundedButton();

        sendButton.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_WIDTH));
        sendButton.setBackground(FORM_COLOR);

        try {
            BufferedImage icon = ImageIO.read(new File("res/sendIcon.png"));
            sendButton.setImage(icon);

        } catch (IOException e) {
            System.err.println(e);
        }

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String output = clear();
                if (!output.isBlank())
                    buttonCallback.accept(output);
            }
        });

        // Creates the text field for the user to enter messages
        textField = new RoundTextField();

        textField.setBorderPainted(false);
        textField.setBorder(new EmptyBorder(Window.SCREEN_MARGIN.height, Window.SCREEN_MARGIN.width,
                Window.SCREEN_MARGIN.height, Window.SCREEN_MARGIN.width));

        textField.setPreferredSize(new Dimension(BOX_SIZE.width - 100, BOX_SIZE.height));

        textField.setFont(Window.SANS_SERIF_16);
        textField.setCaretColor(TEXT_COLOR);

        textField.setForeground(TEXT_COLOR);
        textField.setBackground(FORM_COLOR);

        // Sets size and inserts padding
        setBorder(new EmptyBorder(Window.SCREEN_MARGIN.height, Window.SCREEN_MARGIN.width,
                Window.SCREEN_MARGIN.height, Window.SCREEN_MARGIN.width));

        setBackground(Window.BACKGROUND_COLOR);

        add(textField);
        add(sendButton);
    }

    /**
     *
     * Clears all characters in the text field and returns the string contained in it.
     *
     * @return The string contained in {@code textField} if it is not null; null otherwise
     */
    public String clear() {
        if (textField == null)
            return null;

        String message = textField.getText();
        textField.setText("");

        return message;
    }
}
