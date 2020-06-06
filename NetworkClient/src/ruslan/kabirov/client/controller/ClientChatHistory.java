package ruslan.kabirov.client.controller;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

public class ClientChatHistory {

    private String nickname;
    private File file;
    private List<String> last100messages;

    public ClientChatHistory(String nickname) {
        this.nickname = nickname;
        this.file = new File(nickname);
        this.last100messages = new LinkedList<String>();
        try {
            if (!file.exists()) {
                file.createNewFile();
            } else {
                try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                    for (String line; (line = br.readLine()) != null;) {
                        if (last100messages.size() == 100)
                            last100messages.remove(0);
                        last100messages.add(line);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addMessage(String message) {
        try (OutputStream os = new BufferedOutputStream(new FileOutputStream(file, true))) {
            os.write((message + System.lineSeparator()).getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<String> getLast100messages() {
        return last100messages;
    }

    public int size() {
        return last100messages.size();
    }
}
