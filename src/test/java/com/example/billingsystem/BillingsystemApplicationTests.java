package com.example.billingsystem;

import com.example.billingsystem.entity.Customer;
import com.example.billingsystem.model.CustomerModel;
import com.example.billingsystem.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BillingsystemApplicationTests {
@Autowired
	CustomerService service;
	@Test
	void contextLoads() {
		CustomerModel customerModel1 = CustomerModel.builder().customerName("Allu Arjun").mobileNumber("12345").emailId("allu@gmail.com").build();

		CustomerModel customerModel2 = CustomerModel.builder().customerName("Arjun").mobileNumber("123457").emailId("allu@gmail.com").build();



	service.createAndUpdate(customerModel1);
	service.createAndUpdate(customerModel2);
	}

}
