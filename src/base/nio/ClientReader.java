package base.nio;

import java.awt.*;
import java.io.DataInputStream;
import java.io.IOException;

public class ClientReader extends Thread {
    private ChatClient client;

    public ClientReader(ChatClient client) {
        this.client = client;
    }

    @Override
    public void run() {
        String info;
        DataInputStream dis = client.getDataInputStream();
        TextArea ta = client.getTextArea();
        try {
            while (true) {
                info = dis.readUTF();
                ta.append("对方说: " + info + "\n");
                if ("end".equals(info)) {
                    client.close();
                    System.exit(0);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
