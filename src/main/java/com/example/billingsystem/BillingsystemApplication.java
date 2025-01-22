package com.example.billingsystem;

import com.example.billingsystem.entity.Customer;
import com.example.billingsystem.model.CustomerModel;
import com.example.billingsystem.service.CustomerService;
import com.example.billingsystem.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class BillingsystemApplication {

	public static void main(String[] args) {


		SpringApplication.run(BillingsystemApplication.class, args);
	}

}
