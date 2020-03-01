package base.nio;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * 服务端 接受客户端的发来数据
 */
public class UDPServer {

    public static void main(String[] args) throws Exception {
        DatagramSocket server = new DatagramSocket(1000);
        System.out.println("udp启动");
        byte[] container = new byte[1024];
        DatagramPacket packet = new DatagramPacket(container, container.length);
        // 服务端接收客户端数据
        server.receive(packet);
        // 解析数据
        byte[] data = packet.getData();
        System.out.println(data);
        int len = data.length;
        String res = new String(data, 0, len);
        System.out.println(res);
        server.close();


    }
}
