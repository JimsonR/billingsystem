package com.example.billingsystem.service;

import com.example.billingsystem.entity.Orders;
import com.example.billingsystem.entity.Product;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.draw.LineSeparator;


import java.io.File;
import java.io.FileOutputStream;

public class PdfGenerator {
    public File generateInvoicePdf(Orders order)throws Exception{
        //Create a document and set up PDF Output

        Document document = new Document();

        File pdfFile = new File("invoices/invoice_"+ order.getOrderId()+".pdf");
        PdfWriter.getInstance(document, new FileOutputStream(pdfFile));

        //Open the document for writing
        document.open();

        //Add the title and other information to the pdf
        Font boldFont = new Font(Font.HELVETICA,12, Font.BOLD);
        Font normalFont = new Font(Font.HELVETICA,12);

        //add invoice header
        document.add(new Paragraph("Invoice #"+order.getOrderId(),boldFont));
        document.add(new Paragraph("Customer:" +order.getCustomer().getCustomerName(),normalFont));
        document.add(new Paragraph("Order date:"+order.getOrderDate(),normalFont));
        document.add(new Paragraph("Total price:"+order.getTotalPrice(),normalFont));

        //Add a seperator line
        document.add(new Chunk(new LineSeparator()));

        //Add product details
        document.add(new Paragraph("Products:",boldFont));
        for (Product productId : order.getProducts()){
            document.add(new Paragraph("Product ID:"+productId.getProductId(),normalFont));
        }

        //Add total price and footer
        document.add(new Paragraph("Total amount:"+order.getTotalPrice(),boldFont));

        //Close the document after all content is written

        document.close();

        return pdfFile;



    }
}
