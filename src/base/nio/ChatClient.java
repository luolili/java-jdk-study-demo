package base.nio;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * client
 */
public class ChatClient {
    private TextArea ta;
    private TextField tf;

    private Socket s;
    private DataInputStream dis;
    private DataOutputStream dos;

    public TextField getTextField() {
        return tf;
    }

    public TextArea getTextArea() {
        return ta;
    }

    public DataInputStream getDataInputStream() {
        return dis;
    }

    public DataOutputStream getDataOutputStream() {
        return dos;
    }

    public static void main(String[] args) {
        ChatClient socketClient = new ChatClient();
        socketClient.createUI();
        socketClient.connect();
        socketClient.createThread();
    }

    public void close() {
        try {
            dis.close();
            dos.close();
            s.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建界面
     */
    private void createUI() {
        Frame f = new Frame("socketClient");
        ta = new TextArea();
        tf = new TextField();
        Button send = new Button("send");

        Panel p = new Panel();
        p.setLayout(new BorderLayout());
        p.add(tf, "Center");
        p.add(send, "East");

        f.add(ta, "Center");
        f.add(p, "South");

        ClientListener listener = new ClientListener(this);
        //不管点击发送按钮还是输入框回车，都会触发listener事件
        send.addActionListener(listener);
        tf.addActionListener(listener);
        //当关闭窗口的时候，退出系统
        f.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        f.setSize(400, 400);
        f.setVisible(true);
    }

    /**
     * 创建连接
     */
    public void connect() {
        try {
            s = new Socket("127.0.0.1", 8888);
            dis = new DataInputStream(s.getInputStream());
            dos = new DataOutputStream(s.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 启动监听服务端发来的消息的线程
     */
    public void createThread() {
        ClientReader reader = new ClientReader(this);
        reader.start();
    }
}
