package com.example.billingsystem.controller;

import com.example.billingsystem.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("order/invoice")
public class InvoiceController {
    @Autowired
    private InvoiceService invoiceService;



}
