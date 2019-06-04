package com.idig8.springboot.springbootfirst;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.idig8.springboot.controller"})
public class SpringbootFirstApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootFirstApplication.class, args);
	}

}
