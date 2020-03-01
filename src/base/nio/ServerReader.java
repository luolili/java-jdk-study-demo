package base.nio;

import java.awt.*;
import java.io.DataInputStream;
import java.io.IOException;

public class ServerReader extends Thread {
    private ChatServer server;

    public ServerReader(ChatServer server) {
        this.server = server;
    }

    @Override
    public void run() {
        String info;
        DataInputStream dis = server.getDataInputStream();
        TextArea ta = server.getTextArea();
        try {
            while (true) {
                info = dis.readUTF();
                ta.append("对方说: " + info + "\n");
                if ("end".equals(info)) {
                    server.close();
                    System.exit(0);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
