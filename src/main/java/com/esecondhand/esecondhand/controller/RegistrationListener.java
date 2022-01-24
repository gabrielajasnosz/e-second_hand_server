package com.esecondhand.esecondhand.controller;

import com.esecondhand.esecondhand.domain.entity.User;
import com.esecondhand.esecondhand.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

@Component
public class RegistrationListener implements
        ApplicationListener<OnRegistrationCompleteEvent> {

    @Autowired
    private UserService service;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Override
    public void onApplicationEvent(OnRegistrationCompleteEvent event) {
        this.confirmRegistration(event);
    }

    public String generateMailHtml(String userName, String link)
    {
        Map<String, Object> variables = new HashMap<>();
        variables.put("username", userName);
        variables.put("link", link);

        final String templateFileName = "mail"; //Name of the template file without extension
        String output = this.templateEngine.process(templateFileName, new Context(Locale.getDefault(), variables));

        return output;
    }

    private void confirmRegistration(OnRegistrationCompleteEvent event) {
        User user = event.getUser();
        String token = UUID.randomUUID().toString();
        service.createVerificationToken(user, token);

        String recipientAddress = user.getEmail();
        String subject = "Registration Confirmation";
        String confirmationUrl
                = event.getAppUrl() + "/users/registration/" + token;
        String messagee = "Click link below to confirm your account";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        try {
            helper.setTo(recipientAddress);
            helper.setSubject(subject);
            helper.setText(generateMailHtml(user.getDisplayName(),"http://localhost:3000" + confirmationUrl), true);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        mailSender.send(message);
    }
}
