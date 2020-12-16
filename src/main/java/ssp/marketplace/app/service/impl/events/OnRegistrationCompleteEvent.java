package ssp.marketplace.app.service.impl.events;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEvent;
import ssp.marketplace.app.entity.user.User;

@Getter
@Setter
@Slf4j
public class OnRegistrationCompleteEvent extends ApplicationEvent {

    private final String appUrl;
    private User user;

    public OnRegistrationCompleteEvent(User user, String appUrl) {
        super(user);
        this.user = user;
        this.appUrl = appUrl;
    }
}
