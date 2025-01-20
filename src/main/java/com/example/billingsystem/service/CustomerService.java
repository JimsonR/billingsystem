package com.example.billingsystem.service;

import com.example.billingsystem.entity.Customer;
import com.example.billingsystem.model.CustomerModel;
import com.example.billingsystem.model.CustomerResponseDTO;
import com.example.billingsystem.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    public String createAndUpdate(CustomerModel customerModel) {

        if (customerModel.getId() != null) {
            Customer customer = customerRepository.findById(customerModel.getId()).orElseThrow(() -> new RuntimeException("Customer not found"));
            customer.setCustomerName(customerModel.getCustomerName());
            customer.setMobileNumber(customerModel.getMobileNumber());
            customer.setEmailId(customerModel.getEmailId());


            customerRepository.save(customer);

            return "Customer data updated";
        }

        Customer customer = Customer.builder().customerName(customerModel.getCustomerName())
                .emailId(customerModel.getEmailId())
                .mobileNumber(customerModel.getMobileNumber())
                .build();
        customerRepository.save(customer);
        return "Customer data created";
    }

    public CustomerResponseDTO getCustomer(Long id) {
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new RuntimeException("Customer not found"));

        return (CustomerResponseDTO) customer;
    }

    public String deleteCustomer(Long id) {
        customerRepository.deleteById(id);
        return "Customer deleted successfully";
    }



}
