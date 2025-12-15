package com.project.services;


import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EmailService {

//    @Autowired
//    private JavaMailSender jms;

    @Value("${sendgrid.key}")
    private String apiKey;

    @Value("${email.from}")
    private String fromEmail;

    @Async
    public void sendEmail(String toEmail, String subject, String body) {

        System.out.println("Email thread: " + Thread.currentThread().getName());


        Email from = new Email(fromEmail);
        Email to = new Email(toEmail);

        Content content = new Content("text/plain", body);
        Mail mail = new Mail(from, subject, to, content);

        SendGrid sendGrid = new SendGrid(apiKey);

        try {
            Request request = new Request();
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            sendGrid.api(request);
        } catch (Exception e) {
            System.err.println("Failed to send email: " + e.getMessage()+" to "+toEmail);
        }
    }
}

//    @Autowired
//    private JavaMailSender jms;
//
//    @Transactional
//    public void sendEmail(String toEmail, String subject,String body) {
//
//        SimpleMailMessage message=new SimpleMailMessage();
//        message.setSubject(subject);
//        message.setTo(toEmail);
//        message.setText(body);
//
//        jms.send(message);
//    }


//}