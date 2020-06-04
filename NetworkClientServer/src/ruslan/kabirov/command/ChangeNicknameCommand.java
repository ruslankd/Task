package ruslan.kabirov.command;

import java.io.Serializable;

public class ChangeNicknameCommand implements Serializable {

    private final String newNickname;

    public ChangeNicknameCommand(String newNickname) {
        this.newNickname = newNickname;
    }

    public String getNewNickname() {
        return newNickname;
    }
}
