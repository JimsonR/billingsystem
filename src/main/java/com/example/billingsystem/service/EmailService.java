package com.example.billingsystem.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendInvoiceEmail(String to , String subject, String text, String attachmentPath) throws MessagingException{
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message,true);

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text);

        //Add attachment

        FileSystemResource fileSystemResource = new FileSystemResource(new File(attachmentPath));
        helper.addAttachment(fileSystemResource.getFilename() , fileSystemResource);

        javaMailSender.send(message);
    }
}
