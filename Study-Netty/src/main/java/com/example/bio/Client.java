package com.example.bio;

import java.io.*;
import java.net.Socket;

public class Client {
    public static void main(String[] args) {
        try {
            Socket s = new Socket("127.0.0.1", 999);
            //获取到字节输出流
            OutputStream os = s.getOutputStream();
            //将os包装成打印流
            PrintStream ps = new PrintStream(os);
            int i =0;
            while (true) {
                ps.println("哈哈哈" + i++);
                Thread.sleep(1000);
                ps.flush();

                InputStream in = s.getInputStream();
                InputStreamReader is = new InputStreamReader(in);
                //缓存字符输入流
                BufferedReader br = new BufferedReader(is);
                String msg;
                while ((msg = br.readLine()) != null) {
                    System.out.println(msg);
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
