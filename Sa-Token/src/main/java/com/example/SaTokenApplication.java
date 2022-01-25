package com.example;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

import java.net.InetAddress;
import java.net.UnknownHostException;

@SpringBootApplication
@Slf4j
public class SaTokenApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext applicationContext = SpringApplication.run(SaTokenApplication.class, args);
		ConfigurableEnvironment environment = applicationContext.getEnvironment();
		String ipAddress = "127.0.0.1";
		try {
			ipAddress = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		String port = environment.getProperty("server.port");
		String contextPath = environment.getProperty("server.servlet.context-path");
		String active = environment.getProperty("spring.profiles.active");
		String maxFileSize = environment.getProperty("spring.servlet.multipart.max-file-size"); //最大文件大小
		String maxRequestSize = environment.getProperty("spring.servlet.multipart.max-request-size"); //最大请求大小
		log.info("\n----------------------------------------------------------\n\t" +
				"Application is running! Access URLs:\n\t" +
				"Doc: \t\thttp://" + ipAddress + ":" + port + "/" + "doc.html\n\t" +
				"spring-profiles-active: \t\t" + active + "\n\t" +
				"max-file-size: \t\t" + maxFileSize + "\n\t" +
				"max-request-size: \t\t" + maxRequestSize + "\n" +
				"----------------------------------------------------------");
	}

}
