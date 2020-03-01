package base.nio;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class ServerListener implements ActionListener {
    private ChatServer server;

    public ServerListener(ChatServer server) {
        this.server = server;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        TextField tf = server.getTextField();
        String info = tf.getText();
        server.getTextArea().append("自己说: " + info + "\n");
        try {
            server.getDataOutputStream().writeUTF(info);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        if ("end".equals(info)) {
            server.close();
            System.exit(0);
        }
        tf.setText("");
        tf.requestFocus();
    }
}
