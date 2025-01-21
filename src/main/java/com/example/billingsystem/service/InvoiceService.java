package com.example.billingsystem.service;

import com.example.billingsystem.repository.InvoiceRepository;
import com.example.billingsystem.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InvoiceService {
@Autowired
    private InvoiceRepository invoiceRepository;

@Autowired
    private OrderRepository orderRepository;

@Autowired
    private EmailService emailService;

@Autowired
}
