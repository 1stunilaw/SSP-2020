package ssp.marketplace.app.dto.user.customer;

import lombok.*;
import ssp.marketplace.app.dto.user.ResponseUserDto;
import ssp.marketplace.app.entity.user.*;

@Getter
@Setter
public class ResponseCustomerDto extends ResponseUserDto {
    private String fio;
    private String phone;
    private Boolean sendEmail;

    public ResponseCustomerDto(User user) {
        super(user);
        fio = user.getCustomerDetails().getFio();
        phone = user.getCustomerDetails().getPhone();
        sendEmail = user.getSendEmail().equals(MailAgreement.YES);
    }
}
