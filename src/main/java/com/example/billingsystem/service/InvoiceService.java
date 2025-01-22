package com.example.billingsystem.service;

import com.example.billingsystem.entity.Invoice;
import com.example.billingsystem.entity.Orders;
import com.example.billingsystem.repository.InvoiceRepository;
import com.example.billingsystem.repository.OrderRepository;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class InvoiceService {
@Autowired
    private InvoiceRepository invoiceRepository;

@Autowired
    private OrderRepository orderRepository;

@Autowired
    private EmailService emailService;

@Autowired
    private PdfGenerator pdfGenerator;

public Invoice generateInvoice(Orders order)throws Exception {
    Invoice invoice = new Invoice();
    invoice.setOrders(order);
    invoice.setTotalPrice(order.getTotalPrice());
    invoice.setInvoiceDate(LocalDateTime.now());

    //Generate PDF
    String pdfPath = pdfGenerator.generateInvoicePdf(order).getPath();
    invoice.setPdfPath(pdfPath);

    emailService.sendInvoiceEmail(order.getCustomer().getEmailId(),"Your Order Invoice","Please find attached your invoice.",pdfPath);

    return invoiceRepository.save(invoice);
}

}
