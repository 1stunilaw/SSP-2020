package ssp.marketplace.app.service.impl.events;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.*;
import org.springframework.context.event.EventListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import ssp.marketplace.app.entity.*;
import ssp.marketplace.app.service.UserService;

import java.util.*;

@Slf4j
@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {

    private UserService userService;
    private MessageSource messageSource;
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String username;


    @Autowired
    public RegistrationListener(UserService userService, MessageSource messageSource, JavaMailSender mailSender) {
        log.info("Listener created");
        this.userService = userService;
        this.messageSource = messageSource;
        this.mailSender = mailSender;
    }

    @Override
    public void onApplicationEvent(OnRegistrationCompleteEvent event) {
        log.info("Event caught");
        confirmRegistration(event);
    }

    public void confirmRegistration(OnRegistrationCompleteEvent event){
        User user = event.getUser();

        VerificationToken token = userService.createVerificationToken(user);

        String recipientAddress = "<" + user.getEmail() + ">";
        String subject = "Подтверждение регистрации";
        String confirmationUrl = event.getAppUrl() + "/api/v1/register/verify?token=" + token.getId();
        String message = messageSource.getMessage("email.verify.message", null, new Locale("ru", "RU"));

        SimpleMailMessage email = new SimpleMailMessage();
        email.setFrom(username);
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(message + "\r\n" + confirmationUrl);

        log.info("Sending email");
        mailSender.send(email);

        log.info("Email sended");
    }
}
