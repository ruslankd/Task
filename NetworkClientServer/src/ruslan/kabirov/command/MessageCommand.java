package ruslan.kabirov.command;

import java.io.Serializable;

public class MessageCommand implements Serializable {

    private final String message;
    private final String username;

    public MessageCommand(String username, String message) {
        this.message = message;
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    public String getUsername() {
        return username;
    }
}
