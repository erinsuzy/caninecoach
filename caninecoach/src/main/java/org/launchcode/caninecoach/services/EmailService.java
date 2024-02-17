package org.launchcode.caninecoach.services;

import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;
    private final FreeMarkerConfigurer freemarkerConfigurer;

    @Autowired
    public EmailService(JavaMailSender mailSender, FreeMarkerConfigurer freemarkerConfigurer) {
        this.mailSender = mailSender;
        this.freemarkerConfigurer = freemarkerConfigurer;
    }

    public void sendSimpleVerificationEmail(String to, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("caninecochapp@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);
        mailSender.send(message);
    }

    public void sendTemplateVerificationEmail(String to, String subject, String verificationLink) throws IOException, TemplateException {
        Template template = freemarkerConfigurer.getConfiguration().getTemplate("verificationEmailTemplate.ftl");

        Map<String, Object> model = new HashMap<>();
        model.put("verificationUrl", verificationLink);

        String content = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("caninecochapp@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);
        mailSender.send(message);
    }
}
