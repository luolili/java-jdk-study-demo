package com.luo.base;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(2000);
        System.out.println("服务器ok");
        System.out.println("服务端：" + serverSocket.getInetAddress() + "port:" + serverSocket.getLocalPort());
        //等待客户端来接
        for (; ; ) {
            //得到客户端
            Socket client = serverSocket.accept();
            ClientHandler clientHandler = new ClientHandler(client);
            clientHandler.start();
        }

    }

    private static class ClientHandler extends Thread {
        private Socket client;
        private boolean flag = true;

        public ClientHandler(Socket client) {
            this.client = client;
        }

        @Override
        public void run() {
            System.out.println("客户端：" + client.getLocalAddress() + "port:" + client.getLocalPort());
            try {
                PrintStream printStream = new PrintStream(client.getOutputStream());

                BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
                do {
                    //从客户端读取
                    String str = reader.readLine();

                    if ("bye".equalsIgnoreCase(str)) {
                        System.out.println("end");
                        flag = false;
                    } else {
                        System.out.println(str);
                    }
                } while (flag);
                printStream.close();
            } catch (Exception e) {

            } finally {
                try {
                    client.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("客户端exit");
        }
    }
}
