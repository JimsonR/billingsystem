package com.example.billingsystem;

import com.example.billingsystem.entity.Customer;
import com.example.billingsystem.entity.Inventory;
import com.example.billingsystem.model.CustomerModel;
import com.example.billingsystem.repository.InventoryRepository;
import com.example.billingsystem.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BillingsystemApplicationTests {
@Autowired
	CustomerService service;
@Autowired
private InventoryRepository inventoryRepository;
	@Test
	void contextLoads() {
//		CustomerModel customerModel1 = CustomerModel.builder().customerName("Allu Arjun").mobileNumber("12345").emailId("jimmybalor@gmail.com").build();
		CustomerModel customerModel2 = CustomerModel.builder().id(1L).customerName("jyothika").mobileNumber("1234").emailId("karrimsettijyothikadevi@gmail.com").build();

//		CustomerModel customerModel2 = CustomerModel.builder().id(2L).customerName("kishore").mobileNumber("123").emailId("krrish533@gmail.com").build();



	service.createAndUpdate(customerModel2);
//	service.createAndUpdate(customerModel);
//		for (Inventory inventory : inventoryRepository.findAll()){
//			inventory.setOriginalStockQuantity(inventory.getStockQuantity());
//			inventoryRepository.save(inventory);
//		}
	}

}
