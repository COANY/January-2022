package com.example.nio;

import lombok.SneakyThrows;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Scanner;

public class niochannel {
    @SneakyThrows
    @Test
    public void write() {
        FileOutputStream fos = new FileOutputStream("test.txt");
        FileChannel channel = fos.getChannel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        byteBuffer.put("哈哈哈哈哈".getBytes());
        System.out.println(byteBuffer.position());
        byteBuffer.flip();
        channel.write(byteBuffer);
        channel.close();
    }

    @SneakyThrows
    @Test
    public void read() {
        FileInputStream fos = new FileInputStream("test.txt");
        FileChannel channel = fos.getChannel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        channel.read(byteBuffer);
        byteBuffer.flip();
        System.out.println(new String(byteBuffer.array()));
        channel.close();//
    }

    @SneakyThrows
    @Test
    public void copy() {
        FileOutputStream fos = new FileOutputStream("copy.txt");
        FileChannel fosChannel = fos.getChannel();
        FileInputStream fis = new FileInputStream("test.txt");
        FileChannel fisChannel = fis.getChannel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        while (true) {
            byteBuffer.clear();
            int read = fisChannel.read(byteBuffer);
            if (read == -1) {
                break;
            }
            byteBuffer.flip();
            fosChannel.write(byteBuffer);
        }
        fisChannel.close();
        fosChannel.close();
    }

    @SneakyThrows
    @Test
    public void test() {//分散和聚集
        FileOutputStream fos = new FileOutputStream("copy.txt");
        FileChannel fosChannel = fos.getChannel();
        FileInputStream fis = new FileInputStream("test.txt");
        FileChannel fisChannel = fis.getChannel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(4);
        ByteBuffer byteBuffer1 = ByteBuffer.allocate(1024);
        ByteBuffer[] buffers = {byteBuffer, byteBuffer1};
        fisChannel.read(buffers);
        for (ByteBuffer buffer : buffers) {
            buffer.flip();
        }
        fosChannel.write(buffers);
        fisChannel.close();
        fosChannel.close();

    }

    @SneakyThrows
    @Test
    public void fromTo() {
        FileOutputStream fos = new FileOutputStream("copy.txt");
        FileChannel fosChannel = fos.getChannel();
        FileInputStream fis = new FileInputStream("test.txt");
        FileChannel fisChannel = fis.getChannel();
//        fosChannel.transferFrom(fisChannel,fisChannel.position(),fisChannel.size());
        fisChannel.transferTo(fisChannel.position(), fisChannel.size(), fosChannel);
        fisChannel.close();
        fosChannel.close();

    }

    @SneakyThrows
    @Test
    public void server() {
        //创建选择器
        Selector selector = Selector.open();
        //向选择器注册通道
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);
        ssc.bind(new InetSocketAddress(998));
        //5.注册监听事件
        ssc.register(selector, SelectionKey.OP_ACCEPT);
        //6.轮询获取选择器上已经准备好的事件
        while (selector.select() > 0) {//阻塞的
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                //获取事件
                SelectionKey key = iterator.next();
                //判断是什么事件
                if (key.isAcceptable()) {
                    //获取连接
                    SocketChannel accept = ssc.accept();
                    accept.configureBlocking(false);
                    accept.register(selector, SelectionKey.OP_READ);
                } else if (key.isReadable()) {
                    SocketChannel read = (SocketChannel) key.channel();
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    int len = 0;
                    while ((len = read.read(buffer)) > 0) {

                    }

                } else if (key.isWritable()) {

                }
            }
        }
    }

    @SneakyThrows
    @Test
    public void clint() {
        SocketChannel sc = SocketChannel.open(new InetSocketAddress("127.0.0.1", 998));
        sc.configureBlocking(false);
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()){
            String str = scanner.next();
            buffer.put(str.getBytes());
            buffer.flip();
            sc.write(buffer);
            buffer.clear();
        }
        sc.close();
    }

}
