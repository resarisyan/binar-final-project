package com.binar.byteacademy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class ByteacademyApplication {

	public static void main(String[] args) {
		SpringApplication.run(ByteacademyApplication.class, args);
	}

}
