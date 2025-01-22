package com.example.billingsystem.service;

import com.example.billingsystem.entity.Inventory;
import com.example.billingsystem.entity.Orders;
import com.example.billingsystem.entity.Product;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.draw.LineSeparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;


import java.io.File;
import java.io.FileOutputStream;
import java.text.Format;
import java.util.HashMap;
import java.util.Map;

@Service
public class PdfGenerator {

    @Autowired
    private InventoryService inventoryService;

    public File generateInvoicePdf(Orders order)throws Exception{
        //Create a document and set up PDF Output

        File directory = new File("invoices");

        if (!directory.exists()){
            directory.mkdirs();
        }

        Document document = new Document(PageSize.A4);

        File pdfFile = new File("invoices/invoice_"+ order.getOrderId()+".pdf");
        PdfWriter.getInstance(document, new FileOutputStream(pdfFile));

        //Open the document for writing
        document.open();

        //Font setup
        Font titleFont = new Font(Font.HELVETICA, 16, Font.BOLD);
        Font boldFont = new Font(Font.HELVETICA,12, Font.BOLD);
        Font normalFont = new Font(Font.HELVETICA,12);
        Font footerFont = new Font(Font.HELVETICA, 10 , Font.ITALIC);

        // Add the title of the invoice
        document.add(new Paragraph("Invoice", titleFont));
        document.add(new Chunk(new LineSeparator()));
        document.add(Chunk.NEWLINE);


        //add order and customer information
        document.add(new Paragraph("Invoice #"+order.getOrderId(),boldFont));
        document.add(new Paragraph("Customer: " +order.getCustomer().getCustomerName(),normalFont));
        document.add(new Paragraph("Email: "+ order.getCustomer().getEmailId(),normalFont));
        document.add(new Paragraph("Phone: "+order.getCustomer().getMobileNumber(),normalFont));
        document.add(new Paragraph("Order date: "+order.getOrderDate(),normalFont));
        document.add(new Paragraph("Total price: "+order.getTotalPrice(),normalFont));

        //Add a separator line
        document.add(new Chunk(new LineSeparator()));
        document.add(Chunk.NEWLINE);

        //Add product details in a table
        PdfPTable table = new PdfPTable(4);// 4 columns for product id, name, Quantity, Price
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);

        //Add table Headers
        table.addCell(new Phrase("Product ID", boldFont));
        table.addCell(new Phrase("Product Name", boldFont));
        table.addCell(new Phrase("Quantity", boldFont));
        table.addCell(new Phrase("Price", boldFont));

        //Add product details
        document.add(new Paragraph("Products:",boldFont));

        Map<Product,Integer> map = new HashMap<>();
        for(Product product : order.getProducts()){
            if(map.containsKey(product)){
                map.put(product, map.get(product)+1);
            }
            else{
                map.put(product,1);
            }
        }


        for (Product productId : map.keySet()){
//            document.add(new Paragraph("Product ID:"+productId.getProductId()+" Product Name:"+productId.getName(),normalFont ));
            table.addCell(new Phrase(String.valueOf(productId.getProductId()),normalFont));
            table.addCell(new Phrase(productId.getName(),normalFont));
            table.addCell(new Phrase(String.valueOf(map.get(productId))));
            table.addCell(new Phrase(String.format("$%.2f",inventoryService.findByProdId(productId.getProductId()).getFirst().getUnitPrice()),normalFont));


        }

        //add table to the document
        document.add(table);
        document.add(Chunk.NEWLINE);


        //Add total price and footer
        document.add(new Paragraph("Total amount:"+String.format("$%.2f",order.getTotalPrice()),boldFont));
        document.add(new Chunk(new LineSeparator()));
        document.add(new Paragraph("Thank you for your business!",footerFont));
        //Close the document after all content is written

        document.close();

        return pdfFile;
    }
}
