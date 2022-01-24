package com.esecondhand.esecondhand.controller;

import com.esecondhand.esecondhand.domain.entity.Item;
import com.esecondhand.esecondhand.domain.entity.ItemPicture;
import com.esecondhand.esecondhand.domain.entity.User;
import com.esecondhand.esecondhand.domain.repository.ItemPictureRepository;
import com.esecondhand.esecondhand.domain.repository.ItemRepository;
import com.esecondhand.esecondhand.service.ItemService;
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
public class ReportListener implements
        ApplicationListener<OnCreateReportCompleteEvent> {

    @Autowired
    private ItemPictureRepository itemPictureRepository;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Override
    public void onApplicationEvent(OnCreateReportCompleteEvent onCreateReportCompleteEvent) {
        this.reportItem(onCreateReportCompleteEvent);
    }

    public String generateMailHtml(String userName, String cause, String name)
    {
        Map<String, Object> variables = new HashMap<>();
        variables.put("username", userName);
        variables.put("cause", cause);
        variables.put("name", name);

        final String templateFileName = "report"; //Name of the template file without extension
        String output = this.templateEngine.process(templateFileName, new Context(Locale.getDefault(), variables));

        return output;
    }

    private void reportItem(OnCreateReportCompleteEvent event) {
        Item item = event.getItem();
        String cause = event.getCause();
        String recipientAddress = item.getUser().getEmail();
        String subject = "Report";
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        try {
            helper.setTo(recipientAddress);
            helper.setSubject(subject);
            ItemPicture itemPicture = itemPictureRepository.findMainImageUrlByItemId(item.getId());
            helper.setText(generateMailHtml(item.getUser().getDisplayName(), cause, item.getName()), true);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        mailSender.send(message);
    }
}
