package ssp.marketplace.app.service.impl.events;

import org.mapstruct.ap.shaded.freemarker.template.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.*;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import ssp.marketplace.app.entity.user.*;
import ssp.marketplace.app.service.*;

import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Component
@Async
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {

    private final UserService userService;
    private final MailService mailService;

    @Value(value = "${spring.mail.username}")
    private String username;

    @Value(value = "${frontend.url}")
    private String frontendUrl;



    @Autowired
    public RegistrationListener(UserService userService, MailService mailService) {
        this.userService = userService;
        this.mailService = mailService;
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
        Map<String, Object> data = new HashMap<>();
        data.put("url", frontendUrl + "/verify");
        data.put("token", token.getId().toString());
        mailService.sendMail("verification", "Подтверждение регистрации", data, user);
    }
}
