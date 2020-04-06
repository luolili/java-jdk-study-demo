package com.luo.base;

import java.io.*;
import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.net.Socket;

public class Client {
    public static void main(String[] args) throws Exception {
        Socket socket = new Socket();
        //读取流的超时时间
        socket.setSoTimeout(3000);
        //连接本地，端口2000，
        socket.connect(new InetSocketAddress(Inet4Address.getLocalHost(), 2000), 3000);
        System.out.println("已经发起连接");
        System.out.println("客户端：" + socket.getLocalAddress() + "port:" + socket.getLocalPort());
        System.out.println("服务端：" + socket.getInetAddress() + "port:" + socket.getPort());
        try {
            todu(socket);
        } catch (Exception e) {
            System.out.println("erro close");
        }

        //释放资源
        socket.close();
        System.out.println("客户端exit");

    }

    public static void todu(Socket client) throws Exception {

        InputStream in = System.in;
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
        OutputStream out = client.getOutputStream();
        PrintStream printStream = new PrintStream(out);
        boolean flag = true;
        do {
            //从控制台读取
            String line = bufferedReader.readLine();
            //发送到服务器
            printStream.println(line);
            InputStream inputStream = client.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            //从服务器读取
            String str = reader.readLine();

            if ("bye".equalsIgnoreCase(str)) {
                System.out.println("end");
                flag = false;
            } else {
                System.out.println(str);
            }
        } while (flag);
        printStream.close();
        bufferedReader.close();


    }
}
