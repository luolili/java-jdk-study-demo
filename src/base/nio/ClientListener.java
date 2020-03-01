package base.nio;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class ClientListener implements ActionListener {
    private ChatClient client;

    public ClientListener(ChatClient client) {
        this.client = client;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        TextField tf = client.getTextField();
        String info = tf.getText();
        client.getTextArea().append("自己说: " + info + "\n");
        try {
            client.getDataOutputStream().writeUTF(info);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        if ("end".equals(info)) {
            client.close();
            System.exit(0);
        }
        tf.setText("");
        tf.requestFocus();
    }
}
