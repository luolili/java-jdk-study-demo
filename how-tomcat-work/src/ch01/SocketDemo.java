package ch01;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class SocketDemo {
    public static void main(String[] args) throws Throwable {
        //创建客户端连接服务端的管道
        Socket socket = new Socket("127.0.0.1", 8080);
        //写入 socket 的 内容
        OutputStream os = socket.getOutputStream();
        boolean autoflush = true;
        PrintWriter out = new PrintWriter(socket.getOutputStream(), autoflush);

        //读取 socket 里面的内容
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        //发送请求道服务端
        out.println("GET /index.jsp HTTP/1.1");
        out.println("HOST localhost:8080");
        out.println("Collection Close");
        out.println();
        //读取响应信息
        boolean loop = true;
        StringBuffer sb = new StringBuffer(8096);
        while (loop) {
            if (in.ready()) {
                int i = 0;
                while (i != -1) {
                    i = in.read();
                    sb.append((char) i);
                }
                loop = false;
            }
        }
        System.out.println(sb.toString());
        socket.close();


    }
}
