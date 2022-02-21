package com.example.nio;

import lombok.SneakyThrows;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

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
        channel.close();
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
        fisChannel.transferTo(fisChannel.position(),fisChannel.size(),fosChannel);
        fisChannel.close();
        fosChannel.close();

    }
}
