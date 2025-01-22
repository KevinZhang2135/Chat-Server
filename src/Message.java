import java.io.Serializable;

/**
 * Message is a struct used by sockets to transport sender and message through a
 * {@code ObjectOutputStream}
 */
public class Message implements Serializable{
    public final String username, message;

    /**
     * Initializes a message with the specified sender username and text.
     * 
     * @param username Specified sender username
     * @param message Specified text
     */
    public Message(String username, String message) {
        this.username = username;
        this.message = message;
    }
}
