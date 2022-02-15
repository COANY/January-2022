package com.example;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StudynettyApplication {

	public static void main(String[] args) {
		SpringApplication.run(StudynettyApplication.class, args);
	}

	void aa() {
		try {
			EventLoopGroup boos = new NioEventLoopGroup(1);
			EventLoopGroup worker = new NioEventLoopGroup();
			ServerBootstrap sbs = new ServerBootstrap();
			sbs.option(ChannelOption.SO_BACKLOG, 1024)
					.group(boos, worker)
					.channel(NioServerSocketChannel.class)
					.handler(new LoggingHandler(LogLevel.INFO))
					.childHandler(new LoggingHandler(LogLevel.INFO));

			Channel channel = sbs.bind().sync().channel();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}


	}

}
