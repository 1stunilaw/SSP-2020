package ssp.marketplace.app.service;

import ssp.marketplace.app.entity.user.User;

import java.util.*;

public interface MailService {

    void sendMail(String templateName, String mailSubject, Map<String, Object> data, User toUser);

    void sendMassMail(String templateName, String mailSubject, Map<String, Object> data, List<User> toUsers);
}
