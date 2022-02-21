package com.example.bio;


import lombok.SneakyThrows;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    public static void main(String[] args) {
        List<Socket> socketList = new ArrayList<>();
        try {
            ServerSocket ss = new ServerSocket(999);
            while (true) {
                Socket accSocket = ss.accept();
                serverRun run = new serverRun(accSocket);
                run.start();
                socketList.add(accSocket);
                socketList.forEach(e->{
                    try {
                        OutputStream os = e.getOutputStream();
                        PrintStream ps = new PrintStream(os);
                        ps.println("123");
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                });

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class serverRun extends Thread {
    private Socket accSocket ;

    public serverRun(Socket accSocket) {
        this.accSocket = accSocket;
    }

    @SneakyThrows
    @Override
    public void run() {
        //服务端
        //获取通道
        //通过管道获取字节输入流
        InputStream in = accSocket.getInputStream();
        //将字节输入流包装成缓冲字符输入流   目的是为了一行一行读取 规定的传输协议
        //字符输入流
        InputStreamReader is = new InputStreamReader(in);
        //缓存字符输入流
        BufferedReader br = new BufferedReader(is);
        String msg;
        while ((msg = br.readLine()) != null) {
            System.out.println(msg);
        }
    }
}
