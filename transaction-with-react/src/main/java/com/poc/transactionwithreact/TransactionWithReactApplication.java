package com.poc.transactionwithreact;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@EnableJpaRepositories(basePackages = {"com.poc.transactionwithreact.repository"})
public class TransactionWithReactApplication {

	public static void main(String[] args) {
		SpringApplication.run(TransactionWithReactApplication.class, args);
	}

}
