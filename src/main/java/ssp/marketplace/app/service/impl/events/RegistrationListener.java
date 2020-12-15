package ssp.marketplace.app.service.impl.events;

import org.springframework.beans.factory.annotation.*;
import org.springframework.context.*;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import ssp.marketplace.app.entity.*;
import ssp.marketplace.app.service.UserService;

import java.util.Locale;

@Component
@Async
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {

    private UserService userService;
    private MessageSource messageSource;
    private JavaMailSender mailSender;

    @Value(value = "${spring.mail.username}")
    private String username;

    @Value(value = "${frontend.url}")
    private String frontendUrl;

    @Autowired
    public RegistrationListener(UserService userService, MessageSource messageSource, JavaMailSender mailSender) {
        this.userService = userService;
        this.messageSource = messageSource;
        this.mailSender = mailSender;
    }

    @Override
    public void onApplicationEvent(OnRegistrationCompleteEvent event) {
        confirmRegistration(event);
    }

    @Async
    public void confirmRegistration(OnRegistrationCompleteEvent event){
        User user = event.getUser();

        VerificationToken token = userService.createVerificationToken(user);

        sendEmail(user, token);
    }

    private void sendEmail(User user, VerificationToken token){
        String recipientAddress = "<" + user.getEmail() + ">";
        String subject = "Подтверждение регистрации";
        String confirmationUrl = frontendUrl + "/api/register/verify?token=" + token.getId();
        String message = messageSource.getMessage("email.verify.message", null, new Locale("ru", "RU"));

        SimpleMailMessage email = new SimpleMailMessage();
        email.setFrom(username);
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(message + "\r\n" + confirmationUrl);

        mailSender.send(email);
    }
}
