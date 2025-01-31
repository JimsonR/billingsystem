package com.example.billingsystem.service;

import com.example.billingsystem.entity.Invoice;
import com.example.billingsystem.entity.Orders;
import com.example.billingsystem.events.InvoiceGeneratedEvent;
import com.example.billingsystem.repository.InvoiceRepository;
import com.example.billingsystem.repository.OrderRepository;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
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

@EventListener
//public Invoice generateInvoice(Orders order)throws Exception {
    public Invoice generateInvoice(InvoiceGeneratedEvent event)throws Exception {
    Orders order = event.getOrder();
        Invoice invoice = new Invoice();
    invoice.setOrders(order);
    invoice.setTotalPrice(order.getTotalPrice());
    invoice.setInvoiceDate(LocalDateTime.now());

    //Generate PDF
    try {
        String pdfPath = pdfGenerator.generateInvoicePdf(order, invoiceRepository.count()).getPath();
        invoice.setPdfPath(pdfPath);

        emailService.sendInvoiceEmail(order.getCustomer().getEmailId(), "Your Order Invoice", "Please find attached your invoice.", pdfPath);


    }catch (Exception e){
        System.err.println("Error while generating and sending invoice: " + e.getMessage());

    }

    return invoiceRepository.save(invoice);
}

}
