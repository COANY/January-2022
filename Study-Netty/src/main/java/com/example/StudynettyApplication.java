package com.example;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.junit.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StudynettyApplication {

	public static void main(String[] args) {
		SpringApplication.run(StudynettyApplication.class, args);
	}

	@Test
	void aa() {
		try {
			EventLoopGroup boos = new NioEventLoopGroup(1);//接受请求  接受ServerSocketChannel的请求
			EventLoopGroup worker = new NioEventLoopGroup();//执行其请求  处理SocketChannel的请求
			ServerBootstrap sbs = new ServerBootstrap();
			//设置一些信息
			sbs.option(ChannelOption.SO_BACKLOG, 1024)
					.group(boos, worker)
					.channel(NioServerSocketChannel.class)
					//设置处理ServerSocketChannel的处理器  打印日志接受到什么连接
					.handler(new LoggingHandler(LogLevel.INFO))
					//处理SocketChannel 和响应SocketChannel的请求
					.childHandler(new LoggingHandler(LogLevel.INFO));
			//bind 绑定端口 启动
			Channel channel = sbs.bind(999).sync().channel();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}


	}

}
