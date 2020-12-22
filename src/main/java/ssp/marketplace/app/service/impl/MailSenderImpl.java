package ssp.marketplace.app.service.impl;

import freemarker.template.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.mail.javamail.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import ssp.marketplace.app.entity.user.*;
import ssp.marketplace.app.service.*;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Async
@Service
public class MailSenderImpl implements MailService {

    private final JavaMailSender mailSender;

    private final Configuration configuration;

    @Value(value = "${spring.mail.username}")
    private String username;

    @Autowired
    public MailSenderImpl(JavaMailSender mailSender, Configuration configuration) {
        this.mailSender = mailSender;
        this.configuration = configuration;
    }

    @Async
    @Override
    public void sendMail(String templateName, String mailSubject, Map<String, Object> data, User toUser) {
        if (toUser.getSendEmail().equals(MailAgreement.YES)){
            sendMailAnyway(templateName, mailSubject, data, toUser);
        }
    }

    @Override
    @Async
    public void sendMailAnyway(String templateName, String mailSubject, Map<String, Object> data, User toUser) {
        String recipientAddress = "<" + toUser.getEmail() + ">";
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
            freemarker.template.Template template = configuration.getTemplate(templateName + ".ftl", "UTF-8");
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, data);

            helper.setFrom(username);
            helper.setText(html, true);
            helper.setTo(recipientAddress);
            helper.setSubject(mailSubject);

            mailSender.send(message);
        } catch (MessagingException | IOException | TemplateException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    @Async
    @Override
    public void sendMassMail(String templateName, String mailSubject, Map<String, Object> data, List<User> toUsers) {
        try {
            MimeMessage[] messages = new MimeMessage[toUsers.size()];
            freemarker.template.Template template = configuration.getTemplate(templateName + ".ftl", "UTF-8");
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, data);

            for (int i = 0; i < messages.length; i++) {
                if (toUsers.get(i).getSendEmail().equals(MailAgreement.NO)){
                    continue;
                }
                String recipientAddress = "<" + toUsers.get(i).getEmail() + ">";

                MimeMessage message = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());

                helper.setFrom(username);
                helper.setText(html, true);
                helper.setTo(recipientAddress);
                helper.setSubject(mailSubject);

                messages[i] = message;
            }

            mailSender.send(messages);
        } catch (MessagingException | IOException | TemplateException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }
}
