package com.example.billingsystem.service;

import com.example.billingsystem.entity.Inventory;
import com.example.billingsystem.entity.Orders;
import com.example.billingsystem.entity.Product;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import com.lowagie.text.pdf.draw.LineSeparator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;


import java.io.File;
import java.io.FileOutputStream;
import java.text.Format;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class PdfGenerator {

    @Autowired
    private InventoryService inventoryService;

    public File generateInvoicePdf(Orders order , long count)throws Exception{
        //Create a document and set up PDF Output

        File directory = new File("invoices");

        if (!directory.exists()){
            directory.mkdirs();
        }

        Document document = new Document(PageSize.A4);

        File pdfFile = new File("invoices/invoice_"+ count+".pdf");
        PdfWriter.getInstance(document, new FileOutputStream(pdfFile));

        //Open the document for writing
        document.open();

        //logo
        String logoPath =  getClass().getClassLoader().getResource("images/logo.png").getPath();
        Image logo = Image.getInstance(logoPath);

        logo.scaleToFit(50,50);

        logo.setAlignment(Element.ALIGN_RIGHT);

//        document.add(logo);

        //Font setup
        String fontPath = getClass().getClassLoader().getResource("fonts/Roboto-Regular.ttf").getPath();

        BaseFont unicodeFont = BaseFont.createFont(fontPath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);

        Font titleFont = new Font(Font.HELVETICA, 16, Font.BOLD);
        Font boldFont = new Font(unicodeFont,12, Font.BOLD);
        Font normalFont = new Font(unicodeFont,12);
        Font footerFont = new Font(Font.HELVETICA, 10 , Font.ITALIC);

        // Add the title of the invoice

        PdfPTable headerTable = new PdfPTable(2);

        float[] columnWidths = {1f, 0.2f};
        headerTable.setWidths(columnWidths);


        PdfPCell textCell = new PdfPCell(new Phrase("Invoice", titleFont));
        textCell.setVerticalAlignment(Element.ALIGN_BOTTOM);
        textCell.setBorder(0);
        headerTable.addCell(textCell);

        PdfPCell logoCell = new PdfPCell(logo);
        logoCell.setBorder(0);
        logoCell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
        headerTable.addCell(logoCell);
        document.add(headerTable);
//        document.add(new Paragraph("Invoice", titleFont));
//        document.add(logo);
        document.add(new Chunk(new LineSeparator()));
        document.add(Chunk.NEWLINE);


        //add order and customer information
        document.add(new Paragraph("Invoice No: "+count,boldFont));
        document.add(new Paragraph("Customer Name: " +order.getCustomer().getCustomerName(),normalFont));
//        document.add(new Paragraph("Email: "+ order.getCustomer().getEmailId(),normalFont));
        document.add(new Paragraph("PhoneNo: "+order.getCustomer().getMobileNumber(),normalFont));

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String formattedDate = order.getOrderDate().format(dateTimeFormatter);


        document.add(new Paragraph("Date: "+formattedDate,normalFont));
//        document.add(new Paragraph("Total price: "+order.getTotalPrice(),normalFont));

        //Add a separator line
        document.add(new Chunk(new LineSeparator()));
        document.add(Chunk.NEWLINE);

        //Add product details in a table
        PdfPTable table = new PdfPTable(4);// 4 columns for product id, name, Quantity, Price
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);

        //Add table Headers

        PdfPCell cell = new PdfPCell(new Phrase("Product ID", boldFont));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Product Name", boldFont));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
//        table.addCell(new Phrase("Product Name", boldFont));
        cell = new PdfPCell(new Phrase("Quantity", boldFont));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
//        table.addCell(new Phrase("Quantity", boldFont));
        cell = new PdfPCell(new Phrase("Price", boldFont));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
//        table.addCell(new Phrase("Price", boldFont));

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
            PdfPCell cell1 = new PdfPCell(new Phrase(String.valueOf(productId.getProductId()),normalFont));
            cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell1);

            cell1 = new PdfPCell(new Phrase(productId.getName(),normalFont));
            cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell1);

            cell1 = new PdfPCell(new Phrase(String.valueOf(map.get(productId)),normalFont));
            cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell1);

//            table.addCell(new Phrase(String.valueOf(productId.getProductId()),normalFont));
//            table.addCell(new Phrase(productId.getName(),normalFont));
//            table.addCell(new Phrase(String.valueOf(map.get(productId))));

             cell1 = new PdfPCell(new Phrase(String.format("₹%.2f",inventoryService.findByProdId(productId.getProductId()).getFirst().getUnitPrice()),normalFont));
            cell1.setHorizontalAlignment(Element.ALIGN_RIGHT);
//            table.addCell(new Phrase(String.format("₹%.2f",inventoryService.findByProdId(productId.getProductId()).getFirst().getUnitPrice()),normalFont));
        table.addCell(cell1);

        }
        table.addCell(new Phrase());
        table.addCell(new Phrase());
        table.addCell(new Phrase());
        PdfPCell cell1 = new PdfPCell(new Phrase("Total: " + String.format("₹%.2f",order.getTotalPrice()),boldFont));
        cell1.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cell1);


        //add table to the document
        document.add(table);
        document.add(Chunk.NEWLINE);


        //Add total price and footer
        document.add(new Paragraph("Total amount:"+String.format("₹%.2f",order.getTotalPrice()),boldFont));
        document.add(new Chunk(new LineSeparator()));
        document.add(new Paragraph("Thank you for your business!",footerFont));
        //Close the document after all content is written

        document.close();

        return pdfFile;
    }
}
