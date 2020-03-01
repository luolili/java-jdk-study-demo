package base.nio;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

/**
 * 客户端发送数据
 */
public class UDPClient {
    public static void main(String[] args) throws Exception {
        //链接服务端
        DatagramSocket client = new DatagramSocket(10086);
        // 准备数据
        String msg = "this is a msg!";
        byte[] data = msg.getBytes();
        DatagramPacket packet = new DatagramPacket(data, data.length, new InetSocketAddress("127.0.0.1", 1000));
        client.send(packet);
        client.close();

    }
}
