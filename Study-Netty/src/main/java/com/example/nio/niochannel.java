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
        SelectionKey key = null;
        SocketChannel sc = null;
        try {

            //创建选择器
            Selector selector = Selector.open();
            //向选择器注册通道
            ServerSocketChannel ssc = ServerSocketChannel.open();
            ssc.configureBlocking(false);
            ssc.bind(new InetSocketAddress(998));
            //5.注册监听事件
            ssc.register(selector, SelectionKey.OP_ACCEPT);
            //6.轮询获取选择器上已经准备好的事件
            System.out.println("启动成功");
            while (selector.select() > 0) {//阻塞的
                System.out.println("有事件来了");
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    //获取事件
                    key = iterator.next();
                    //判断是什么事件
                    if (key.isAcceptable()) {
                        //获取连接
                        sc = ssc.accept();
                        sc.configureBlocking(false);
                        sc.register(selector, SelectionKey.OP_READ);
                        System.out.println("接受到了连接");
                    } else if (key.isReadable()) {
                        sc = (SocketChannel) key.channel();
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        int read = sc.read(buffer);
                        if (read > 0) {
                            //转发给其他客户端
                            buffer.flip();
                            String mag = new String(buffer.array(), 0, buffer.remaining());
                            System.out.println("发消息了" + mag);
                            for (SelectionKey selectionKey : selector.keys()) {
                                Channel channel = selectionKey.channel();
                                if (channel instanceof SocketChannel && channel != sc) {
                                    ByteBuffer wrap = ByteBuffer.wrap(mag.getBytes());
                                    ((SocketChannel) channel).write(wrap);
                                }
                            }
                        }
                    } else if (key.isWritable()) {
                    }
                    iterator.remove();
                }
            }
        } catch (Exception e) {
            key.cancel();
            sc.close();
        }
    }

    @SneakyThrows
    @Test
    public void clint() {
        //创建选择器
        Selector selector = Selector.open();
        SocketChannel sc = SocketChannel.open(new InetSocketAddress("127.0.0.1", 998));
        sc.configureBlocking(false);
        sc.register(selector, SelectionKey.OP_READ);

        //接受事件
        new Thread(new Runnable() {
            @SneakyThrows
            @Override
            public void run() {
                while (selector.select() > 0) {
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while (iterator.hasNext()) {
                        SelectionKey key = iterator.next();
                        if (key.isReadable()) {
                            SocketChannel channel = (SocketChannel) key.channel();
                            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                            channel.read(byteBuffer);
                            System.out.println(new String(byteBuffer.array()).trim());
                        }
                        iterator.remove();
                    }
                }
            }
        }).start();

        ByteBuffer buffer = ByteBuffer.allocate(1024);
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("请说");
            String str = scanner.nextLine();
            buffer.put(str.getBytes());
            buffer.flip();
            sc.write(buffer);
            buffer.clear();
        }
    }

}
