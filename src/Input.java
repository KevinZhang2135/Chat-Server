
import components.RoundTextField;
import components.RoundedButton;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.function.Consumer;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class Input extends JPanel {
    /* Dimension and size constants */
    // Constant size of send button
    public static final int BUTTON_WIDTH = Client.INPUT_SIZE.height - Client.SCREEN_MARGIN.height * 2;

    // Maximum size of the box
    public static final Dimension BOX_SIZE = new Dimension(
            Client.INPUT_SIZE.width - Client.SCREEN_MARGIN.height - BUTTON_WIDTH, BUTTON_WIDTH);

    /* Colors */
    public static final Color FORM_COLOR = new Color(0x282a2d);
    public static final Color TEXT_COLOR = new Color(0xe3e2e5);

    private final RoundTextField textField;

    private final RoundedButton sendButton;
    private Consumer<Message> buttonCallback; // Used to send client requests upon pressing button

    /**
     * Initializes an input text box and button at the bottom of the app to send
     * messages.
     * 
     * @param username The specified username of the user
     */
    public Input(String username) {

        // Creates button to send messages to other users
        sendButton = new RoundedButton();

        sendButton.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_WIDTH));
        sendButton.setBackground(FORM_COLOR);

        try {
            BufferedImage icon = ImageIO.read(new File("res/sendIcon.png"));
            sendButton.setImage(icon);

        } catch (IOException e) {
            e.printStackTrace();
        }

        sendButton.addActionListener((ActionEvent e) -> {
            // Sends message when pressed
            String message = clear();

            if (!message.isBlank() && buttonCallback != null)
                buttonCallback.accept(new Message(username, message));
        });

        // Creates the text field for the user to enter messages
        textField = new RoundTextField();

        textField.setBorderPainted(false);
        textField.setBorder(new EmptyBorder(Client.SCREEN_MARGIN.height, Client.SCREEN_MARGIN.width,
                Client.SCREEN_MARGIN.height, Client.SCREEN_MARGIN.width));

        textField.setFont(Client.SANS_SERIF_16);
        textField.setCaretColor(TEXT_COLOR);

        textField.setPreferredSize(BOX_SIZE);
        textField.setForeground(TEXT_COLOR);
        textField.setBackground(FORM_COLOR);

        // Sets size and inserts padding
        setBorder(new EmptyBorder(Client.SCREEN_MARGIN.height, Client.SCREEN_MARGIN.width,
                Client.SCREEN_MARGIN.height, Client.SCREEN_MARGIN.width));

        setBackground(Client.BACKGROUND_COLOR);

        add(textField);
        add(sendButton);
    }

    /**
     * Returns the send button.
     * 
     * @return The send button for user input
     */
    public JButton getSendButton() {
        return sendButton;
    }

    /**
     * Sets the behavior of the send button upon click.
     * 
     * @param buttonCallback The callback triggered by clicking the send button
     */
    public void setButtonCallback(Consumer<Message> buttonCallback) {
        this.buttonCallback = buttonCallback;
    }

    /**
     *
     * Clears all characters in the text field and returns the string contained in
     * it.
     *
     * @return The string contained in {@code textField} if it is not null; null
     *         otherwise
     */
    public String clear() {
        if (textField == null)
            return null;

        String message = textField.getText();
        textField.setText("");

        return message;
    }
}
